package com.joost.joke_exercise.framework.presentation.dialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.joost.joke_exercise.framework.datasource.network.repository.JokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteViewModel @Inject constructor(
    private val jokeRepository: JokeRepository,
    private val applicationScope: CoroutineScope,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val message = savedStateHandle.get<String>("message")

    private val deleteAll = savedStateHandle.get<Boolean>("deleteAll")

    fun onConfirmClicked() = applicationScope.launch {
        if (deleteAll == true) {
            jokeRepository.deleteAllJokesFromDatabase()
        } else {
            jokeRepository.deleteAllNonFavoriteJokesFromDatabase()
        }
    }
}