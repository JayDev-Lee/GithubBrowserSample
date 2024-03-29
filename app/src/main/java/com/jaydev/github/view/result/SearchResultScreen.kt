package com.jaydev.github.view.result

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User
import com.jaydev.github.model.SearchListItem
import com.jaydev.github.ui.theme.GithubBrowserTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchResultScreen(viewModel: SearchResultViewModel) {
    GithubBrowserTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "SearchList")
                    },
                    colors = TopAppBarDefaults
                        .topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            },

            ) { padding ->
            val lists = viewModel.refreshListData.collectAsState().value
            val isLoading = viewModel.loading.collectAsState().value

            Crossfade(targetState = isLoading, label = "loading") {
                if (it) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(padding)
                            .consumeWindowInsets(padding)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Horizontal,
                                ),
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lists) { item ->
                            when (item) {
                                is SearchListItem.Header -> UserInfoItem(
                                    item,
                                    viewModel::onClickUser
                                )

                                is SearchListItem.RepoItem -> UserRepoItem(
                                    item,
                                    viewModel::onClickRepo
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserInfoItem(
    item: SearchListItem.Header,
    onClick: (User) -> Unit
) {
    Row(Modifier.clickable {
        onClick.invoke(item.user)
    }) {
        AsyncImage(
            model = item.user.profileImageUrl,
            contentDescription = "User Profile Image",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = item.user.name)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserRepoItem(
    item: SearchListItem.RepoItem,
    onClick: (Repo) -> Unit
) {
    Card(
        onClick = {
            onClick.invoke(item.repo)
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val (repoName, description, starCount) = createRefs()

            Text(
                text = item.repo.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(repoName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            Text(
                text = item.repo.description,
                modifier = Modifier.constrainAs(description) {
                    top.linkTo(repoName.bottom)
                    start.linkTo(parent.start)
                }
            )

            Text(
                text = item.repo.starCount,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.constrainAs(starCount) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}