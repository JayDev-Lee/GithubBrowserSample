package com.jaydev.github.ui.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jaydev.github.base.BaseViewModel
import com.jaydev.github.domain.entity.Fork
import com.jaydev.github.domain.interactor.usecase.GetRepoDetailUseCase
import com.jaydev.github.model.PopupMessage
import kotlinx.coroutines.launch

class RepoDetailViewModel(
    userName: String,
    repoName: String,
    private val getRepoDetail: GetRepoDetailUseCase
) : BaseViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _starCount = MutableLiveData<String>()
    val starCount: LiveData<String> = _starCount

    private val _repoName = MutableLiveData<String>()
    val repoName: LiveData<String> = _repoName

    private val _refreshForks = MutableLiveData<List<Fork>?>()
    val refreshForks: LiveData<List<Fork>?> = _refreshForks

    init {
        viewModelScope.launch {
            getRepoDetail(GetRepoDetailUseCase.Params(userName, repoName))
                .onSuccess {
                    _title.value = userName
                    _repoName.value = it.first.name
                    _description.value = it.first.description
                    _starCount.value = it.first.starCount
                    _refreshForks.value = it.second
                }.onFailure {
                    showAlertDialog(PopupMessage("통신 실패", it.message))
                }.commonErrorHandler()
                .call()
        }
    }
}