package com.joost.joke_exercise.framework.datasource.cache.mapper

import com.joost.joke_exercise.business.domain.model.Joke
import com.joost.joke_exercise.business.domain.util.EntityMapper
import com.joost.joke_exercise.framework.datasource.cache.models.JokeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class CacheJokeMapper() : EntityMapper<JokeEntity, Joke> {

    fun entityFlowListToJokeFlowList(entities: Flow<List<JokeEntity>>): Flow<List<Joke>> {
        return entities.map{entityFlowListToJokeFlowList(it)}
    }

    private fun entityFlowListToJokeFlowList(entities: List<JokeEntity>): List<Joke>{
        val list: ArrayList<Joke> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }


    override fun mapFromEntity(entity: JokeEntity): Joke {
        return Joke(
            id = entity.id,
            jokeText = entity.jokeText,
            favorite = entity.favorite,
            delivery = entity.delivery,
            created = entity.created,
            category = entity.category
        )
    }

    override fun mapToEntity(domainModel: Joke): JokeEntity {
        return JokeEntity(
            id = domainModel.id,
            jokeText = domainModel.jokeText,
            favorite = domainModel.favorite,
            delivery = domainModel.delivery,
            created = domainModel.created,
            category = domainModel.category
        )
    }

}