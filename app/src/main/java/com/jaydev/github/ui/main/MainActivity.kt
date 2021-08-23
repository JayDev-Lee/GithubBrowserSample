package com.jaydev.github.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.isVisible
import com.jaydev.github.base.BaseActivity
import com.jaydev.github.common.VerticalSpaceItemDecoration
import com.jaydev.github.databinding.ActivityMainBinding
import com.jaydev.github.ui.repo.RepoDetailActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = getViewModel<MainViewModel> {
            parametersOf(intent.data?.path?.substring(1))
        }

        setupUI(binding)
        subscribeUI(binding, viewModel)
    }

    private fun setupUI(binding: ActivityMainBinding) {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
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

            showToast.onResult {
                showToast(it)
            }

            showAlertDialog.onResult {
                showAlertDialog(it)
            }

            showRetryDialog.onResult {
                showRetryDialog(it)
            }

            showProgress.onResult {
                binding.progressLayout.isVisible = it
            }
        }
    }
}