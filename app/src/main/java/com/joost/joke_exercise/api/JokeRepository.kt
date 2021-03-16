package com.joost.joke_exercise.api

import com.joost.joke_exercise.localstorage.FilterPreferences
import com.joost.joke_exercise.localstorage.JokeDAO
import com.joost.joke_exercise.models.Joke
import com.joost.joke_exercise.models.JokeApiResponse
import com.joost.joke_exercise.ui.home.JokeViewModel
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepository @Inject constructor(
    private val jokeApi: JokeApi,
    private val jokeDao: JokeDAO
) {

    suspend fun getOnlineJoke(category: String, flags: List<String>, type: String): JokeApiResponse? {
        return try {
            val response = jokeApi.getJoke(category, type, flags)
            if (response.isSuccessful) {
                response.body()
            } else {
                JokeApiResponse(
                    joke = response.message(),
                )
            }
        } catch (exc: IOException){
            JokeApiResponse(
                joke = "No connection"
            )
        }
    }

    fun getJokesFromDatabase(
        query: String,
        filterPreferences: FilterPreferences
    ): Flow<List<Joke>> {
        val selectedCategories = SelectedCategories(
            filterPreferences.showProgramming,
            filterPreferences.showDark,
            filterPreferences.showMisc,
            filterPreferences.showSpooky,
            filterPreferences.showChristmas,
            filterPreferences.showPun
        )
        return jokeDao.getJokes(
            query,
            filterPreferences.sortOrder,
            filterPreferences.hideNonFavorite,
            selectedCategories.toString().split(",")
        )
    }

    suspend fun deleteJokeFromDatabase(joke: Joke) {
        jokeDao.delete(joke)
    }

    suspend fun deleteAllJokesFromDatabase() {
        jokeDao.deleteAll()
    }

    suspend fun insertJokeInDatabase(joke: Joke) {
        jokeDao.insert(joke)
    }

    suspend fun deleteAllNonFavoriteJokesFromDatabase() {
        jokeDao.deleteAllNonFavorite()
    }

    suspend fun updateJoke(joke: Joke) {
        jokeDao.update(joke)
    }

    fun getAllCategories() = jokeDao.getCategories()

    data class SelectedCategories(
        var prog: Boolean = false,
        var dark: Boolean = false,
        var misc: Boolean = false,
        var spooky: Boolean = false,
        var christ: Boolean = false,
        var pun: Boolean = false
    ) {

        override fun toString(): String {
            var finalString = ""
            if (prog) {
                finalString += "Programming,"
            }
            if (dark) {
                finalString += "Dark,"
            }
            if (misc) {
                finalString += "Misc,"
            }
            if (pun) {
                finalString += "Pun,"
            }
            if (christ) {
                finalString += "Christmas,"
            }
            if (spooky) {
                finalString += "Spooky,"
            }
            finalString = finalString.removeSuffix(",")
            return finalString
        }
    }
}