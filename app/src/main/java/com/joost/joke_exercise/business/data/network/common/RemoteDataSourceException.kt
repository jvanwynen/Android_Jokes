package com.joost.joke_exercise.business.data.network.common

sealed class RemoteDataSourceException(
    val messageResource: Any?
) : RuntimeException() {

    class Server(messageResource: Any?) : RemoteDataSourceException(messageResource)
}