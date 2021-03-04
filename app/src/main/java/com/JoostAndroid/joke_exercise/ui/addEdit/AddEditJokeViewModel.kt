package com.JoostAndroid.joke_exercise.ui.addEdit


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.JoostAndroid.joke_exercise.localstorage.JokeDAO
import com.JoostAndroid.joke_exercise.models.Joke
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class AddEditJokeViewModel @Inject constructor(
    private val jokeDAO: JokeDAO,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    val joke = savedStateHandle.get<Joke>("joke")

    var jokeText = savedStateHandle.get<String>("jokeText") ?: joke?.jokeText ?: ""
        set(value) {
            field = value
            savedStateHandle.set("jokeText", value)
        }
    var jokeFavorite = savedStateHandle.get<Boolean>("jokeFavorite") ?: joke?.favorite ?: false
        set(value) {
            field = value
            savedStateHandle.set("jokeFavorite", value)
        }


}