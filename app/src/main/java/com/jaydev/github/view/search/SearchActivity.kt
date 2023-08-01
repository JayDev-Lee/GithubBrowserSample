package com.jaydev.github.view.search

import android.os.Bundle
import androidx.activity.compose.setContent
import com.jaydev.github.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SearchScreen()
        }
    }
}