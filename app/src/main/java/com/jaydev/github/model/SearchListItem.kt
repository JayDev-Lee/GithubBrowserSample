package com.jaydev.github.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface SearchListItem : Parcelable {
    @Parcelize
    data class Header(
        val userName: String,
        val profileImageUrl: String
    ) : SearchListItem, Parcelable

    @Parcelize
    data class RepoItem(
        val name: String,
        val description: String,
        val starCount: String
    ) : SearchListItem, Parcelable
}
