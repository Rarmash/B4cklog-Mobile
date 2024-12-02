package com.rarmash.b4cklog.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rarmash.b4cklog.R
import com.rarmash.b4cklog.auth.PrefsManager
import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var lastSelectedFragmentId: Int = R.id.nav_host_fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= 31) {
            DynamicColors.applyToActivitiesIfAvailable(application)
        }

        super.onCreate(savedInstanceState)

        val accessToken = PrefsManager.getAccessToken(this)
        if (accessToken == null) {
            navigateToLoginActivity()
            return
        } else {
            setContentView(R.layout.activity_main)
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showFragment(R.id.nav_host_fragment_home)
                    true
                }
                R.id.nav_search -> {
                    showFragment(R.id.nav_host_fragment_search)
                    true
                }
                R.id.nav_profile -> {
                    showFragment(R.id.nav_host_fragment_profile)
                    true
                }
                R.id.nav_settings -> {
                    showFragment(R.id.nav_host_fragment_settings)
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.viewTreeObserver.addOnGlobalLayoutListener {
            val bottomNavHeight = bottomNavigationView.height

            updateFragmentBottomMargin(R.id.nav_host_fragment_home, bottomNavHeight)
            updateFragmentBottomMargin(R.id.nav_host_fragment_search, bottomNavHeight)
            updateFragmentBottomMargin(R.id.nav_host_fragment_profile, bottomNavHeight)
            updateFragmentBottomMargin(R.id.nav_host_fragment_settings, bottomNavHeight)
        }
    }

    private fun updateFragmentBottomMargin(fragmentId: Int, bottomNavHeight: Int) {
        val fragmentContainer = findViewById<View>(fragmentId)
        val layoutParams = fragmentContainer.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.bottomMargin = bottomNavHeight
        fragmentContainer.layoutParams = layoutParams
    }

    private fun showFragment(fragmentId: Int) {
        findViewById<View>(R.id.nav_host_fragment_home).visibility = View.GONE
        findViewById<View>(R.id.nav_host_fragment_search).visibility = View.GONE
        findViewById<View>(R.id.nav_host_fragment_profile).visibility = View.GONE
        findViewById<View>(R.id.nav_host_fragment_settings).visibility = View.GONE

        findViewById<View>(fragmentId).visibility = View.VISIBLE

        val navController = when (fragmentId) {
            R.id.nav_host_fragment_home -> runCatching { findNavController(R.id.nav_host_fragment_home) }.getOrNull()
            R.id.nav_host_fragment_search -> runCatching { findNavController(R.id.nav_host_fragment_search) }.getOrNull()
            R.id.nav_host_fragment_profile -> runCatching { findNavController(R.id.nav_host_fragment_profile) }.getOrNull()
            R.id.nav_host_fragment_settings -> runCatching { findNavController(R.id.nav_host_fragment_settings) }.getOrNull()
            else -> null
        }

        if (fragmentId == R.id.nav_host_fragment_home && lastSelectedFragmentId == fragmentId) {
            navController?.popBackStack(navController.graph.startDestinationId, false)
        }
        if (fragmentId == R.id.nav_host_fragment_search && lastSelectedFragmentId == fragmentId) {
            navController?.popBackStack(navController.graph.startDestinationId, false)
        }

        lastSelectedFragmentId = fragmentId
    }

    override fun onBackPressed() {
        val currentNavController = when {
            findViewById<View>(R.id.nav_host_fragment_home).visibility == View.VISIBLE ->
                findNavController(R.id.nav_host_fragment_home)
            findViewById<View>(R.id.nav_host_fragment_search).visibility == View.VISIBLE ->
                findNavController(R.id.nav_host_fragment_search)
            findViewById<View>(R.id.nav_host_fragment_profile).visibility == View.VISIBLE ->
                findNavController(R.id.nav_host_fragment_profile)
            findViewById<View>(R.id.nav_host_fragment_settings).visibility == View.VISIBLE ->
                findNavController(R.id.nav_host_fragment_settings)
            else -> null
        }

        if (currentNavController != null && currentNavController.currentDestination?.id != currentNavController.graph.startDestinationId) {
            currentNavController.popBackStack()
        } else {
            if (lastSelectedFragmentId != R.id.nav_host_fragment_home) {
                showFragment(R.id.nav_host_fragment_home)
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
