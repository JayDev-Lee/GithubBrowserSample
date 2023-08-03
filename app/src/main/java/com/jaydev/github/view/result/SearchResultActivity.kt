package com.jaydev.github.view.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.jaydev.github.view.base.BaseActivity
import com.jaydev.github.view.repo.RepoDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchResultActivity : BaseActivity() {
    private val viewModel by viewModels<SearchResultViewModel>(
        extrasProducer = {
            val extras = MutableCreationExtras(defaultViewModelCreationExtras)
            intent?.data?.path?.substring(1)?.let { queryParamId ->
                extras[DEFAULT_ARGS_KEY] = bundleOf("userName" to queryParamId)
            }
            extras
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchResultScreen(viewModel = viewModel)
        }

        subscribeUI(viewModel)
    }

    private fun subscribeUI(viewModel: SearchResultViewModel) {
        with(viewModel) {
            sideEffectFlow
                .flowWithLifecycle(lifecycle)
                .onEach {
                    val result = dispatchBaseSideEffect(it)
                    if (!result) {
                        when (it) {
                            is SearchResultSideEffect.NavigateToProfile -> {
                                val uri = Uri.parse("githubbrowser://repos/${it.userName}")
                                startActivity(Intent(Intent.ACTION_VIEW, uri))
                            }

                            is SearchResultSideEffect.NavigateToRepoDetail -> {
                                RepoDetailActivity.start(
                                    this@SearchResultActivity,
                                    it.userName,
                                    it.repoName
                                )
                            }

                            else -> {
                                // no-op
                            }
                        }
                    }
                }.launchIn(lifecycleScope)
        }
    }

}