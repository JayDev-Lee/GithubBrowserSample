package com.jaydev.github.domain.entity

sealed interface NetError {
    object Network : NetError
    object Timeout : NetError
    data class BadRequest(val code: Int, val message: String) : NetError
    data class InternalServer(val code: Int, val message: String) : NetError
    class Unknown(val throwable: Throwable) : NetError
}
