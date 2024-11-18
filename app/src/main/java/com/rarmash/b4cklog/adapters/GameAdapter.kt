package com.rarmash.b4cklog.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rarmash.b4cklog.R
import com.rarmash.b4cklog.models.Game

class GameAdapter(private val games: List<Game>) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    inner class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.gameTitle)
        val image: ImageView = view.findViewById(R.id.gameImage)
        val summary: TextView = view.findViewById(R.id.gameSummary)
        val platforms: TextView = view.findViewById(R.id.gamePlatforms)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.title.text = game.name
        holder.summary.text = game.summary

        Glide.with(holder.image.context).load(game.cover).into(holder.image)

        holder.platforms.text = game.platforms.joinToString { it.name }
    }

    override fun getItemCount(): Int = games.size
}
