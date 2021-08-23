package com.jaydev.github.di.loader

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ResourcesProvider {
    fun getDrawable(@DrawableRes res: Int): Drawable?

    @ColorInt
    fun getColor(@ColorRes res: Int): Int
    fun getString(@StringRes res: Int): String
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String

    fun loadImage(url: String)
    suspend fun getDrawable(url: String?): Drawable?
}

class ResourcesProviderImpl(private val context: Context) : ResourcesProvider {
    override fun getDrawable(res: Int) = context.getDrawable(res)

    override fun getColor(res: Int) = context.resources.getColor(res)

    override fun getString(res: Int) = context.getString(res)

    override fun getString(@StringRes resId: Int, vararg formatArgs: Any) =
        context.getString(resId, *formatArgs)

    override fun loadImage(url: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getDrawable(url: String?): Drawable? {
        TODO("Not yet implemented")
    }
}