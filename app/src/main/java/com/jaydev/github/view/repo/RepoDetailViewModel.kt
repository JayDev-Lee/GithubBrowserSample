package com.jaydev.github.view.repo

import androidx.lifecycle.SavedStateHandle
import com.jaydev.github.domain.entity.Fork
import com.jaydev.github.domain.interactor.usecase.GetRepoDetailUseCase
import com.jaydev.github.model.AlertUIModel
import com.jaydev.github.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    handle: SavedStateHandle,
    getRepoDetail: GetRepoDetailUseCase
) : BaseViewModel() {


    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _starCount = MutableStateFlow("")
    val starCount = _starCount.asStateFlow()

    private val _repoName = MutableStateFlow("")
    val repoName = _repoName.asStateFlow()

    private val _refreshForks = MutableStateFlow<List<Fork>>(emptyList())
    val refreshForks = _refreshForks.asStateFlow()

    init {
        val userName = handle.get<String>("userName") ?: ""
        val repoName = handle.get<String>("repoName") ?: ""

        getRepoDetail(GetRepoDetailUseCase.Params(userName, repoName))
            .onSuccess {
                _title.value = userName
                _repoName.value = it.first.name
                _description.value = it.first.description
                _starCount.value = it.first.starCount
                _refreshForks.value = it.second
            }.onFailure {
                showAlertDialog(AlertUIModel.Dialog("통신 실패", it.message))
            }.commonErrorHandler()
            .call()
    }
}