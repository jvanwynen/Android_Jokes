package com.joost.joke_exercise.api

import com.joost.joke_exercise.models.Joke
import com.joost.joke_exercise.models.JokeApiResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepository @Inject constructor(
    private val jokeApi: JokeApi
) {

    suspend fun getOnlineJoke(category: String, flags: List<String>, type: String): JokeApiResponse? {
        val response =  jokeApi.getJoke(category, type, flags)
        return if (response.isSuccessful){
            response.body()
        } else {
            JokeApiResponse(joke = response.message(), category = "Programming")
        }
    }
}