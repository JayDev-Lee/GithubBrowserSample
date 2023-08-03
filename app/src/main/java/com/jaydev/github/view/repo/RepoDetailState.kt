package com.jaydev.github.view.repo

import android.os.Parcelable
import com.jaydev.github.model.ForkItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepoDetailState(
    val title: String = "",
    val description: String = "",
    val starCount: String = "",
    val repoName: String = "",
    val forks: List<ForkItem> = emptyList()
) : Parcelable
