package com.joost.joke_exercise.api

import com.joost.joke_exercise.models.Joke
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepository @Inject constructor(
    private val jokeApi: JokeApi
) {

    suspend fun getOnlineJoke(path: String): Joke {
       return try {
            jokeApi.getJoke(path)
        } catch (exception: IOException){
            Joke(jokeText = "Something Went wrong with getting the Joke", category = "Programming")
        } catch (exception: HttpException){
            Joke(jokeText = "No Joke Found", category = "Programming")
        }
    }



}