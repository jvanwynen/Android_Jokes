package com.joost.joke_exercise.framework.datasource.cache.implementation

import com.joost.joke_exercise.business.domain.model.Category
import com.joost.joke_exercise.business.domain.model.Joke
import com.joost.joke_exercise.framework.datasource.cache.abstraction.JokeDaoService
import com.joost.joke_exercise.framework.datasource.cache.localstorage.JokeDAO
import com.joost.joke_exercise.framework.datasource.cache.localstorage.SortOrder
import com.joost.joke_exercise.framework.datasource.cache.mapper.CacheCategoryMapper
import com.joost.joke_exercise.framework.datasource.cache.mapper.CacheJokeMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeDaoServiceImpl  @Inject constructor(
    private val jokeDAO: JokeDAO,
    private val cacheJokeMapper: CacheJokeMapper,
    private val cacheCategoryMapper: CacheCategoryMapper
) : JokeDaoService {
    override fun getJokes(
        query: String,
        sortOrder: SortOrder,
        hideNonFavorite: Boolean,
        selectedCategory: List<String>
    ): Flow<List<Joke>>  = cacheJokeMapper.entityFlowListToJokeFlowList(jokeDAO.getJokes(query, sortOrder, hideNonFavorite, selectedCategory))


    override suspend fun insert(joke: Joke): Long = jokeDAO.insert(cacheJokeMapper.mapToEntity(joke))

    override suspend fun update(joke: Joke): Int = jokeDAO.update(cacheJokeMapper.mapToEntity(joke))

    override suspend fun delete(joke: Joke): Int = jokeDAO.delete(cacheJokeMapper.mapToEntity(joke))

    override suspend fun deleteAll(): Int = jokeDAO.deleteAll()

    override suspend fun deleteAllNonFavorite(): Int  = jokeDAO.deleteAllNonFavorite()

    override suspend fun insertCategory(category: Category): Long = jokeDAO.insertCategory(cacheCategoryMapper.mapToEntity(category))

    override fun getCategories(): Flow<List<Category>> = cacheCategoryMapper.entityFlowListToCategoryFlowList(jokeDAO.getCategories())
}