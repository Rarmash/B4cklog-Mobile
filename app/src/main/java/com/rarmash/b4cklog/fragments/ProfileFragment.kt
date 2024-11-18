package com.rarmash.b4cklog.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.rarmash.b4cklog.R
import com.rarmash.b4cklog.auth.PrefsManager
import com.rarmash.b4cklog.databinding.FragmentProfileBinding
import com.rarmash.b4cklog.network.ProfileResponse
import com.rarmash.b4cklog.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var backlogWantToPlayTextView: TextView
    private lateinit var backlogPlayingTextView: TextView
    private lateinit var backlogPlayedTextView: TextView
    private lateinit var backlogCompletedTextView: TextView
    private lateinit var backlogCompleted100TextView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        profileImageView = binding.profileImageView
        usernameTextView = binding.usernameTextView
        backlogWantToPlayTextView = binding.backlogWantToPlayTextView
        backlogPlayingTextView = binding.backlogPlayingTextView
        backlogPlayedTextView = binding.backlogPlayedTextView
        backlogCompletedTextView = binding.backlogCompletedTextView
        backlogCompleted100TextView = binding.backlogCompleted100TextView
        progressBar = binding.progressBar

        val token = "Bearer ${PrefsManager.getAccessToken(requireContext())}"

        getProfileData(token)

        return binding.root
    }

    private fun getProfileData(token: String) {
        progressBar.visibility = View.VISIBLE

        val apiService = RetrofitClient.apiService
        val call = apiService.getProfile(token)

        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val profile = response.body()

                    profile?.let {
                        usernameTextView.text = it.user
                        Glide.with(this@ProfileFragment)
                            .load(it.image)
                            .into(profileImageView)

                        backlogWantToPlayTextView.text = it.backlog_want_to_play.joinToString(", ")
                        backlogPlayingTextView.text = it.backlog_playing.joinToString(", ")
                        backlogPlayedTextView.text = it.backlog_played.joinToString(", ")
                        backlogCompletedTextView.text = it.backlog_completed.joinToString(", ")
                        backlogCompleted100TextView.text = it.backlog_completed_100.joinToString(", ")
                    }
                } else {
                    Toast.makeText(context, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
