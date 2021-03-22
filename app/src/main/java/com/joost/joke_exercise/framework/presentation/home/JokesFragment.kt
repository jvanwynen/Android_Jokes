package com.joost.joke_exercise.framework.presentation.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.joost.joke_exercise.R
import com.joost.joke_exercise.databinding.FragmentJokesListBinding
import com.joost.joke_exercise.framework.datasource.cache.localstorage.SortOrder
import com.joost.joke_exercise.framework.presentation.dialog.DeleteDialogFragmentDirections

import com.joost.joke_exercise.framework.util.exhaustive
import com.joost.joke_exercise.framework.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class JokesFragment : Fragment(R.layout.fragment_jokes_list) {

    private lateinit var binding: FragmentJokesListBinding

    private val viewModel: JokeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentJokesListBinding.bind(view)

        val jokeAdapter = JokeAdapter(
            onItemLongClicked = {
                viewModel.onJokeSelected(it)
            }
        )

        val listener = View.OnClickListener {
            when (it) {
                binding.checkboxDark -> {
                    viewModel.onDarkSelected(binding.checkboxDark.isChecked)
                }
                binding.checkboxProg -> {
                    viewModel.onProgrammingSelected(binding.checkboxProg.isChecked)
                }
                binding.checkboxPun -> {
                    viewModel.onPun(binding.checkboxPun.isChecked)
                }
                binding.checkboxSpooky -> {
                   viewModel.onSpooky(binding.checkboxSpooky.isChecked)
                }
                binding.checkboxChrist -> {
                   viewModel.onChristmasSelected(binding.checkboxChrist.isChecked)
                }
                binding.checkboxMisc -> {
                   viewModel.onMiscSelected(binding.checkboxMisc.isChecked)
                }
            }
        }

        binding.apply {
            recyclerView.apply {
                adapter = jokeAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val joke = jokeAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onJokeSwiped(joke)
                }
            }).attachToRecyclerView(recyclerView)
            actionButton.setOnClickListener {
                viewModel.addNewJokeClicked()
            }
            checkboxProg.setOnClickListener(listener)
            checkboxChrist.setOnClickListener(listener)
            checkboxDark.setOnClickListener(listener)
            checkboxMisc.setOnClickListener(listener)
            checkboxPun.setOnClickListener(listener)
            checkboxSpooky.setOnClickListener(listener)
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.get("add_edit_result")
            viewModel.onAddEditResult(result as Int)
        }

        viewModel.jokes.observe(viewLifecycleOwner) {
            jokeAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            binding.checkboxChrist.isChecked =
                viewModel.preferencesFlow.first().showChristmas

            binding.checkboxProg.isChecked =
                viewModel.preferencesFlow.first().showProgramming

            binding.checkboxPun.isChecked =
                viewModel.preferencesFlow.first().showPun

            binding.checkboxDark.isChecked =
                viewModel.preferencesFlow.first().showDark

            binding.checkboxMisc.isChecked =
                viewModel.preferencesFlow.first().showMisc

            binding.checkboxSpooky.isChecked =
                viewModel.preferencesFlow.first().showSpooky
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.jokeEvent.collect { event ->
                when (event) {
                    is JokeViewModel.JokeEvent.ShowUndoDeleteMessage -> {
                        Snackbar.make(requireView(), "Joke Deleted!", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClicked(event.joke)
                            }.show()
                    }
                    is JokeViewModel.JokeEvent.NavigateToAddJoke -> {
                        val action =
                            JokesFragmentDirections.actionJokesFragmentToAddEditJokeFragment(
                                null,
                                "New Joke"
                            )
                        findNavController().navigate(action)
                    }
                    is JokeViewModel.JokeEvent.NavigateToEditJoke -> {
                        val action =
                            JokesFragmentDirections.actionJokesFragmentToAddEditJokeFragment(
                                event.joke,
                                "Edit Joke"
                            )
                        findNavController().navigate(action)
                    }
                    is JokeViewModel.JokeEvent.ShowJokeSavedConfirm -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    JokeViewModel.JokeEvent.NavigateToDeleteAllNonFavorite -> {
                        val action = DeleteDialogFragmentDirections
                            .actionGlobalDeleteAllNonFavoriteDialogFragment(
                                "Do you want to delete all non Favorite Jokes?", false
                            )
                        findNavController().navigate(action)
                    }
                    JokeViewModel.JokeEvent.NavigateToDeleteAll -> {
                        val action = DeleteDialogFragmentDirections
                            .actionGlobalDeleteAllNonFavoriteDialogFragment(
                                "Do you want to delete All Items?", true
                            )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_jokes, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_non_favorite).isChecked =
                viewModel.preferencesFlow.first().hideNonFavorite
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_date -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_non_favorite -> {
                item.isChecked = !item.isChecked
                viewModel.onHidingSelected(item.isChecked)
                true
            }
            R.id.action_delete_non_favorite -> {
                viewModel.onDeleteNonFavoriteClicked()
                true
            }
            R.id.action_delete_all -> {
                viewModel.onDeleteAllClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
