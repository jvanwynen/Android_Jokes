package com.joost.joke_exercise.ui.addEdit


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.joost.joke_exercise.api.JokeRepository
import com.joost.joke_exercise.localstorage.JokeDAO
import com.joost.joke_exercise.models.Joke
import com.joost.joke_exercise.models.JokeApiResponse
import com.joost.joke_exercise.ui.ADD_JOKE_RESULT_OK
import com.joost.joke_exercise.ui.EDIT_JOKE_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditJokeViewModel @Inject constructor(
    private val jokeRepository: JokeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val joke = savedStateHandle.get<Joke>("joke")

    private val categoryFlow = jokeRepository.getAllCategories()

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

    var delivery = savedStateHandle.get<String>("delivery") ?: joke?.delivery ?: ""
        set(value) {
            field = value
            savedStateHandle.set("delivery", value)
        }


    var jokeFavorite = savedStateHandle.get<Boolean>("jokeFavorite") ?: joke?.favorite ?: false
        set(value) {
            field = value
            savedStateHandle.set("jokeFavorite", value)
        }

    private val addEditJokeChannel = Channel<AddEditJokeEvent>()

    val addEditJokeEvent = addEditJokeChannel.receiveAsFlow()

    //Method to handle save button clicked, makes new or updates joke
    fun onSaveClick() {
        if (jokeText.isEmpty()) {
            showInvalidInputMessage("Empty jokes are not funny")
            return
        }
        if (joke != null) {
            val updatedJoke =
                joke.copy(
                    jokeText = jokeText,
                    favorite = jokeFavorite,
                    category = category,
                    delivery = delivery
                )
            updateJoke(updatedJoke)
        } else {
            val newJoke =
                Joke(
                    jokeText = jokeText,
                    favorite = jokeFavorite,
                    category = category,
                    delivery = delivery
                )
            createJoke(newJoke)
        }
    }

    fun getOnlineJoke(category: String, flags: List<String>, type: Boolean) =
        viewModelScope.launch {
            resultToJoke(jokeRepository.getOnlineJoke(category, flags, booleanTypeToString(type)))
        }


    private fun createJoke(joke: Joke) = viewModelScope.launch {
        jokeRepository.insertJokeInDatabase(joke)
        addEditJokeChannel.send(AddEditJokeEvent.NavigateBackResult(ADD_JOKE_RESULT_OK))
    }

    private fun updateJoke(joke: Joke) = viewModelScope.launch {
        jokeRepository.updateJoke(joke)
        addEditJokeChannel.send(AddEditJokeEvent.NavigateBackResult(EDIT_JOKE_RESULT_OK))
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        addEditJokeChannel.send(AddEditJokeEvent.InvalidInput(msg))
    }

    private fun booleanTypeToString(type: Boolean) = if (type) {
        "twopart"
    } else {
        "single"
    }

    /*
    Method to turn the result from the Joke API in to Joke object, necessary because result may vary
     */
    private fun resultToJoke(result: JokeApiResponse?) = viewModelScope.launch {
        if (result != null) {
            if (result.joke?.isEmpty() == false) {
                addEditJokeChannel.send(
                    AddEditJokeEvent.JokeFromInternetResult(
                        Joke(
                            jokeText = result.joke,
                            category = result.category
                        )
                    )
                )
            } else {
                addEditJokeChannel.send(
                    AddEditJokeEvent.JokeFromInternetResult(
                        Joke(
                            jokeText = result.setup ?: "",
                            delivery = result.delivery ?: "",
                            category = result.category
                        )
                    )
                )
            }
        }
    }

    fun onOnlineClick() = viewModelScope.launch {
        addEditJokeChannel.send(AddEditJokeEvent.ShowSelection)
    }

    sealed class AddEditJokeEvent {
        data class InvalidInput(val msg: String) : AddEditJokeEvent()
        data class NavigateBackResult(val result: Int) : AddEditJokeEvent()
        data class JokeFromInternetResult(val joke: Joke) : AddEditJokeEvent()
        object ShowSelection : AddEditJokeEvent()
    }

}