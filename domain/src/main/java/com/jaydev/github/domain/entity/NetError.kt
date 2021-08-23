package com.jaydev.github.domain.entity

sealed class NetError {
    object Network : NetError()
    object Timeout : NetError()
    data class BadRequest(val code :Int, val message: String) : NetError()
    object InternalServer : NetError()
    class Unknown(val throwable: Throwable) : NetError()
}
