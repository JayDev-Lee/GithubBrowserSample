package com.jaydev.github.view.result

import android.os.Parcelable
import com.jaydev.github.model.SearchListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResultState(
    val lists: List<SearchListItem> = emptyList()
) : Parcelable