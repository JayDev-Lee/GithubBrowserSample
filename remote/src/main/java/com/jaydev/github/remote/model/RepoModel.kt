package com.jaydev.github.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RepoModel(
    val name: String,
    val description: String?,
    val stargazers_count: String
)
