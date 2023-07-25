package com.jaydev.github.ui.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.jaydev.github.base.BaseActivity
import com.jaydev.github.common.KeyboardFocusManager
import com.jaydev.github.common.clicks
import com.jaydev.github.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchActivity : BaseActivity() {
    private var keyboardFocusManager: KeyboardFocusManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI(binding)
    }

    override fun onResume() {
        super.onResume()
        keyboardFocusManager?.findCorrectFocus()
    }

    private fun setupUI(binding: ActivitySearchBinding) {
        keyboardFocusManager = KeyboardFocusManager(lifecycleScope)
        keyboardFocusManager?.registerViews(binding.editText)
        setSupportActionBar(binding.toolbar)

        binding.searchButton.clicks()
            .onEach {
                val search = binding.editText.text.toString()
                val uri = Uri.parse("githubbrowser://repos/$search")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }.launchIn(lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardFocusManager?.destroy()
        keyboardFocusManager = null
    }
}