package com.joost.joke_exercise.ui.addEdit

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.joost.joke_exercise.models.Joke
import com.joost.joke_exercise.util.exhaustive
import com.joost.joke_exercise.util.setSelectionOnStringValue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class AddEditJokeFragment : Fragment(R.layout.fragment_add_edit_jokes) {

    private val viewModel: AddEditJokeViewModel by viewModels()

    private lateinit var binding: FragmentAddEditJokesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddEditJokesBinding.bind(view)

        //fill spinner with all available categories from database
        viewModel.categories.observe(viewLifecycleOwner) {
            binding.apply {
                addEditSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    it
                )
                addEditSpinner.setSelectionOnStringValue(
                    viewModel.category
                )
            }
        }

        binding.apply {
            jokeTextEdit.setText(viewModel.jokeText)
            jokeTextDelivery.setText(viewModel.delivery)
            isFavoriteEdit.isChecked = viewModel.jokeFavorite
            isFavoriteEdit.jumpDrawablesToCurrentState()
            dateCreatedEdit.isVisible = viewModel.joke != null
            addEditButtonOnline.isVisible = viewModel.joke == null
            addEditSpinnerText.isVisible = viewModel.joke == null
            dateCreatedEdit.text = "Created: ${viewModel.joke?.createFormattedDate}"

            jokeTextEdit.addTextChangedListener {
                viewModel.jokeText = it.toString()
            }

            jokeTextDelivery.addTextChangedListener {
                viewModel.delivery = it.toString()
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


            addEditButtonGet.setOnClickListener {
                //TODO: improve
                val selectedCategories = mutableListOf<String>()
                if (binding.addEditCheckSpooky.isChecked) {
                    selectedCategories.add(binding.addEditCheckSpooky.text.toString())
                }

                if (binding.addEditCheckPun.isChecked) {
                    selectedCategories.add(binding.addEditCheckPun.text.toString())
                }

                if (binding.addEditCheckProg.isChecked) {
                    selectedCategories.add(binding.addEditCheckProg.text.toString())
                }

                if (binding.addEditCheckChrist.isChecked) {
                    selectedCategories.add(binding.addEditCheckChrist.text.toString())
                }

                if (binding.addEditCheckMisc.isChecked) {
                    selectedCategories.add(binding.addEditCheckMisc.text.toString())
                }

                if (binding.addEditCheckDark.isChecked) {
                    selectedCategories.add(binding.addEditCheckDark.text.toString())
                }
                viewModel.getOnlineJoke(
                    binding.addEditSpinner.selectedItem.toString(),
                    selectedCategories,
                    binding.addEditSwitch.isChecked
                )
            }

            //save the selected category of spinner
            addEditSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    if (parent != null) {
                        viewModel.category = parent.getItemAtPosition(position).toString()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

        //Get event from ViewModel and react accordingly
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
                    is AddEditJokeViewModel.AddEditJokeEvent.JokeFromInternetResult -> {
                        displayOnlineJoke(event.joke)
                    }
                }.exhaustive
            }
        }
    }

    //Displays the content of the online joke
    private fun displayOnlineJoke(onlineJoke: Joke) {
        binding.apply {
            viewModel.jokeText = onlineJoke.jokeText
            viewModel.delivery = onlineJoke.delivery
            viewModel.category = onlineJoke.category
            jokeTextEdit.setText(viewModel.jokeText)
            jokeTextDelivery.setText(viewModel.delivery)
            addEditSpinner.setSelectionOnStringValue(
                viewModel.category
            )
        }
    }
}