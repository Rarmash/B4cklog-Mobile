package com.rarmash.b4cklog.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.rarmash.b4cklog.R
import com.rarmash.b4cklog.activities.LoginActivity
import com.rarmash.b4cklog.auth.PrefsManager

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var logoutButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logoutButton = view.findViewById(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            PrefsManager.clearTokens(requireContext())
            navigateToLoginActivity()
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}