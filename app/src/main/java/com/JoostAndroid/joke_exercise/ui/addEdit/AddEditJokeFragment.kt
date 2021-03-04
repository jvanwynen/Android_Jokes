package com.JoostAndroid.joke_exercise.ui.addEdit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.JoostAndroid.joke_exercise.R
import com.JoostAndroid.joke_exercise.databinding.FragmentAddEditJokesBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEditJokeFragment : Fragment(R.layout.fragment_add_edit_jokes){

    private val viewModel: AddEditJokeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditJokesBinding.bind(view)

        binding.apply {
            jokeTextEdit.setText(viewModel.jokeText)
            isFavoriteEdit.isChecked = viewModel.jokeFavorite
            isFavoriteEdit.jumpDrawablesToCurrentState()
            dateCreatedEdit.isVisible = viewModel.joke != null
            dateCreatedEdit.text = "Created: ${viewModel.joke?.createFormattedDate}"
        }
    }

}