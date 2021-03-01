package com.JoostAndroid.joke_exercise.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.JoostAndroid.joke_exercise.databinding.ItemJokeBinding
import com.JoostAndroid.joke_exercise.models.Joke

class JokeAdapter : ListAdapter<Joke, JokeAdapter.JokeViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val binding = ItemJokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JokeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }


    class JokeViewHolder(private val binding: ItemJokeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(joke: Joke) {
            binding.apply {
                jokeMain.text = joke.jokeText
                if (joke.favorite) {
                    favorite.setImageResource(android.R.drawable.btn_star_big_on)
                } else {
                    favorite.setImageResource(android.R.drawable.btn_star_big_off)
                }
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Joke>() {
    override fun areItemsTheSame(oldItem: Joke, newItem: Joke): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Joke, newItem: Joke): Boolean = oldItem == newItem
}



