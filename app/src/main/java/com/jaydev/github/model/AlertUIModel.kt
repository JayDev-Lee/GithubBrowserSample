package com.jaydev.github.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AlertUIModel {
	data class Dialog(
		val title: String? = null,
		val message: CharSequence,
		val positiveButton: String? = null,
		val negativeButton: String? = null,
		val isButtonRed: Boolean = false
	) : AlertUIModel()

	@Parcelize
	data class Snackbar(
		val message: String,
	) : AlertUIModel(), Parcelable

	data class Toast(
		val message: String
	) : AlertUIModel()
}
