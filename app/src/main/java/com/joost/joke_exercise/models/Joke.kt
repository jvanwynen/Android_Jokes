package com.joost.joke_exercise.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity
@Parcelize
data class Joke(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val jokeText: String,
    val favorite: Boolean = false,
    val delivery: String = "",
    val created: Long = System.currentTimeMillis(),
    val category: String

) : Parcelable {

    val createFormattedDate: String
        get() = DateFormat.getDateTimeInstance().format(created)

}
