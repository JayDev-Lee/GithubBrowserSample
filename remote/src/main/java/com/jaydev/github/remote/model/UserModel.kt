package com.jaydev.github.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val login: String,
    val avatar_url: String,
)
