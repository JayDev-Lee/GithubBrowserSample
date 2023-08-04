package com.jaydev.github.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.jaydev.github.view.base.BaseSideEffectDispatcherImpl
import com.jaydev.github.view.base.LocalBaseSideEffectDispatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseSideEffectDispatcher = BaseSideEffectDispatcherImpl(this, onBackPressedDispatcher)
        setContent {
            CompositionLocalProvider(LocalBaseSideEffectDispatcher provides baseSideEffectDispatcher) {
                GitHubBrowserApp()
            }
        }
    }
}