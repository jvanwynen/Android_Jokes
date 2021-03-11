package com.joost.joke_exercise.ui.addEdit


import androidx.lifecycle.*
import com.joost.joke_exercise.api.JokeRepository
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
    private val jokeRepository: JokeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val joke = savedStateHandle.get<Joke>("joke")

    val onlineJoke: MutableLiveData<Joke> = MutableLiveData()

    private val categoryFlow = jokeDAO.getCategories()

    val categories = categoryFlow.asLiveData()

    var category = savedStateHandle.get<String>("category") ?: joke?.category ?: ""
        set(value) {
            field = value
            savedStateHandle.set("category", value)
        }

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
            val updatedJoke =
                joke.copy(jokeText = jokeText, favorite = jokeFavorite, category = category)
            updateJoke(updatedJoke)
        } else {
            val newJoke =
                Joke(jokeText = jokeText, favorite = jokeFavorite, category = category)
            createJoke(newJoke)
        }
    }

    fun getOnlineJoke(path: String) = viewModelScope.launch {
        onlineJoke.value = jokeRepository.getOnlineJoke(path)
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

    sealed class AddEditJokeEvent {
        data class InvalidInput(val msg: String) : AddEditJokeEvent()
        data class NavigateBackResult(val result: Int) : AddEditJokeEvent()
        object ShowSelection : AddEditJokeEvent()
    }

}