package com.jaydev.github.model

import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User

sealed class SearchListItem {
    data class Header(val user: User) : SearchListItem() {
        companion object
    }

    data class RepoItem(val repo: Repo) : SearchListItem() {
        companion object
    }
}
