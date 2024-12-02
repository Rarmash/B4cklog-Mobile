package com.rarmash.b4cklog.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rarmash.b4cklog.databinding.ItemGameBinding
import com.rarmash.b4cklog.models.Game
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rarmash.b4cklog.R

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

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
            binding.gameCover.layoutParams = LinearLayout.LayoutParams(120.dp, 180.dp)
            binding.gameCover.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.gameCover.setBackgroundResource(R.drawable.image_border)

            Glide.with(binding.root.context)
                .load(game.cover)
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                // .centerCrop()
                .transform(RoundedCorners(16))
                .into(binding.gameCover)

            binding.root.setOnClickListener {
                onGameClick(game.igdb_id)
            }
        }
    }
}
