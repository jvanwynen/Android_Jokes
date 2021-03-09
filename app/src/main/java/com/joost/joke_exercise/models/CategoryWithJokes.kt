package com.joost.joke_exercise.models

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithJokes(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "category",
        entityColumn = "category"
    )
    val jokes: List<Joke>
)
