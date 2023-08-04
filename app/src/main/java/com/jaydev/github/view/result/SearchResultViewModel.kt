package com.jaydev.github.view.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jaydev.github.domain.interactor.usecase.GetUserDataUseCase
import com.jaydev.github.model.SearchListItem
import com.jaydev.github.view.base.BaseSideEffect
import com.jaydev.github.view.base.BaseViewModel
import com.jaydev.github.view.base.UiSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    handle: SavedStateHandle,
    getUserData: GetUserDataUseCase
) : BaseViewModel() {
    private val host =
        object : ContainerHost<SearchResultState, UiSideEffect> {
            override val container =
                container<SearchResultState, UiSideEffect>(SearchResultState(), handle, {
                    repeatOnSubscribedStopTimeout = 0L
                })
        }

    val state = host.container.stateFlow

    val sideEffectFlow =
        host.container.sideEffectFlow.shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    init {
        val userName = handle.get<String>("userName") ?: ""

        getUserData(GetUserDataUseCase.Params(userName))
            .onSuccess {
                val list = mutableListOf<SearchListItem>()
                list.add(SearchListItem.Header(it.first.name, it.first.profileImageUrl))
                list.addAll(
                    it.second.map { repo ->
                        SearchListItem.RepoItem(repo.name, repo.description, repo.starCount)
                    }
                )
                host.intent {
                    reduce {
                        state.copy(lists = list)
                    }
                }
            }.onFailure {
                host.intent {
                    postSideEffect(BaseSideEffect.ShowDialog("API Failure", it.message))
                }
            }.commonErrorHandler(host)
            .load {
                host.intent {
                    postSideEffect(BaseSideEffect.Loading(it))
                }
            }
    }


    fun onClickUser(userName: String) = host.intent {
        postSideEffect(SearchResultSideEffect.NavigateToProfile(userName))
    }

    fun onClickRepo(repoName: String) = host.intent {
        val user = state.lists.firstOrNull() as? SearchListItem.Header ?: return@intent
        postSideEffect(SearchResultSideEffect.NavigateToRepoDetail(user.userName, repoName))
    }
}