package com.joost.joke_exercise.framework.datasource.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    val category: String
)
