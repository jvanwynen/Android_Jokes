package com.joost.joke_exercise.business.data.network.implementation

import com.joost.joke_exercise.business.data.network.abstraction.JokeNetworkDataSource
import com.joost.joke_exercise.business.data.network.common.JokeResult
import com.joost.joke_exercise.business.domain.model.Joke
import com.joost.joke_exercise.framework.datasource.network.abstraction.JokeRemoteService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeNetworkDataSourceImpl @Inject constructor(
    private val jokeService: JokeRemoteService
) : JokeNetworkDataSource {
    override suspend fun getJoke(
        category: String,
        type: String,
        flags: List<String>
    ): JokeResult<Joke?> =
        jokeService.getJoke(category, type, flags)

}