package com.joost.joke_exercise.business.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
data class Joke(

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
