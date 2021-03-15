package com.joost.joke_exercise.models

import com.joost.joke_exercise.ui.home.JokeViewModel

data class JokeApiResponse(
    val joke: String?,
    val setup: String,
    val delivery: String,
    val message: String? = "",
    val category: String
)
