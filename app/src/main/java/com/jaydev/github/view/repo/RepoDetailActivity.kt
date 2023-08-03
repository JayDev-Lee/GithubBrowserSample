package com.jaydev.github.view.repo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jaydev.github.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RepoDetailActivity : BaseActivity() {
    private val viewModel by viewModels<RepoDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RepoDetailScreen(viewModel = viewModel)
        }

        subscribeUI(viewModel)
    }

    private fun subscribeUI(viewModel: RepoDetailViewModel) {
        with(viewModel) {
            sideEffectFlow
                .flowWithLifecycle(lifecycle)
                .onEach {
                    dispatchBaseSideEffect(it)
                }.launchIn(lifecycleScope)
        }
    }


    companion object {
        private const val EXTRA_USER_NAME = "userName"
        private const val EXTRA_REPO_NAME = "repoName"

        fun start(context: Context, userName: String, repoName: String) {
            context.startActivity(
                Intent(context, RepoDetailActivity::class.java).apply {
                    putExtra(EXTRA_USER_NAME, userName)
                    putExtra(EXTRA_REPO_NAME, repoName)
                }
            )
        }
    }
}