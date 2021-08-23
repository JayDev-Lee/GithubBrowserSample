package com.jaydev.github.domain.interactor.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.jaydev.github.domain.NetResult
import com.jaydev.github.domain.interactor.ErrorHandler

abstract class BaseUseCase<out Type : Any, in Params> {
    abstract suspend fun run(params: Params): Type

    suspend operator fun invoke(
        params: Params
    ) = run(params)

    protected fun <T> Flow<T>.toResult(errorHandler: ErrorHandler) = map {
        NetResult.Success(it) as NetResult<T>
    }.catch { cause ->
        emit(NetResult.Error(errorHandler.getError(cause)))
    }

    class None
}
