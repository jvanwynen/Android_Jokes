package com.joost.joke_exercise.business.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val category: String
): Parcelable {

    override fun toString(): String {
        return category
    }

}