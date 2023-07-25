package com.jaydev.github.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ForkModel(
    val name: String,
    val full_name: String,
    val owner: UserModel
)
