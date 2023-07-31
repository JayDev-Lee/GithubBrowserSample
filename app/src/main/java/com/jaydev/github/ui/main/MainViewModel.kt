package com.jaydev.github.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jaydev.github.base.BaseViewModel
import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User
import com.jaydev.github.domain.interactor.usecase.GetUserDataUseCase
import com.jaydev.github.model.AlertUIModel
import com.jaydev.github.model.MainListItem
import com.jaydev.github.model.RepoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val getUserData: GetUserDataUseCase
) : BaseViewModel() {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _refreshListData = MutableStateFlow<List<MainListItem>>(emptyList())
    val refreshListData = _refreshListData.asStateFlow()

    private val _navigateProfile = MutableSharedFlow<String>()
    val navigateProfile = _navigateProfile.asSharedFlow()

    private val _navigateRepoDetail = MutableSharedFlow<RepoData>()
    val navigateRepoDetail = _navigateRepoDetail.asSharedFlow()

    private val _showProgress = MutableStateFlow(false)
    val showProgress = _showProgress.asStateFlow()

    init {
        val userName = handle.get<String>("userName") ?: ""

        getUserData(GetUserDataUseCase.Params(userName))
            .onSuccess {
                val list = mutableListOf<MainListItem>()
                list.add(MainListItem.Header(it.first))
                list.addAll(
                    it.second.map { repo ->
                        MainListItem.RepoItem(repo)
                    }
                )
                _refreshListData.value = list
            }.onFailure {
                showAlertDialog(AlertUIModel.Dialog("통신 실패", it.message))
            }.commonErrorHandler()
            .load { isLoading ->
                _showProgress.value = isLoading
            }
    }


    fun onClickUser(user: User) {
        viewModelScope.launch {
            _navigateProfile.emit(user.name)
        }
    }

    fun onClickRepo(user: User, repo: Repo) {
        viewModelScope.launch {
            _navigateRepoDetail.emit(RepoData(user.name, repo.name))
        }
    }
}