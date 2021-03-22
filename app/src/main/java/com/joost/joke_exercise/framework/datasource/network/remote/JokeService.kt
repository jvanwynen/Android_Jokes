package com.joost.joke_exercise.framework.datasource.network.remote

import com.joost.joke_exercise.framework.datasource.network.models.JokeApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface JokeService {

    companion object {
        const val BASE_URL = "https://v2.jokeapi.dev/"
    }

    @GET("joke/{category}")
    suspend fun getJoke(
        @Path("category") category: String,
        @Query("type") type: String,
        @Query("blacklistFlags") flag: List<String>,
    ): Response<JokeApiResponse>


}