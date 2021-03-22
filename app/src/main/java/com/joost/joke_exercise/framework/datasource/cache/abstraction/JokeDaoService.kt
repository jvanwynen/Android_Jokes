package com.joost.joke_exercise.framework.datasource.cache.abstraction

import com.joost.joke_exercise.business.domain.model.Category
import com.joost.joke_exercise.business.domain.model.Joke
import com.joost.joke_exercise.framework.datasource.cache.localstorage.SortOrder
import kotlinx.coroutines.flow.Flow

interface JokeDaoService {

    fun getJokes(query: String,
                 sortOrder: SortOrder,
                 hideNonFavorite: Boolean,
                 selectedCategory: List<String>
    ): Flow<List<Joke>>

    suspend fun insert(joke: Joke): Long

    suspend fun update(joke: Joke): Int

    suspend fun delete(joke: Joke): Int

    suspend fun deleteAll(): Int

    suspend fun deleteAllNonFavorite(): Int

    suspend fun insertCategory(category: Category): Long

    fun getCategories(): Flow<List<Category>>



}