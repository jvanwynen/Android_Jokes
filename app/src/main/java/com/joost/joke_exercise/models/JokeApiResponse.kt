package com.joost.joke_exercise.models

data class JokeApiResponse(
    val joke: String?,
    val setup: String? = "",
    val delivery: String? = "",
    val category: String
)
