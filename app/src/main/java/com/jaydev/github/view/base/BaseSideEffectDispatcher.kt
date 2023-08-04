package com.jaydev.github.view.base

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.LifecycleOwner

val LocalBaseSideEffectDispatcher = staticCompositionLocalOf<BaseSideEffectDispatcher> {
    error("No SideEffectDispatcher provided")
}

interface BaseSideEffectDispatcher {
    @Composable
    fun dispatchBaseSideEffect(
        sideEffect: UiSideEffect,
        loading: @Composable (Boolean) -> Unit
    ): Boolean
}

class BaseSideEffectDispatcherImpl(
    private val context: Context,
    private val onBackPressedDispatcher: OnBackPressedDispatcher
) : BaseSideEffectDispatcher {

    fun addBackPressedCallback(owner: LifecycleOwner, callback: OnBackPressedCallback) {
        onBackPressedDispatcher.addCallback(owner, callback)
    }

    @Composable
    override fun dispatchBaseSideEffect(
        sideEffect: UiSideEffect,
        loading: @Composable (Boolean) -> Unit
    ): Boolean {
        when (sideEffect) {
            is BaseSideEffect.NavigateToBack -> {
                sideEffect.action?.invoke()
                onBackPressedDispatcher.onBackPressed()
            }

            is BaseSideEffect.ShowDialog -> {
                if (sideEffect.action != null) {
                    showActionDialog(sideEffect.title, sideEffect.message, sideEffect.action)
                } else {
                    showAlertDialog(sideEffect.title, sideEffect.message)
                }
            }

            is BaseSideEffect.ShowToast -> {
                showToast(sideEffect.message)
            }

            is BaseSideEffect.Loading -> {
                // to do show loading progress here or composable
                loading.invoke(sideEffect.isVisible)
            }

            else -> {
                return false
            }
        }
        return true
    }

    protected open fun showActionDialog(
        title: String?,
        message: CharSequence?,
        action: Invokable
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                action.invoke()
                dialog.dismiss()
            }.show()
    }

    protected open fun showAlertDialog(title: String?, message: CharSequence?) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    protected fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}