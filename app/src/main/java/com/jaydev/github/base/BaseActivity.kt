package com.jaydev.github.base

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finishAfterTransition()
        }
    }

    protected fun <T> LiveData<T>.onResult(action: (T) -> Unit) {
        observe(this@BaseActivity) { data ->
            action(data)
        }
    }

    protected fun <T> LiveData<T?>.onSafeResult(action: (T) -> Unit) {
        observe(this@BaseActivity) { data ->
            data?.let(action)
        }
    }

    protected open fun showActionDialog(
        title: String?,
        message: CharSequence?,
        action: InvokableAction
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

    protected open fun showSnackbar(message: String?) {
        message?.let { Snackbar.make(window.decorView, it, LENGTH_SHORT).show() }
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}