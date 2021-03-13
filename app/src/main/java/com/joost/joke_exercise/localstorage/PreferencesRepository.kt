package com.joost.joke_exercise.localstorage

import android.content.Context
import android.text.BoringLayout
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

data class FilterPreferences(
    val sortOrder: SortOrder,
    val hideNonFavorite : Boolean,
    val showProgramming: Boolean,
    val showDark: Boolean,
    val showSpooky: Boolean,
    val showChristmas: Boolean,
    val showPun: Boolean,
    val showMisc: Boolean
)

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
            val showProgramming = it[PreferencesKeys.SHOW_PROG] ?: false
            val showPun = it[PreferencesKeys.SHOW_PUN] ?: false
            val showDark = it[PreferencesKeys.SHOW_DARK] ?: false
            val showMisc = it[PreferencesKeys.SHOW_MISC] ?: false
            val showSpooky = it[PreferencesKeys.SHOW_SPOOKY] ?: false
            val showChristmas = it[PreferencesKeys.SHOW_CHRIST] ?: false
            FilterPreferences(sortOrder, hideNonFavorite, showProgramming, showDark, showSpooky, showChristmas, showPun, showMisc)
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

    suspend fun updateShowProgramming(showProgramming: Boolean){
        dataStore.edit {
            it[PreferencesKeys.SHOW_PROG] = showProgramming
        }
    }

    suspend fun updateShowSpooky(showSpooky: Boolean){
        dataStore.edit {
            it[PreferencesKeys.SHOW_SPOOKY] = showSpooky
        }
    }
    suspend fun updateShowMisc(showMisc: Boolean){
        dataStore.edit {
            it[PreferencesKeys.SHOW_MISC] = showMisc
        }
    }
    suspend fun updateShowPun(showPun: Boolean){
        dataStore.edit {
            it[PreferencesKeys.SHOW_PUN] = showPun
        }
    }
    suspend fun updateShowChrist(showChristmas: Boolean){
        dataStore.edit {
            it[PreferencesKeys.SHOW_CHRIST] = showChristmas
        }
    }
    suspend fun updateShowDark(showDark: Boolean){
        dataStore.edit {
            it[PreferencesKeys.SHOW_DARK] = showDark
        }
    }

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val HIDE_NON_FAVORITE = booleanPreferencesKey("hide_non_favorite")
        val SHOW_PROG = booleanPreferencesKey("show_programming")
        val SHOW_DARK = booleanPreferencesKey("show_dark")
        val SHOW_MISC = booleanPreferencesKey("show_misc")
        val SHOW_PUN = booleanPreferencesKey("show_pun")
        val SHOW_SPOOKY = booleanPreferencesKey("show_spooky")
        val SHOW_CHRIST = booleanPreferencesKey("show_christ")
    }

}