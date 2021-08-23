package com.jaydev.github.model.interactor

import com.jaydev.github.domain.entity.NetError
import com.jaydev.github.domain.interactor.ErrorHandler
import com.jaydev.github.domain.interactor.NetworkConnectException
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException

class NetErrorHandlerImpl(private val retrofit: Retrofit) : ErrorHandler {
    override fun getError(cause: Throwable): NetError {
        return when (cause) {
            is SocketTimeoutException -> NetError.Timeout
            is HttpException -> {
                when (cause.code()) {
                    in 500..599 -> NetError.InternalServer
                    in 400..499 -> {
                        val code = cause.code()
                        val message = extractErrorMessage(cause.response())
                        NetError.BadRequest(code, message)
                    }
                    else -> NetError.Unknown(cause)
                }
            }
            is NetworkConnectException, is IOException -> NetError.Network
            else -> NetError.Unknown(cause)
        }
    }

    private fun extractErrorMessage(response: Response<*>?): String {
        val converter = retrofit.responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            arrayOfNulls(0)
        )
        val baseResponse = converter.convert(response?.errorBody()!!)
        return baseResponse?.message.orEmpty()
    }

    private data class ErrorResponse(val message: String, val documentation_url: String)
}


