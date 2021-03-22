package com.joost.joke_exercise.framework.datasource.cache.mapper

import com.joost.joke_exercise.business.domain.model.Category
import com.joost.joke_exercise.business.domain.util.EntityMapper
import com.joost.joke_exercise.framework.datasource.cache.models.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CacheCategoryMapper () : EntityMapper<CategoryEntity, Category> {

    fun entityFlowListToCategoryFlowList(entities: Flow<List<CategoryEntity>>): Flow<List<Category>> {
        return entities.map{entityFlowListToCategoryFlowList(it)}
    }

    private fun entityFlowListToCategoryFlowList(entities: List<CategoryEntity>): List<Category>{
        val list: ArrayList<Category> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }


    override fun mapFromEntity(entity: CategoryEntity): Category {
        return Category(category = entity.category)
    }

    override fun mapToEntity(domainModel: Category): CategoryEntity {
        return CategoryEntity(category = domainModel.category)
    }
}