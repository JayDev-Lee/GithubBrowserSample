/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaydev.github.view.result

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

const val USER_NAME_ARG = "userName"
const val searchResultNavigationRoute = "search_result_route"
private const val DEEP_LINK_URI_PATTERN = "githubbrowser://repos/{$USER_NAME_ARG}"

fun NavController.navigateToSearchResult(navOptions: NavOptions? = null) {
    this.navigate(searchResultNavigationRoute, navOptions)
}

fun NavGraphBuilder.searchResult(onClickRepoDetail: (userName: String, repoName: String) -> Unit) {
    composable(
        route = searchResultNavigationRoute,
        deepLinks = listOf(
            navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN }
        ),
        arguments = listOf(
            navArgument(USER_NAME_ARG) { type = NavType.StringType }
        ),
    ) {
        SearchResultScreen(onClickRepoDetail = onClickRepoDetail)
    }
}
