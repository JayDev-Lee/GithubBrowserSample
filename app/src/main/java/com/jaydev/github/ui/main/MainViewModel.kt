package com.jaydev.github.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jaydev.github.base.BaseViewModel
import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User
import com.jaydev.github.domain.interactor.usecase.GetUserDataUseCase
import com.jaydev.github.model.MainListItem
import com.jaydev.github.model.PopupMessage
import com.jaydev.github.model.RepoData
import kotlinx.coroutines.launch

class MainViewModel(
    userName: String,
    private val getUserData: GetUserDataUseCase
) : BaseViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _refreshListData = MutableLiveData<List<MainListItem>>()
    val refreshListData: LiveData<List<MainListItem>> = _refreshListData

    private val _navigateProfile = MutableLiveData<String>()
    val navigateProfile: LiveData<String> = _navigateProfile

    private val _navigateRepoDetail = MutableLiveData<RepoData>()
    val navigateRepoDetail: LiveData<RepoData> = _navigateRepoDetail

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean> = _showProgress

    init {
        viewModelScope.launch {
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
                    showAlertDialog(PopupMessage("통신 실패", it.message))
                }.commonErrorHandler()
                .load { isLoading ->
                    _showProgress.value = isLoading
                }
        }
    }


    fun onClickUser(user: User) {
        _navigateProfile.value = user.name
    }

    fun onClickRepo(user: User, repo: Repo) {
        _navigateRepoDetail.value = RepoData(user.name, repo.name)
    }
}