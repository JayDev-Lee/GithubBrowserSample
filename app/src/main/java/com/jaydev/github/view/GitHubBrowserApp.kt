package com.jaydev.github.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jaydev.github.view.repo.navigateToRepoDetail
import com.jaydev.github.view.repo.repoDetail
import com.jaydev.github.view.result.searchResult
import com.jaydev.github.view.search.searchNavigationRoute
import com.jaydev.github.view.search.searchScreen

@Composable
fun GitHubBrowserApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = searchNavigationRoute,
        modifier = Modifier.fillMaxSize()
    ) {
        searchScreen()
        searchResult(
            onClickRepoDetail = navController::navigateToRepoDetail
        )
        repoDetail()
    }
}