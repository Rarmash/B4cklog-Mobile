package com.rarmash.b4cklog.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.rarmash.b4cklog.R
import com.rarmash.b4cklog.databinding.FragmentHomeBinding
import com.rarmash.b4cklog.models.Game
import com.rarmash.b4cklog.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchGames()
    }

    private fun fetchGames() {
        lifecycleScope.launch {
            try {
                val gamesResponse = RetrofitClient.apiService.getGames(page = 1)
                if (gamesResponse.isSuccessful) {
                    val gamesList = gamesResponse.body()
                    if (!gamesList.isNullOrEmpty()) {
                        Log.d("HomeFragment", "Games Loaded: ${gamesList.size}")
                        displayGames(gamesList)
                    } else {
                        Log.e("HomeFragment", "No games found")
                    }
                } else {
                    Log.e("HomeFragment", "Error: ${gamesResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Exception: ${e.message}")
            }
        }
    }

    private fun displayGames(games: List<Game>) {
        val context = requireContext()

        binding.gamesContainer.removeAllViews()

        for (game in games) {
            val gameCard = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                }
                setPadding(16, 16, 16, 16)
                setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
            }

            val gameTitle = TextView(context).apply {
                text = game.name
                textSize = 18f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
            }

            val gameImage = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    500
                ).apply {
                    setMargins(0, 16, 0, 16)
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(context).load(game.cover).into(this)
            }

            val gameSummary = TextView(context).apply {
                text = game.summary
                textSize = 14f
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                maxLines = 3
                ellipsize = TextUtils.TruncateAt.END
            }

            gameCard.addView(gameTitle)
            gameCard.addView(gameImage)
            gameCard.addView(gameSummary)

            binding.gamesContainer.addView(gameCard)
        }
    }
}
