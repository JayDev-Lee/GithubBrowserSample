package com.jaydev.github.common

import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** For Fragment. **/
class KeyboardFocusManager(private val viewLifecycleScope: CoroutineScope) {
	private var focusableViews: MutableList<View>? = mutableListOf()
	private var keyboardJob: Job? = null

	private val focusListener = View.OnFocusChangeListener { view, hasFocus ->
		keyboardJob?.cancelIfActive()
		viewLifecycleScope.launch {
			keyboardJob = async(viewLifecycleScope.coroutineContext + Dispatchers.Main) {
				if (hasFocus) {
					delay(300)
					view?.showKeyboard()
				} else {
					view?.hideKeyboard()
					view?.clearFocus()
				}
			}
			keyboardJob?.join()
		}
	}

	fun add(view: View) {
		focusableViews?.add(view)
	}

	fun add(index: Int, view: View) {
		focusableViews?.add(index, view)
	}

	fun remove(view: View) {
		focusableViews?.remove(view)
	}

	fun registerViews(vararg views: View) {
		focusableViews = views.toMutableList()
		focusableViews?.forEach {
			it.onFocusChangeListener = focusListener
		}
	}

	fun unregisterViews() {
		focusableViews?.clear()
		focusableViews = null
	}

	fun destroy() {
		unregisterViews()
		keyboardJob?.cancelIfActive()
		keyboardJob = null
	}

	@Throws(IllegalAccessException::class)
	fun findCorrectFocus() {
		var notFoundRequestFocus = true
		focusableViews?.reversed()?.forEach {
			val text = when (it) {
				is EditText -> it.text
				else -> throw IllegalAccessException("registered wrong view type.")
			}
			val canTakeFocus = it.isEnabled && it.isFocusable && it.isVisible
			if (canTakeFocus && text.isNullOrBlank()) {
				notFoundRequestFocus = false
				it.requestFocus()
			}
		}
		if (notFoundRequestFocus) {
			viewLifecycleScope.launch {
				delay(100)
				focusableViews?.forEach {
					it.hideKeyboard()
					it.clearFocus()
				}
			}
		}
	}
}
