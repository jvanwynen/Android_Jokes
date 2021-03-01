package com.JoostAndroid.joke_exercise.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.room.Dao
import com.JoostAndroid.joke_exercise.localstorage.JokeDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JokeViewModel @Inject constructor(
    private val jokeDao: JokeDAO
) : ViewModel() {

    val jokes = jokeDao.getJokes().asLiveData()

}