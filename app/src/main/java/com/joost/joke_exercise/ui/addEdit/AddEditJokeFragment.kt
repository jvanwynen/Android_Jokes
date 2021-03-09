package com.joost.joke_exercise.ui.addEdit

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.joost.joke_exercise.R
import com.joost.joke_exercise.databinding.FragmentAddEditJokesBinding
import com.joost.joke_exercise.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class AddEditJokeFragment : Fragment(R.layout.fragment_add_edit_jokes){

    private val viewModel: AddEditJokeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditJokesBinding.bind(view)

        viewModel.categories.observe(viewLifecycleOwner) {
            binding.apply {
                addEditSpinner.apply {
                    isClickable = viewModel.editOrNew()
                    isEnabled = viewModel.editOrNew()
                }
                addEditSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    it
                )
                addEditSpinner.setSelection(
                    viewModel.getIndex(
                        addEditSpinner,
                        viewModel.joke?.category
                    )
                )
            }
        }

        binding.apply {
            jokeTextEdit.setText(viewModel.jokeText)
            isFavoriteEdit.isChecked = viewModel.jokeFavorite
            isFavoriteEdit.jumpDrawablesToCurrentState()
            dateCreatedEdit.isVisible = viewModel.editOrNew()
            addEditButtonOnline.isVisible = viewModel.editOrNew()
            addEditSpinnerText.isVisible = viewModel.editOrNew()
            dateCreatedEdit.text = "Created: ${viewModel.joke?.createFormattedDate}"

            jokeTextEdit.addTextChangedListener{
                viewModel.jokeText = it.toString()
            }

            isFavoriteEdit.setOnCheckedChangeListener { _, isChecked ->
                viewModel.jokeFavorite = isChecked
            }

            actionSave.setOnClickListener {
                viewModel.onSaveClick()
            }

            addEditButtonOnline.setOnClickListener {
                viewModel.onOnlineClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditJokeEvent.collect { event ->
                when (event) {
                    is AddEditJokeViewModel.AddEditJokeEvent.InvalidInput -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditJokeViewModel.AddEditJokeEvent.NavigateBackResult -> {
                        binding.jokeTextEdit.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditJokeViewModel.AddEditJokeEvent.ShowSelection -> {
                        binding.addEditLayoutSelection.isVisible =
                            !binding.addEditLayoutSelection.isVisible
                    }
                }.exhaustive
            }
        }
    }
}