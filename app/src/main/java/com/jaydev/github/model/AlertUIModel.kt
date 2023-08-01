package com.jaydev.github.model

sealed class AlertUIModel {
	data class Dialog(
		val title: String? = null,
		val message: CharSequence,
		val positiveButton: String? = null,
		val negativeButton: String? = null,
		val isButtonRed: Boolean = false
	) : AlertUIModel()

	data class Toast(
		val message: String
	) : AlertUIModel()
}
