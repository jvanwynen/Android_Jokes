package com.joost.joke_exercise.framework.datasource.network.repository

import com.joost.joke_exercise.business.data.network.common.JokeResult
import com.joost.joke_exercise.business.domain.model.Joke
import com.joost.joke_exercise.framework.datasource.cache.abstraction.JokeDaoService
import com.joost.joke_exercise.framework.datasource.cache.localstorage.FilterPreferences
import com.joost.joke_exercise.framework.datasource.network.abstraction.JokeRemoteService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepositoryImpl @Inject constructor(
    private val jokeRemoteService: JokeRemoteService,
    private val jokeDaoService: JokeDaoService
) : JokeRepository {

    override suspend fun getOnlineJoke(category: String, flags: List<String>, type: String): Joke? {
        return jokeRemoteService.getJoke(category, type, flags).run {
            when (this) {
                is JokeResult.Success -> {
                    this.data
                }
                is JokeResult.Error -> {
                    Joke(jokeText = "No joke found", category = "")
                }
            }
        }
    }

    override fun getJokesFromDatabase(
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
        return jokeDaoService.getJokes(
            query,
            filterPreferences.sortOrder,
            filterPreferences.hideNonFavorite,
            selectedCategories.toString().split(",")
        )
    }

    override suspend fun deleteJokeFromDatabase(joke: Joke) {
        jokeDaoService.delete(joke)
    }

    override suspend fun deleteAllJokesFromDatabase() {
        jokeDaoService.deleteAll()
    }

    override suspend fun insertJokeInDatabase(joke: Joke) {
        jokeDaoService.insert(joke)
    }

    override suspend fun deleteAllNonFavoriteJokesFromDatabase() {
        jokeDaoService.deleteAllNonFavorite()
    }

    override suspend fun updateJoke(joke: Joke) {
        jokeDaoService.update(joke)
    }

    override fun getAllCategories() = jokeDaoService.getCategories()

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