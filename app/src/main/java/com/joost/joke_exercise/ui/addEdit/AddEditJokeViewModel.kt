package com.joost.joke_exercise.ui.addEdit


import android.widget.Spinner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.joost.joke_exercise.localstorage.JokeDAO
import com.joost.joke_exercise.models.Joke
import com.joost.joke_exercise.ui.ADD_JOKE_RESULT_OK
import com.joost.joke_exercise.ui.EDIT_JOKE_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class AddEditJokeViewModel @Inject constructor(
    private val jokeDAO: JokeDAO,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val joke = savedStateHandle.get<Joke>("joke")

    private val categoryFlow = jokeDAO.getCategories()

    val categories = categoryFlow.asLiveData()

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

    private val addEditJokeChannel = Channel<AddEditJokeEvent>()
    val addEditJokeEvent = addEditJokeChannel.receiveAsFlow()

    fun onSaveClick() {
        if (jokeText.isEmpty()) {
            showInvalidInputMessage("Empty jokes are not funny")
            return
        }
        if (joke != null) {
            val updatedJoke = joke.copy(jokeText = jokeText, favorite = jokeFavorite)
            updateJoke(updatedJoke)
        } else {
            val newJoke =
                Joke(jokeText = jokeText, favorite = jokeFavorite, category = "Programming")
            createJoke(newJoke)
        }
    }

    private fun createJoke(joke: Joke) = viewModelScope.launch {
        jokeDAO.insert(joke)
        addEditJokeChannel.send(AddEditJokeEvent.NavigateBackResult(ADD_JOKE_RESULT_OK))
    }

    private fun updateJoke(joke: Joke) = viewModelScope.launch {
        jokeDAO.update(joke)
        addEditJokeChannel.send(AddEditJokeEvent.NavigateBackResult(EDIT_JOKE_RESULT_OK))
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        addEditJokeChannel.send(AddEditJokeEvent.InvalidInput(msg))
    }

    fun onOnlineClick() = viewModelScope.launch {
        addEditJokeChannel.send(AddEditJokeEvent.ShowSelection)
    }

    fun getIndex(spinner: Spinner, myString: String?): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    //true if new, false if edit
    fun editOrNew(): Boolean = joke == null

    sealed class AddEditJokeEvent {
        data class InvalidInput(val msg: String) : AddEditJokeEvent()
        data class NavigateBackResult(val result: Int) : AddEditJokeEvent()
        object ShowSelection : AddEditJokeEvent()
    }

}