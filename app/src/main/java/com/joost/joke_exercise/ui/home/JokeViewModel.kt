package com.joost.joke_exercise.ui.home


import androidx.lifecycle.*
import com.joost.joke_exercise.api.JokeRepository
import com.joost.joke_exercise.localstorage.FilterPreferences
import com.joost.joke_exercise.localstorage.JokeDAO
import com.joost.joke_exercise.localstorage.PreferencesRepository
import com.joost.joke_exercise.localstorage.SortOrder
import com.joost.joke_exercise.models.Joke
import com.joost.joke_exercise.ui.ADD_JOKE_RESULT_OK
import com.joost.joke_exercise.ui.EDIT_JOKE_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokeViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val jokeRepository: JokeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val searchQuery = savedStateHandle.getLiveData("query", "")

    val preferencesFlow = preferencesRepository.preferencesFlow

    private val jokeEventChannel = Channel<JokeEvent>()

    val jokeEvent = jokeEventChannel.receiveAsFlow()

    private val jokeFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    )
    { query, filterPreferences ->
        Pair(query, filterPreferences)
    }
        .flatMapLatest { (query, filterPreferences) ->
            jokeRepository.getJokesFromDatabase(query, filterPreferences)
        }

    val jokes = jokeFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesRepository.updateSortOrder(sortOrder)
    }

    fun onProgrammingSelected(showProg: Boolean) = viewModelScope.launch {
        preferencesRepository.updateShowProgramming(showProg)
    }

    fun onDarkSelected(showDark: Boolean) = viewModelScope.launch {
        preferencesRepository.updateShowDark(showDark)
    }

    fun onSpooky(showSpooky: Boolean) = viewModelScope.launch {
        preferencesRepository.updateShowSpooky(showSpooky)
    }

    fun onPun(showPun: Boolean) = viewModelScope.launch {
        preferencesRepository.updateShowPun(showPun)
    }

    fun onChristmasSelected(showChristmas: Boolean) = viewModelScope.launch {
        preferencesRepository.updateShowChrist(showChristmas)
    }

    fun onMiscSelected(showMisc: Boolean) = viewModelScope.launch {
        preferencesRepository.updateShowMisc(showMisc)
    }

    fun onHidingSelected(hideNonFavorite: Boolean) = viewModelScope.launch {
        preferencesRepository.updateHideNonFavorite(hideNonFavorite)
    }

    fun onJokeSwiped(joke: Joke) = viewModelScope.launch {
        jokeRepository.deleteJokeFromDatabase(joke)
        jokeEventChannel.send(JokeEvent.ShowUndoDeleteMessage(joke))
    }

    fun onUndoDeleteClicked(joke: Joke) = viewModelScope.launch {
        jokeRepository.insertJokeInDatabase(joke)
    }

    fun addNewJokeClicked() = viewModelScope.launch {
        jokeEventChannel.send(JokeEvent.NavigateToAddJoke)
    }

    fun onJokeSelected(joke: Joke) = viewModelScope.launch {
        jokeEventChannel.send(JokeEvent.NavigateToEditJoke(joke))
    }

    fun onAddEditResult(result: Int) {
        when (result) {
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

    sealed class JokeEvent {
        data class ShowUndoDeleteMessage(val joke: Joke) : JokeEvent()
        object NavigateToAddJoke : JokeEvent()
        data class NavigateToEditJoke(val joke: Joke) : JokeEvent()
        data class ShowJokeSavedConfirm(val msg: String) : JokeEvent()
        object NavigateToDeleteAllNonFavorite : JokeEvent()
        object NavigateToDeleteAll : JokeEvent()
    }

}

