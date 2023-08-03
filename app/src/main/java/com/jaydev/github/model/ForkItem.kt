package com.jaydev.github.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForkItem(
    val name: String,
    val fullName: String,
    val owner: UserItem
) : Parcelable
