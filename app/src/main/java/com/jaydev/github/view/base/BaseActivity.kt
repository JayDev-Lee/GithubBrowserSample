package com.jaydev.github.view.base

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback

open class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    protected fun dispatchBaseSideEffect(
        sideEffect: UiSideEffect,
        loading: (Boolean) -> Unit = {}
    ): Boolean {
        when (sideEffect) {
            is BaseSideEffect.NavigateToBack -> {
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

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finishAfterTransition()
        }
    }

    protected open fun showActionDialog(
        title: String?,
        message: CharSequence?,
        action: Invokable
    ) {
        AlertDialog.Builder(this)
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
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}