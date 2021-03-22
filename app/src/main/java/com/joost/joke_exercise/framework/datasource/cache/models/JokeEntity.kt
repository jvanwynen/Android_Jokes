package com.joost.joke_exercise.framework.datasource.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "joke")
data class JokeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val jokeText: String,
    val favorite: Boolean = false,
    val delivery: String = "",
    val created: Long = System.currentTimeMillis(),
    val category: String

) {

}