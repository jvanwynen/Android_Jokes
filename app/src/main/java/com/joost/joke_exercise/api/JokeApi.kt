package com.joost.joke_exercise.api

import com.joost.joke_exercise.models.Joke
import retrofit2.http.GET
import retrofit2.http.Path


interface JokeApi {

    companion object {
        const val BASE_URL = "https://v2.jokeapi.dev/"
    }

    @GET("joke/{category}?type=single")
    suspend fun getJoke(
        @Path("category")category: String
    ): Joke


}