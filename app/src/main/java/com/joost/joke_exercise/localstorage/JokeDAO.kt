package com.joost.joke_exercise.localstorage

import androidx.room.*
import com.joost.joke_exercise.models.Category
import com.joost.joke_exercise.models.Joke
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDAO {

    fun getJokes(query: String, sortOrder: SortOrder, hideNonFavorite: Boolean): Flow<List<Joke>> =
        when(sortOrder){
            SortOrder.BY_DATE -> getJokesSortedByDate(query, hideNonFavorite)
            SortOrder.BY_NAME -> getJokesSortedByName(query, hideNonFavorite)
        }

    @Query("SELECT * FROM joke WHERE (favorite = :hideNonFavorite OR favorite = 1) AND jokeText LIKE '%' || :searchQuery ||  '%' ORDER BY favorite DESC, UPPER(jokeText) ")
    fun getJokesSortedByName(searchQuery: String, hideNonFavorite : Boolean): Flow<List<Joke>>

    @Query("SELECT * FROM joke WHERE (favorite = :hideNonFavorite OR favorite = 1) AND jokeText LIKE '%' || :searchQuery || '%' ORDER BY favorite DESC, created")
    fun getJokesSortedByDate(searchQuery: String, hideNonFavorite : Boolean): Flow<List<Joke>>

    @Insert
    suspend fun insert(joke: Joke)

    @Update
    suspend fun update(joke: Joke)

    @Delete
    suspend fun delete(joke: Joke)

    @Query("DELETE FROM joke")
    suspend fun deleteAll()

    @Query("DELETE FROM joke WHERE favorite = 0")
    suspend fun deleteAllNonFavorite()

    @Insert
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT category FROM joke")
    fun getCategoryOfJoke(): Flow<List<Category>>

//    @Transaction
//    @Query("SELECT * FROM category WHERE category = :category")
//    suspend fun getByCategoryId(category: String): Flow<List<CategoryWithJokes>>

}