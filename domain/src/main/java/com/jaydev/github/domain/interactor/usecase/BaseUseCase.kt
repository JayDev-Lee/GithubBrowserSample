package com.jaydev.github.domain.interactor.usecase

import com.jaydev.github.domain.NetResult
import com.jaydev.github.domain.interactor.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

abstract class BaseUseCase<out Type : Any, in Params> {
    abstract fun run(params: Params): Type

    operator fun invoke(
        params: Params
    ) = run(params)

    protected fun <T> Flow<T>.toResult(errorHandler: ErrorHandler) = map {
        NetResult.Success(it) as NetResult<T>
    }.catch { cause ->
        emit(NetResult.Error(errorHandler.getError(cause)))
    }

    class None
}
