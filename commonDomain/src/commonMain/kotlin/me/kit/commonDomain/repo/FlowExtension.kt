package me.kit.commonDomain.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import me.kit.commonDomain.TruckResult

fun <T : Any> resultFlow(data: T): Flow<TruckResult<T>> =
    flowOf<TruckResult<T>>(TruckResult.Success(data))
        .onStart { emit(TruckResult.Loading) }
        .catch { TruckResult.Error(message = it.message.orEmpty(), cause = it.cause) }