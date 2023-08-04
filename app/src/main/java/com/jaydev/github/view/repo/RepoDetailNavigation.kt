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

package com.jaydev.github.view.repo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jaydev.github.view.result.USER_NAME_ARG


const val REPO_NAME_ARG = "repoName"
const val repoDetailNavigationRoute = "repo_detail_route"

fun NavController.navigateToRepoDetail(
    userName: String,
    repoName: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$repoDetailNavigationRoute/$userName/$repoName", navOptions)
}

fun NavGraphBuilder.repoDetail() {
    composable(
        route = "$repoDetailNavigationRoute/{$USER_NAME_ARG}/{$REPO_NAME_ARG}",
        arguments = listOf(
            navArgument(USER_NAME_ARG) { type = NavType.StringType },
            navArgument(REPO_NAME_ARG) { type = NavType.StringType }
        ),
    ) {
        RepoDetailScreen()
    }
}
