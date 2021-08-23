package com.jaydev.github.ui.repo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jaydev.github.base.BaseActivity
import com.jaydev.github.common.VerticalSpaceItemDecoration
import com.jaydev.github.databinding.ActivityRepoDetailBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class RepoDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRepoDetailBinding.inflate(layoutInflater)
        val viewModel = getViewModel<RepoDetailViewModel> {
            parametersOf(
                intent.getStringExtra(EXTRA_USER_NAME),
                intent.getStringExtra(EXTRA_REPO_NAME)
            )
        }
        setContentView(binding.root)


        setupUI(binding)
        subscribeUI(binding, viewModel)
    }

    private fun setupUI(binding: ActivityRepoDetailBinding) {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
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
        }
    }


    companion object {
        private const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
        private const val EXTRA_REPO_NAME = "EXTRA_REPO_NAME"

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