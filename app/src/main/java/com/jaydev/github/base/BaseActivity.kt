package com.jaydev.github.base

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.jaydev.github.model.PopupMessage

open class BaseActivity : AppCompatActivity() {

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

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showAlertDialog(popup: PopupMessage) {
        AlertDialog.Builder(this)
            .setTitle(popup.title)
            .setMessage(popup.message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    protected fun showRetryDialog(retry: Retry) {
        AlertDialog.Builder(this)
            .setTitle(retry.message.title)
            .setMessage(retry.message.message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                retry.action?.invoke()
                dialog.dismiss()
            }.show()
    }
}