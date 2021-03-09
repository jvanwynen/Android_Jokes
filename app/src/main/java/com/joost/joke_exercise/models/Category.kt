package com.joost.joke_exercise.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Category(
    @PrimaryKey(autoGenerate = false)
    val category: String
): Parcelable {

    override fun toString(): String {
        return category
    }

}