package com.jaydev.github.domain.entity


data class Fork(
    val name: String,
    val fullName: String,
    val owner: User
)
