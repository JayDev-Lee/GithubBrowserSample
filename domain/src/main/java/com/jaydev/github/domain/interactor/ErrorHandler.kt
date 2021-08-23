package com.jaydev.github.domain.interactor

import com.jaydev.github.domain.entity.NetError

interface ErrorHandler {
    fun getError(throwable: Throwable): NetError
}
