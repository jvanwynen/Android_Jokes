package com.joost.joke_exercise.framework.datasource.cache.localstorage

import androidx.room.*
import com.joost.joke_exercise.framework.datasource.cache.models.CategoryEntity
import com.joost.joke_exercise.framework.datasource.cache.models.JokeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDAO {

    fun getJokes(query: String,
                 sortOrder: SortOrder,
                 hideNonFavorite: Boolean,
                 selectedCategory: List<String>
    ): Flow<List<JokeEntity>> =
        when(sortOrder){
            SortOrder.BY_DATE -> getJokesSortedByDate(query, hideNonFavorite, selectedCategory)
            SortOrder.BY_NAME -> getJokesSortedByName(query, hideNonFavorite, selectedCategory)
        }

    @Query("SELECT * FROM joke WHERE (favorite = :hideNonFavorite OR favorite = 1) AND category IN (:selectedCategory) AND jokeText LIKE '%' || :searchQuery ||  '%' ORDER BY favorite DESC, UPPER(jokeText) ")
    fun getJokesSortedByName(searchQuery: String, hideNonFavorite : Boolean, selectedCategory: List<String>): Flow<List<JokeEntity>>

    @Query("SELECT * FROM joke WHERE (favorite = :hideNonFavorite OR favorite = 1) AND category IN (:selectedCategory) AND jokeText LIKE '%' || :searchQuery || '%' ORDER BY favorite DESC, created")
    fun getJokesSortedByDate(searchQuery: String, hideNonFavorite : Boolean, selectedCategory: List<String>): Flow<List<JokeEntity>>

    @Insert
    suspend fun insert(joke: JokeEntity): Long

    @Update
    suspend fun update(joke: JokeEntity): Int

    @Delete
    suspend fun delete(joke: JokeEntity): Int

    @Query("DELETE FROM joke")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM joke WHERE favorite = 0")
    suspend fun deleteAllNonFavorite(): Int

    @Insert
    suspend fun insertCategory(category: CategoryEntity): Long

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT category FROM joke")
    fun getCategoryOfJoke(): Flow<List<CategoryEntity>>


}