package com.joost.joke_exercise.localstorage

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesRepository"

enum class SortOrder{BY_NAME, BY_DATE}

data class FilterPreferences(val sortOrder: SortOrder, val hideNonFavorite : Boolean, val selectedCategories: String)

@Singleton
class PreferencesRepository @Inject constructor(@ApplicationContext context: Context){

    private val dataStore = context.createDataStore("user_preferences")

    val preferencesFlow = dataStore.data
        .catch { exc ->
            if(exc is IOException){
                Log.e(TAG, "Preferences gone wrong", exc)
                emit(emptyPreferences())
            }
            throw exc
        }
        .map{
            val sortOrder = SortOrder.valueOf(
                it[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            )
            val hideNonFavorite = it[PreferencesKeys.HIDE_NON_FAVORITE] ?: false
            val selectedCategories = it[PreferencesKeys.SELECTED_CATEGORIES] ?: ""
            FilterPreferences(sortOrder, hideNonFavorite, selectedCategories)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder){
        dataStore.edit {
            it[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideNonFavorite(hideNonFavorite: Boolean){
        dataStore.edit {
            it[PreferencesKeys.HIDE_NON_FAVORITE] = hideNonFavorite
        }
    }

    suspend fun updateSelectedCategories(selectedCategories: String){
        dataStore.edit {
            it[PreferencesKeys.SELECTED_CATEGORIES] = selectedCategories
        }
    }

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val HIDE_NON_FAVORITE = booleanPreferencesKey("hide_non_favorite")
        val SELECTED_CATEGORIES = stringPreferencesKey("selected_categories")
    }

}