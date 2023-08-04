package com.jaydev.github.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaydev.github.domain.NetResult
import com.jaydev.github.domain.entity.NetError
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

typealias Invokable = () -> Unit

abstract class BaseViewModel : ViewModel() {
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
        host: ContainerHost<*, UiSideEffect>,
        dialogAction: Invokable = {}
    ) = onEach {
        handleError(it, host, dialogAction)
    }

    private fun handleError(
        result: NetResult<*>,
        host: ContainerHost<*, UiSideEffect>,
        dialogAction: Invokable
    ) {
        if (result !is NetResult.Error) return
        viewModelScope.launch {
            when (result.error) {
                NetError.Network -> {
                    val title = "네트워크 에러"
                    val message = "인터넷 연결 안됨."
                    host.intent {
                        postSideEffect(
                            BaseSideEffect.ShowDialog(
                                title,
                                message,
                                action = dialogAction
                            )
                        )
                    }
                }

                is NetError.InternalServer -> {
                    val (errorCode, errorMessage) = result.error as NetError.InternalServer

                    val title = "네트워크 에러"
                    val message = "서버 에러\n" +
                            "code: $errorCode\n" +
                            "message: $errorMessage"
                    host.intent {
                        postSideEffect(
                            BaseSideEffect.ShowDialog(
                                title,
                                message,
                                action = dialogAction
                            )
                        )
                    }
                }

                is NetError.Timeout -> {
                    val title = "네트워크 에러"
                    val message = "타임 아웃."
                    host.intent {
                        postSideEffect(
                            BaseSideEffect.ShowDialog(
                                title,
                                message,
                                action = dialogAction
                            )
                        )
                    }
                }

                is NetError.Unknown -> {
                    val title = "네트워크 에러"
                    val message = "알 수 없는 에러."
                    host.intent {
                        postSideEffect(BaseSideEffect.ShowDialog(title, message))
                    }

                    val throwable = (result.error as NetError.Unknown).throwable
                    Logger.e(throwable, "Unknown Error")
                }

                is NetError.BadRequest -> {
                    // proceed at onFailure
                }
            }
        }
    }

    protected fun <T> Flow<NetResult<T>>.call() = cancellable().launchIn(viewModelScope)

    protected fun <T> Flow<NetResult<T>>.load(
        loading: (Boolean) -> Unit
    ): Job {
        val loadingProgress = ContentLoadingProgress(viewModelScope)
        return onStart {
            loadingProgress.handleContentLoading(loading, true)
        }.onCompletion {
            loadingProgress.handleContentLoading(loading, false)
        }.cancellable()
            .launchIn(viewModelScope)
    }

    protected fun <T> Flow<NetResult<T>>.load(
        loading: MutableStateFlow<Boolean>
    ): Job {
        val loadingProgress = ContentLoadingProgress(viewModelScope)
        return onStart {
            loadingProgress.handleContentLoading(loading, true)
        }.onCompletion {
            loadingProgress.handleContentLoading(loading, false)
        }.cancellable()
            .launchIn(viewModelScope)
    }
}
