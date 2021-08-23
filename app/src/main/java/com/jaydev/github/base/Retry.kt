package com.jaydev.github.base

import com.jaydev.github.model.PopupMessage

typealias RetryInvokable = () -> Unit

data class Retry(
    val action: RetryInvokable?,
    val message: PopupMessage
)
