package com.jaydev.github.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItem(
    val userName: String,
    val profileImageUrl: String
) : Parcelable
