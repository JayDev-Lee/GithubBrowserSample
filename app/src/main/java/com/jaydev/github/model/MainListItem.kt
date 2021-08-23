package com.jaydev.github.model

import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User

sealed class MainListItem {
    data class Header(val user: User) : MainListItem() {
        companion object
    }

    data class RepoItem(val repo: Repo) : MainListItem() {
        companion object
    }
}
