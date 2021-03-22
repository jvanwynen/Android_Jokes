package com.joost.joke_exercise.framework.datasource.network.implementation

import com.joost.joke_exercise.business.data.network.common.JokeResult
import com.joost.joke_exercise.business.data.network.common.RemoteDataSourceException
import com.joost.joke_exercise.business.domain.model.Joke
import com.joost.joke_exercise.framework.datasource.network.remote.JokeService
import com.joost.joke_exercise.framework.datasource.network.abstraction.JokeRemoteService
import com.joost.joke_exercise.framework.datasource.network.mapper.NetworkJokeMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRemoteServiceImpl @Inject constructor(
    private val jokeService: JokeService,
    private val networkJokeMapper: NetworkJokeMapper
) : JokeRemoteService {

     override suspend fun getJoke(category: String, type: String, flags: List<String>): JokeResult<Joke?> {
        return try {
            val response = jokeService.getJoke(category, type, flags)
            if (response.isSuccessful) {
                if(response.body() != null) {
                    JokeResult.Success(networkJokeMapper.mapFromResponse(response.body()!!))
                } else {
                    JokeResult.Error(RemoteDataSourceException.Server(""))
                }
            } else {
                JokeResult.Error(RemoteDataSourceException.Server(response.errorBody()))
            }
        } catch (exc: Exception){
            JokeResult.Error(RemoteDataSourceException.Server(exc))
        }
    }

}