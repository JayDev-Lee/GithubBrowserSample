package com.jaydev.github.view.result

import com.jaydev.github.view.base.BaseSideEffect

sealed class SearchResultSideEffect : BaseSideEffect() {

    data class NavigateToProfile(val userName: String) : SearchResultSideEffect()

    data class NavigateToRepoDetail(
        val userName: String,
        val repoName: String
    ) : SearchResultSideEffect()
}