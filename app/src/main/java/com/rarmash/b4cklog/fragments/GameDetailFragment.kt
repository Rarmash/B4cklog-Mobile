package com.rarmash.b4cklog.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.rarmash.b4cklog.R
import com.rarmash.b4cklog.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Response
import com.rarmash.b4cklog.models.Game

class GameDetailFragment : Fragment() {

    private lateinit var gameTitle: TextView
    private lateinit var gameSummary: TextView
    private lateinit var gameCover: ImageView
    private lateinit var gameReleaseDate: TextView
    private lateinit var gamePlatforms: TextView

    private var gameId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameId = arguments?.getInt("gameId")

        gameTitle = view.findViewById(R.id.game_name)
        gameSummary = view.findViewById(R.id.game_summary)
        gameCover = view.findViewById(R.id.game_cover)
        gameReleaseDate = view.findViewById(R.id.game_release_date)
        gamePlatforms = view.findViewById(R.id.game_platforms)

        gameId?.let {
            getGameDetails(it)
        }
    }

    private fun getGameDetails(gameId: Int) {
        lifecycleScope.launch {
            val response: Response<Game> = RetrofitClient.apiService.getGameDetails(gameId)

            if (response.isSuccessful) {
                val gameDetails = response.body()
                if (gameDetails != null) {
                    updateUIWithGameDetails(gameDetails)
                } else {
                    showError("Game details not found.")
                }
            } else {
                showError("Error fetching game details")
            }
        }
    }

    private fun updateUIWithGameDetails(gameDetails: Game) {
        gameTitle.text = gameDetails.name
        gameSummary.text = gameDetails.summary
        gameReleaseDate.text = "Released on " + (gameDetails.first_release_date ?: "Unknown")
        gamePlatforms.text = "Platforms: " + gameDetails.platforms.joinToString(", ") { it.name }

        Glide.with(requireContext())
            .load(gameDetails.cover)
            .placeholder(R.drawable.default_cover)
            .error(R.drawable.default_cover)
            .into(gameCover)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
