package com.JoostAndroid.joke_exercise.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity
@Parcelize
data class Joke(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val jokeText: String,
    val favorite: Boolean = false,
    val created: Long = System.currentTimeMillis()

) : Parcelable {

    val createFormattedDate: String
        get() = DateFormat.getDateTimeInstance().format(created)

}