package com.JoostAndroid.joke_exercise.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Joke(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val jokeText: String,
    val favorite: Boolean = false,
    val created: Long

) : Parcelable