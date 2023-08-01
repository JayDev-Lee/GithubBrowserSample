package com.jaydev.github.view.repo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.jaydev.github.domain.entity.Fork
import com.jaydev.github.ui.theme.GithubBrowserTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RepoDetailScreen(viewModel: RepoDetailViewModel) {
    GithubBrowserTheme {
        Scaffold(
            topBar = {

                val title = viewModel.title.collectAsState().value
                TopAppBar(
                    title = {
                        Text(text = title)
                    },
                    colors = TopAppBarDefaults
                        .topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            }
        ) { padding ->
            Card(
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    )
                    .padding(16.dp)
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    val (repoName, description, starCount, forks) = createRefs()

                    Text(
                        text = viewModel.repoName.collectAsState().value,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.constrainAs(repoName) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                    )

                    Text(
                        text = viewModel.description.collectAsState().value,
                        modifier = Modifier.constrainAs(description) {
                            top.linkTo(repoName.bottom, 8.dp)
                            start.linkTo(parent.start)
                        }
                    )

                    Text(
                        text = viewModel.starCount.collectAsState().value,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.constrainAs(starCount) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                    )

                    val lists = viewModel.refreshForks.collectAsState().value
                    LazyColumn(
                        modifier = Modifier.constrainAs(forks) {
                            top.linkTo(description.bottom, 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lists) { item ->
                            ForkItem(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ForkItem(item: Fork) {
    ConstraintLayout(modifier = Modifier.padding(16.dp)) {
        val (forkName, userName, profileImage) = createRefs()

        Text(
            text = item.fullName,
            modifier = Modifier
                .constrainAs(forkName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        AsyncImage(
            model = item.owner.profileImageUrl,
            contentDescription = "User Profile Image",
            modifier = Modifier
                .size(32.dp)
                .constrainAs(profileImage) {
                    top.linkTo(forkName.bottom)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = item.name,
            modifier = Modifier
                .constrainAs(userName) {
                    top.linkTo(forkName.bottom)
                    start.linkTo(profileImage.end)
                }
        )
    }
}