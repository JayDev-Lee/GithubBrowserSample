package com.jaydev.github.model

sealed class AlertUIModel {
	data class Dialog(
		val title: String? = null,
		val message: CharSequence,
		val positiveButton: String? = null,
		val negativeButton: String? = null,
	) : AlertUIModel()

	data class Toast(
		val message: String
	) : AlertUIModel()
}
