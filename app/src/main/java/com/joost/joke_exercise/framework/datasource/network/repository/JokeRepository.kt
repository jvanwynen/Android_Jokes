package com.joost.joke_exercise.framework.datasource.network.repository

import com.joost.joke_exercise.business.data.network.common.JokeResult
import com.joost.joke_exercise.business.domain.model.Category
import com.joost.joke_exercise.business.domain.model.Joke
import com.joost.joke_exercise.framework.datasource.cache.localstorage.FilterPreferences
import kotlinx.coroutines.flow.Flow

interface JokeRepository {

    suspend fun getOnlineJoke(category: String, flags: List<String>, type: String): Joke?

    fun getJokesFromDatabase(
        query: String,
        filterPreferences: FilterPreferences
    ): Flow<List<Joke>>

    suspend fun deleteJokeFromDatabase(joke: Joke)

    suspend fun deleteAllJokesFromDatabase()

    suspend fun insertJokeInDatabase(joke: Joke)

    suspend fun deleteAllNonFavoriteJokesFromDatabase()

    suspend fun updateJoke(joke: Joke)

    fun getAllCategories(): Flow<List<Category>>

}