package com.beetlestance.smartcaller.domain

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

abstract class UseCase<in P> {
    operator fun invoke(params: P): Flow<Status> {
        return flow {
            emit(Status.Started)
            doWork(params)
            emit(Status.Success)
        }.catch { throwable ->
            emit(Status.Error(throwable))
        }
    }

    // Will only be used when we know the result will be returned without errors
    suspend fun executeSync(params: P): Unit = doWork(params)

    protected abstract suspend fun doWork(params: P)
}

abstract class ResultUseCase<in P, out R> {
    operator fun invoke(params: P): Flow<R> {
        return flow { emit(doWork(params)) }
    }

    protected abstract suspend fun doWork(params: P): R
}

abstract class ObserveUseCase<P : Any, T> {
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    var currentParam: P? = null

    operator fun invoke(params: P) {
        currentParam = params
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>

    fun observe(): Flow<T> = paramState.flatMapLatest { createObservable(it) }
}

abstract class PagingUseCase<P : PagingUseCase.Parameters<T>, T : Any> :
    ObserveUseCase<P, PagingData<T>>() {

    interface Parameters<T : Any> {
        val pagingConfig: PagingConfig
    }
}


operator fun <T> ResultUseCase<Unit, T>.invoke(): Flow<T> = invoke(Unit)
operator fun <T> ObserveUseCase<Unit, T>.invoke(): Unit = invoke(Unit)
