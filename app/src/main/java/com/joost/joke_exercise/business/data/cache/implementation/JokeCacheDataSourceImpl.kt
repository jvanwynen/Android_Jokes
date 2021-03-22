package com.joost.joke_exercise.business.data.cache.implementation

import com.joost.joke_exercise.business.data.cache.abstraction.JokeCacheDataSource
import com.joost.joke_exercise.framework.datasource.cache.localstorage.SortOrder
import com.joost.joke_exercise.business.domain.model.Category
import com.joost.joke_exercise.business.domain.model.Joke
import com.joost.joke_exercise.framework.datasource.cache.abstraction.JokeDaoService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeCacheDataSourceImpl @Inject constructor(
private val jokeDaoService : JokeDaoService
) : JokeCacheDataSource {
    override fun getJokes(
        query: String,
        sortOrder: SortOrder,
        hideNonFavorite: Boolean,
        selectedCategory: List<String>
    ): Flow<List<Joke>> = jokeDaoService.getJokes(query, sortOrder, hideNonFavorite, selectedCategory)

    override suspend fun insert(joke: Joke): Long = jokeDaoService.insert(joke)

    override suspend fun update(joke: Joke): Int = jokeDaoService.update(joke)

    override suspend fun delete(joke: Joke): Int = jokeDaoService.delete(joke)

    override suspend fun deleteAll(): Int = jokeDaoService.deleteAll()

    override suspend fun deleteAllNonFavorite(): Int = jokeDaoService.deleteAllNonFavorite()

    override suspend fun insertCategory(category: Category): Long = jokeDaoService.insertCategory(category)

    override fun getCategories(): Flow<List<Category>> = jokeDaoService.getCategories()

}