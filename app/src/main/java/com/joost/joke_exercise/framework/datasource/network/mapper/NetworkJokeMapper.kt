package com.joost.joke_exercise.framework.datasource.network.mapper

import com.joost.joke_exercise.business.domain.model.Joke
import com.joost.joke_exercise.business.domain.util.ResponseMapper
import com.joost.joke_exercise.framework.datasource.network.models.JokeApiResponse

class NetworkJokeMapper() : ResponseMapper<JokeApiResponse, Joke> {

    override fun mapFromResponse(response: JokeApiResponse): Joke {
        val jokeText = if(!response.joke.isNullOrEmpty()) { //checks if it is a single or twoPart joke
            response.joke
        } else {
            response.setup ?: ""
        }
        return Joke(jokeText = jokeText,
            category = response.category ?: "",
            delivery = response.delivery ?: ""
        )
    }
}