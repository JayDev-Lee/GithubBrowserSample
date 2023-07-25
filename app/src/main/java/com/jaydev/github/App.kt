package com.jaydev.github

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        PrettyFormatStrategy.newBuilder()
            .tag("JayDev")
            .build()
            .let { AndroidLogAdapter(it) }
            .also {
                Logger.addLogAdapter(it)
            }
    }
}