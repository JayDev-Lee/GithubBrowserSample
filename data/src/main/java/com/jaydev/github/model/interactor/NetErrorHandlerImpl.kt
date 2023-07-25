package com.jaydev.github.model.interactor

import com.jaydev.github.domain.entity.NetError
import com.jaydev.github.domain.interactor.ErrorHandler
import com.jaydev.github.domain.interactor.NetworkConnectException
import kotlinx.serialization.Serializable
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
                    in 500..599 -> {
                        val response = cause.response()
                        val (code, message) = extractErrorMessage(response)
                        NetError.InternalServer(code, message)
                    }

                    in 400..499 -> {
                        val response = cause.response()
                        val (code, message) = extractErrorMessage(response)
                        NetError.BadRequest(code, message)
                    }

                    else -> NetError.Unknown(cause)
                }
            }

            is NetworkConnectException, is IOException -> NetError.Network
            else -> NetError.Unknown(cause)
        }
    }

    private fun extractErrorMessage(response: Response<*>?): ErrorResponse {
        return try {
            val converter = retrofit.responseBodyConverter<ErrorResponse>(
                ErrorResponse::class.java,
                arrayOfNulls(0)
            )
            val netResponse = converter.convert(response?.errorBody()!!)
            return ErrorResponse(
                response.code(),
                netResponse?.message ?: response.errorBody()?.string().orEmpty(),
                netResponse?.documentation_url.orEmpty()
            )
        } catch (e: Exception) {
            ErrorResponse(
                response?.code()!!,
                response.errorBody()?.string().orEmpty(),
                ""
            )
        }
    }

    @Serializable
    private data class ErrorResponse(
        val code: Int,
        val message: String,
        val documentation_url: String
    )
}


