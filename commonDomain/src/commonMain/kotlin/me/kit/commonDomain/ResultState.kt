package me.kit.commonDomain

sealed class TruckResult<out T : Any> {
    object Loading : TruckResult<Nothing>()
    data class Success<out T : Any>(val value: T) : TruckResult<T>()
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : TruckResult<Nothing>()
}