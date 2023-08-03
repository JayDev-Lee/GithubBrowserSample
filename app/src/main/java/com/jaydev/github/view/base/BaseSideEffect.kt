package com.jaydev.github.view.base

sealed interface UiSideEffect

open class BaseSideEffect : UiSideEffect {
    object NavigateToBack : BaseSideEffect()

    data class Loading(val isVisible: Boolean) : BaseSideEffect()

    data class ShowDialog(
        val title: String,
        val message: CharSequence,
        val positiveButton: String? = null,
        val negativeButton: String? = null,
        val action: Invokable? = null
    ) : BaseSideEffect()

    data class ShowToast(val message: String) : BaseSideEffect()
}