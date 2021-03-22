package com.joost.joke_exercise.business.data.network.common

sealed class JokeResult<out T> {
    data class Success<out T>(val data: T) : JokeResult<T>()
    data class Error(val exception: RemoteDataSourceException) : JokeResult<Nothing>()
}

inline fun <T : Any> JokeResult<T>.onSuccess(action: (T) -> Unit): JokeResult<T> {
    if (this is JokeResult.Success) action(data)
    return this
}

inline fun <T : Any> JokeResult<T>.onError(action: (RemoteDataSourceException) -> Unit): JokeResult<T> {
    if (this is JokeResult.Error) action(exception)
    return this
}