package com.jaydev.github.view.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User
import com.jaydev.github.domain.interactor.usecase.GetUserDataUseCase
import com.jaydev.github.model.AlertUIModel
import com.jaydev.github.model.RepoData
import com.jaydev.github.model.SearchListItem
import com.jaydev.github.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    handle: SavedStateHandle,
    getUserData: GetUserDataUseCase
) : BaseViewModel() {
    private val _refreshListData = MutableStateFlow<List<SearchListItem>>(emptyList())
    val refreshListData = _refreshListData.asStateFlow()

    private val _navigateProfile = MutableSharedFlow<String>()
    val navigateProfile = _navigateProfile.asSharedFlow()

    private val _navigateRepoDetail = MutableSharedFlow<RepoData>()
    val navigateRepoDetail = _navigateRepoDetail.asSharedFlow()

    init {
        val userName = handle.get<String>("userName") ?: ""

        getUserData(GetUserDataUseCase.Params(userName))
            .onSuccess {
                val list = mutableListOf<SearchListItem>()
                list.add(SearchListItem.Header(it.first))
                list.addAll(
                    it.second.map { repo ->
                        SearchListItem.RepoItem(repo)
                    }
                )
                _refreshListData.value = list
            }.onFailure {
                showAlertDialog(AlertUIModel.Dialog("통신 실패", it.message))
            }.commonErrorHandler()
            .load()
    }


    fun onClickUser(user: User) {
        viewModelScope.launch {
            _navigateProfile.emit(user.name)
        }
    }

    fun onClickRepo(repo: Repo) {
        viewModelScope.launch {
            val header =
                refreshListData.value.firstOrNull() as? SearchListItem.Header ?: return@launch
            _navigateRepoDetail.emit(RepoData(header.user.name, repo.name))
        }
    }
}