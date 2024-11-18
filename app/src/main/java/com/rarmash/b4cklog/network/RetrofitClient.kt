package com.rarmash.b4cklog.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    const val BASE_URL = "http://10.0.2.2:8000/"

    private lateinit var apiServiceInstance: ApiService

    fun initialize(context: Context) {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(context))
            .build()


        apiServiceInstance = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val apiService: ApiService
        get() {
            if (!RetrofitClient::apiServiceInstance.isInitialized) {
                throw UninitializedPropertyAccessException("com.rarmash.b4cklog.network.RetrofitClient is not initialized. Call initialize() first.")
            }
            return apiServiceInstance
        }
}
