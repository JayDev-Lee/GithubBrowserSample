package com.jaydev.github.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaydev.github.domain.NetResult
import com.jaydev.github.domain.entity.NetError
import com.jaydev.github.model.AlertUIModel
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

typealias InvokableAction = () -> Unit

abstract class BaseViewModel : ViewModel() {
    private val loadingProgress = ContentLoadingProgress(viewModelScope)

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    private val _showToast = MutableSharedFlow<String>()
    val showToast = _showToast.asSharedFlow()

    private val _showAlertDialog = MutableSharedFlow<AlertUIModel.Dialog>()
    val showAlertDialog = _showAlertDialog.asSharedFlow()

    private val _showActionDialog = MutableSharedFlow<Pair<AlertUIModel.Dialog, InvokableAction>>()
    val showActionDialog = _showActionDialog.asSharedFlow()

    private val _navigateToBack = MutableSharedFlow<Unit>()
    val navigateToBack = _navigateToBack.asSharedFlow()

    protected fun navigateToBack() {
        viewModelScope.launch {
            _navigateToBack.emit(Unit)
        }
    }

    protected fun showAlert(alert: AlertUIModel, action: InvokableAction? = null) {
        when (alert) {
            is AlertUIModel.Dialog -> {
                if (action != null) showActionDialog(alert, action)
                else showAlertDialog(alert)
            }

            is AlertUIModel.Toast -> {
                showToast(alert.message)
            }
        }
    }

    protected fun showToast(message: String) {
        viewModelScope.launch {
            _showToast.emit(message)
        }
    }

    protected fun showActionDialog(alertDialog: AlertUIModel.Dialog, action: InvokableAction) {
        viewModelScope.launch {
            _showActionDialog.emit(Pair(alertDialog, action))
        }
    }

    protected fun showAlertDialog(alertDialog: AlertUIModel.Dialog) {
        viewModelScope.launch {
            _showAlertDialog.emit(alertDialog)
        }
    }

    protected fun showAlertDialog(title: String, message: CharSequence) {
        viewModelScope.launch {
            _showAlertDialog.emit(AlertUIModel.Dialog(title, message))
        }
    }

    fun progress(isLoading: Boolean) {
        _loading.value = isLoading
    }

    protected fun <T> Flow<NetResult<T>>.onSuccess(
        success: (suspend (T) -> Unit)? = null
    ) = onEach {
        if (it is NetResult.Success) success?.invoke(it.response)
    }

    protected fun <T> Flow<NetResult<T>>.onFailure(
        failure: (suspend (NetError.BadRequest) -> Unit)? = null
    ) = onEach {
        if (it is NetResult.Error && it.error is NetError.BadRequest) {
            failure?.invoke(it.error as NetError.BadRequest)
        }
    }

    protected fun <T> Flow<NetResult<T>>.commonErrorHandler(
        action: InvokableAction = this@BaseViewModel::navigateToBack
    ) = onEach {
        handleError(it, action)
    }

    private fun handleError(result: NetResult<*>, action: InvokableAction) {
        if (result !is NetResult.Error) return
        viewModelScope.launch {
            when (result.error) {
                NetError.Network -> {
                    val title = "네트워크 에러"
                    val message = "인터넷 연결 안됨."

                    _showActionDialog.emit(Pair(AlertUIModel.Dialog(title, message), action))
                }

                is NetError.InternalServer -> {
                    val (errorCode, errorMessage) = result.error as NetError.InternalServer

                    val title = "네트워크 에러"
                    val message = "서버 에러\n" +
                            "code: $errorCode\n" +
                            "message: $errorMessage"
                    _showActionDialog.emit(Pair(AlertUIModel.Dialog(title, message), action))
                }

                is NetError.Timeout -> {
                    val title = "네트워크 에러"
                    val message = "타임 아웃."
                    _showActionDialog.emit(Pair(AlertUIModel.Dialog(title, message), action))
                }

                is NetError.Unknown -> {
                    val title = "네트워크 에러"
                    val message = "알 수 없는 에러."
                    _showAlertDialog.emit(AlertUIModel.Dialog(title, message))

                    val throwable = (result.error as NetError.Unknown).throwable
                    Logger.e(throwable, "Unknown Error")
                }

                is NetError.BadRequest -> {
                    // no-op
                }
            }
        }
    }

    protected fun <T> Flow<NetResult<T>>.call() = cancellable().launchIn(viewModelScope)

    protected fun <T> Flow<NetResult<T>>.load(
        loading: (Boolean) -> Unit
    ) = onStart {
        loading.invoke(true)
    }.onCompletion {
        loading.invoke(false)
    }.cancellable()
        .launchIn(viewModelScope)

    protected fun <T> Flow<NetResult<T>>.load(
        loading: MutableStateFlow<Boolean> = _loading
    ) = onStart {
        loadingProgress.handleContentLoading(loading, true)
    }.onCompletion {
        loadingProgress.handleContentLoading(loading, false)
    }.cancellable()
        .launchIn(viewModelScope)
}
