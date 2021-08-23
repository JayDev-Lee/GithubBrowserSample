package com.jaydev.github.remote.model

data class ForkModel(
    val name: String,
    val full_name: String,
    val owner: UserModel
)
