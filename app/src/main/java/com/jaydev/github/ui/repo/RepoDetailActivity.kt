package com.jaydev.github.ui.repo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.jaydev.github.base.BaseActivity
import com.jaydev.github.common.VerticalSpaceItemDecoration
import com.jaydev.github.databinding.ActivityRepoDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RepoDetailActivity : BaseActivity() {
    private val viewModel by viewModels<RepoDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRepoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI(binding)
        subscribeUI(binding, viewModel)
    }

    private fun setupUI(binding: ActivityRepoDetailBinding) {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.recyclerView.adapter = RepoDetailAdapter()
        binding.recyclerView.addItemDecoration(VerticalSpaceItemDecoration(8f))
    }

    private fun subscribeUI(binding: ActivityRepoDetailBinding, viewModel: RepoDetailViewModel) {
        with(viewModel) {
            title.onResult { binding.toolbar.title = it }
            repoName.onResult { binding.nameTextView.text = it }
            starCount.onResult { binding.starCountTextView.text = it }
            description.onResult { binding.descriptionTextView.text = it }
            refreshForks.onSafeResult {
                (binding.recyclerView.adapter as? RepoDetailAdapter)?.updateList(it)
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