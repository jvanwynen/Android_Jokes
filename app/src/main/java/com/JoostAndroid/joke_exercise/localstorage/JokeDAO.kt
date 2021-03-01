package com.JoostAndroid.joke_exercise.localstorage

import androidx.room.*
import com.JoostAndroid.joke_exercise.models.Joke
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDAO {

    @Query("SELECT * FROM joke")
    fun getJokes() : Flow<List<Joke>>

    @Insert
    suspend fun insert(joke: Joke)

    @Update
    suspend fun update(joke: Joke)

    @Delete
    suspend fun delete(joke: Joke)

    @Query("DELETE FROM joke")
    suspend fun deleteAll()


}