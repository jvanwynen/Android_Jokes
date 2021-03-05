package com.JoostAndroid.joke_exercise.ui.home


import androidx.lifecycle.*
import com.JoostAndroid.joke_exercise.localstorage.JokeDAO
import com.JoostAndroid.joke_exercise.localstorage.PreferencesRepository
import com.JoostAndroid.joke_exercise.localstorage.SortOrder
import com.JoostAndroid.joke_exercise.models.Joke
import com.JoostAndroid.joke_exercise.ui.ADD_JOKE_RESULT_OK
import com.JoostAndroid.joke_exercise.ui.EDIT_JOKE_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokeViewModel @Inject constructor(
    private val jokeDao: JokeDAO,
    private val preferencesRepository: PreferencesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val searchQuery = savedStateHandle.getLiveData("query", "")

    val preferencesFlow = preferencesRepository.preferencesFlow

    private val jokeEventChannel = Channel<JokeEvent>()

    val jokeEvent = jokeEventChannel.receiveAsFlow()

    private val jokeFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        jokeDao.getJokes(query, filterPreferences.sortOrder, filterPreferences.hideNonFavorite)
    }

    val jokes = jokeFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesRepository.updateSortOrder(sortOrder)
    }

    fun onHidingSelected(hideNonFavorite : Boolean) = viewModelScope.launch {
        preferencesRepository.updateHideNonFavorite(hideNonFavorite)
    }

    fun onJokeSwiped(joke: Joke) = viewModelScope.launch {
        jokeDao.delete(joke)
        jokeEventChannel.send(JokeEvent.ShowUndoDeleteMessage(joke))
    }

    fun onUndoDeleteClicked(joke: Joke) = viewModelScope.launch {
        jokeDao.insert(joke)
    }

    fun addNewJokeClicked() = viewModelScope.launch {
        jokeEventChannel.send(JokeEvent.NavigateToAddJoke)
    }

    fun onJokeSelected(joke: Joke) = viewModelScope.launch {
        jokeEventChannel.send(JokeEvent.NavigateToEditJoke(joke))
    }

    fun onAddEditResult(result: Int){
        when (result){
            ADD_JOKE_RESULT_OK -> showJokeSaved("Joke added")
            EDIT_JOKE_RESULT_OK -> showJokeSaved("Joke updated")
        }
    }

    private fun showJokeSaved(msg: String) = viewModelScope.launch {
        jokeEventChannel.send(JokeEvent.ShowJokeSavedConfirm(msg))
    }

     fun onDeleteNonFavoriteClicked() = viewModelScope.launch {
        jokeEventChannel.send(JokeEvent.NavigateToDeleteAllNonFavorite)
    }

    fun onDeleteAllClicked() = viewModelScope.launch {
        jokeEventChannel.send(JokeEvent.NavigateToDeleteAll)
    }

    sealed class JokeEvent{
        data class ShowUndoDeleteMessage(val joke: Joke) : JokeEvent()
        object NavigateToAddJoke: JokeEvent()
        data class NavigateToEditJoke(val joke: Joke): JokeEvent()
        data class ShowJokeSavedConfirm(val msg: String) : JokeEvent()
        object NavigateToDeleteAllNonFavorite : JokeEvent()
        object NavigateToDeleteAll : JokeEvent()
    }

}

