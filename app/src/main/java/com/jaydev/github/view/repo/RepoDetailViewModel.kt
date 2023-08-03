package com.jaydev.github.view.repo

import androidx.lifecycle.SavedStateHandle
import com.jaydev.github.domain.interactor.usecase.GetRepoDetailUseCase
import com.jaydev.github.model.ForkItem
import com.jaydev.github.model.UserItem
import com.jaydev.github.view.base.BaseSideEffect
import com.jaydev.github.view.base.BaseViewModel
import com.jaydev.github.view.base.UiSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    handle: SavedStateHandle,
    getRepoDetail: GetRepoDetailUseCase
) : BaseViewModel() {
    private val host =
        object : ContainerHost<RepoDetailState, UiSideEffect> {
            override val container =
                container<RepoDetailState, UiSideEffect>(RepoDetailState(), handle)
        }

    val state = host.container.stateFlow

    val sideEffectFlow = host.container.sideEffectFlow

    init {
        val userName = handle.get<String>("userName") ?: ""
        val repoName = handle.get<String>("repoName") ?: ""

        getRepoDetail(GetRepoDetailUseCase.Params(userName, repoName))
            .onSuccess {
                host.intent {
                    reduce {
                        state.copy(
                            title = userName,
                            description = it.first.description,
                            starCount = it.first.starCount,
                            repoName = it.first.name,
                            forks = it.second.map {
                                val user = it.owner
                                ForkItem(
                                    it.name,
                                    it.fullName,
                                    UserItem(user.name, user.profileImageUrl)
                                )
                            }
                        )
                    }
                }
            }.onFailure {
                host.intent {
                    postSideEffect(BaseSideEffect.ShowDialog("Error", it.message))
                }
            }.commonErrorHandler(host)
            .call()
    }
}