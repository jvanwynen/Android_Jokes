package com.JoostAndroid.joke_exercise.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.JoostAndroid.joke_exercise.R
import com.JoostAndroid.joke_exercise.databinding.FragmentJokesListBinding
import com.JoostAndroid.joke_exercise.util.OnQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class JokesFragment : Fragment(R.layout.fragment_jokes_list) {

    private val viewModel: JokeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentJokesListBinding.bind(view)

        val jokeAdapter = JokeAdapter()

        binding.apply {
            recyclerView.apply {
                adapter = jokeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    DividerItemDecoration(
                        recyclerView.context,
                        (layoutManager as LinearLayoutManager).orientation
                    )
                )
            }
        }

        viewModel.jokes.observe(viewLifecycleOwner) {
            jokeAdapter.submitList(it)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_jokes, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.OnQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            R.id.action_sort_date -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.action_hide_non_favorite -> {
                item.isChecked = !item.isChecked
                viewModel.hideNonFavorite.value = item.isChecked
                true
            }
            R.id.action_delete_non_favorite -> {

                true
            }
            R.id.action_delete_all -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
