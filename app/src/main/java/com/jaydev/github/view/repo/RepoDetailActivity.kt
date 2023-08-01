package com.jaydev.github.view.repo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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

            showToast.onEach {
                showToast(it)
            }.launchIn(lifecycleScope)

            showAlertDialog.onEach {
                showAlertDialog(it.title, it.message)
            }.launchIn(lifecycleScope)

            showActionDialog.onEach {
                showActionDialog(it.first.title, it.first.message, it.second)
            }.launchIn(lifecycleScope)

            navigateToBack.onEach {
                onBackPressedDispatcher.onBackPressed()
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