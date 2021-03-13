package com.joost.joke_exercise.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joost.joke_exercise.R
import com.joost.joke_exercise.databinding.ItemJokeBinding
import com.joost.joke_exercise.models.Joke

class JokeAdapter(
    private val longListener: OnItemLongClickListener
) : ListAdapter<Joke, JokeAdapter.JokeViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val binding = ItemJokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JokeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, holder.itemView.context)
    }

    inner class JokeViewHolder(private val binding: ItemJokeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val joke = getItem(position)
                    longListener.onItemLongClick(joke)
                }
                true
            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val current = getItem(position)
                    if (current.delivery.isNotEmpty()) {
                        binding.delivery.isVisible = !binding.delivery.isVisible
                    }
                }
            }
        }

        fun bind(joke: Joke, context: Context) {
            binding.apply {
                jokeMain.text = joke.jokeText
                if (joke.favorite) {
                    favorite.setImageResource(android.R.drawable.btn_star_big_on)
                } else {
                    favorite.setImageResource(android.R.drawable.btn_star_big_off)
                }
                if (joke.delivery.isNotEmpty()) {
                    root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
                    delivery.text = joke.delivery
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(joke: Joke)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(joke: Joke)
    }

    class DiffCallback : DiffUtil.ItemCallback<Joke>() {
        override fun areItemsTheSame(oldItem: Joke, newItem: Joke): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Joke, newItem: Joke): Boolean = oldItem == newItem
    }
}



