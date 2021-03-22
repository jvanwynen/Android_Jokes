package com.joost.joke_exercise.framework.datasource.network.models

data class JokeApiResponse(
    val joke: String?,
    val setup: String? = "",
    val delivery: String? = "",
    val message: String? = "",
    val category: String? =""
)
