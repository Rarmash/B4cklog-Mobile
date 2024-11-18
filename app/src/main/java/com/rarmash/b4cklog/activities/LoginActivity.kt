package com.rarmash.b4cklog.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.rarmash.b4cklog.R
import com.rarmash.b4cklog.auth.LoginRequest
import com.rarmash.b4cklog.auth.LoginResponse
import com.rarmash.b4cklog.auth.PrefsManager
import com.rarmash.b4cklog.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: AppCompatButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        progressBar = findViewById(R.id.progressBar)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(username, password)
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        progressBar.visibility = View.VISIBLE
        loginButton.isEnabled = false

        val apiService = RetrofitClient.apiService
        val loginRequest = LoginRequest(username, password)
        val loginCall = apiService.loginUser(loginRequest)

        loginCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                progressBar.visibility = View.GONE
                loginButton.isEnabled = true

                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        PrefsManager.saveTokens(this@LoginActivity, loginResponse.access, loginResponse.refresh)
                        Log.d("LoginActivity", "Login successful, tokens saved")
                        navigateToMainActivity()
                    } ?: run {
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        Log.e("LoginActivity", "Login response is null")
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Login failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                loginButton.isEnabled = true
                Toast.makeText(this@LoginActivity, "Network error. Please try again later.", Toast.LENGTH_SHORT).show()
                Log.e("LoginActivity", "Network error", t)
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
