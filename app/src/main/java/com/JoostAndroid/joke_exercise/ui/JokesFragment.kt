package com.JoostAndroid.joke_exercise.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.JoostAndroid.joke_exercise.R
import com.JoostAndroid.joke_exercise.databinding.FragmentJokesListBinding
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
                    ))
            }
        }

        viewModel.jokes.observe(viewLifecycleOwner) {
            jokeAdapter.submitList(it)
        }
    }
}
