package com.rarmash.b4cklog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rarmash.b4cklog.databinding.ItemGameBinding
import com.rarmash.b4cklog.models.Game
import androidx.recyclerview.widget.DiffUtil
import com.rarmash.b4cklog.R

object GameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.igdb_id == newItem.igdb_id
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}

class GameAdapter(
    private val onGameClick: (Int) -> Unit
) : PagingDataAdapter<Game, GameAdapter.GameViewHolder>(GameDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = getItem(position)
        if (game != null) {
            holder.bind(game)
        }
    }

    inner class GameViewHolder(private val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game) {
            Glide.with(binding.root.context)
                .load(game.cover)
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .into(binding.gameImage)

            binding.root.setOnClickListener {
                onGameClick(game.igdb_id)
            }
        }
    }
}
