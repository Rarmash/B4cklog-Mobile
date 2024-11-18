package com.rarmash.b4cklog.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)
        val refreshToken = sharedPreferences.getString("refresh_token", null)

        if (!accessToken.isNullOrEmpty()) {
            val authRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            val response = chain.proceed(authRequest)

            if (response.code == 401 && !refreshToken.isNullOrEmpty()) {
                response.close()

                val refreshJson = JSONObject().put("refresh", refreshToken)
                val requestBody = refreshJson.toString().toRequestBody("application/json".toMediaTypeOrNull())

                val refreshRequest = chain.request()
                    .newBuilder()
                    .url("${RetrofitClient.BASE_URL}api/token/refresh/")
                    .post(requestBody)
                    .header("Content-Type", "application/json")
                    .build()

                val refreshResponse = chain.proceed(refreshRequest)

                return if (refreshResponse.isSuccessful) {
                    val newAccessToken = JSONObject(refreshResponse.body?.string()).getString("access")
                    sharedPreferences.edit().putString("access_token", newAccessToken).apply()

                    val newAuthRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                    chain.proceed(newAuthRequest)
                } else {
                    refreshResponse
                }
            }
            return response
        }
        return chain.proceed(originalRequest)
    }
}
