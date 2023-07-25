package com.jaydev.github.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.jaydev.github.base.BaseActivity
import com.jaydev.github.common.VerticalSpaceItemDecoration
import com.jaydev.github.databinding.ActivityMainBinding
import com.jaydev.github.ui.repo.RepoDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainViewModel>(
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
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI(binding)
        subscribeUI(binding, viewModel)
    }

    private fun setupUI(binding: ActivityMainBinding) {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.recyclerView.addItemDecoration(VerticalSpaceItemDecoration(8f))
    }

    private fun subscribeUI(binding: ActivityMainBinding, viewModel: MainViewModel) {
        with(viewModel) {
            title.onResult { binding.toolbar.title = it }

            refreshListData.onResult {
                binding.recyclerView.adapter = MainListAdapter(
                    it,
                    viewModel::onClickUser,
                    viewModel::onClickRepo
                )
            }

            navigateProfile.onResult {
                val uri = Uri.parse("githubbrowser://repos/$it")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }

            navigateRepoDetail.onResult {
                RepoDetailActivity.start(this@MainActivity, it.userName, it.repoName)
            }

            showToast.onEach {
                showToast(it)
            }.launchIn(lifecycleScope)

            showAlertDialog.onEach {
                showAlertDialog(it.title, it.message)
            }.launchIn(lifecycleScope)

            showActionDialog.onEach {
                showActionDialog(it.first.title, it.first.message, it.second)
            }.launchIn(lifecycleScope)

            showProgress.onResult {
                binding.progressLayout.isVisible = it
            }

            navigateToBack.onEach {
                onBackPressedDispatcher.onBackPressed()
            }.launchIn(lifecycleScope)
        }
    }
}