package com.jaydev.github.common

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Dimension
import kotlinx.coroutines.Job

@Dimension(unit = Dimension.PX)
fun Int.toPx(): Int = (Resources.getSystem().displayMetrics.density * this).toInt()

@Dimension(unit = Dimension.PX)
fun Float.toPx() = (Resources.getSystem().displayMetrics.density * this).toInt()

@Dimension(unit = Dimension.DP)
fun Int.toDp(): Float = this / Resources.getSystem().displayMetrics.density


@Dimension(unit = Dimension.PX)
fun Context.dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int {
    return (resources.displayMetrics.density * dp).toInt()
}

@Dimension(unit = Dimension.PX)
fun Context.dpToPx(@Dimension(unit = Dimension.DP) dp: Float): Int {
    return (resources.displayMetrics.density * dp).toInt()
}

@Dimension(unit = Dimension.DP)
fun Context.pxToDp(@Dimension px: Int): Float {
    return px.toFloat() / resources.displayMetrics.density
}

inline fun <reified T : Any> T.ordinal(): Int {
    if (T::class.isSealed) {
        return T::class.java.classes.indexOfFirst { sub -> sub == javaClass }
    }

    val klass = if (T::class.isCompanion) {
        javaClass.declaringClass
    } else {
        javaClass
    }

    return klass.superclass?.classes?.indexOfFirst { it == klass } ?: -1
}

fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}

fun View.showKeyboard(isForced: Boolean = false) {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, if (isForced) InputMethodManager.SHOW_FORCED else InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}