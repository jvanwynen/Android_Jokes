package com.JoostAndroid.joke_exercise.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.JoostAndroid.joke_exercise.localstorage.JokeDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class JokeViewModel @Inject constructor(
    private val jokeDao: JokeDAO
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideNonFavorite = MutableStateFlow(false)

    private val jokeFlow = combine(
        searchQuery,
        sortOrder,
        hideNonFavorite
    ) { query, sortOrder, hideNonFavorite ->
        Triple(query, sortOrder, hideNonFavorite)
    }.flatMapLatest { (query, sortOrder, hideNonFavorite) ->
        jokeDao.getJokes(query, sortOrder, hideNonFavorite)
    }

    val jokes = jokeFlow.asLiveData()

}

enum class SortOrder{BY_NAME, BY_DATE}