package com.rarmash.b4cklog.network

import com.rarmash.b4cklog.auth.*
import com.rarmash.b4cklog.models.Game
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/token/")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
    fun authenticate(@Body credentials: LoginRequest): Call<LoginResponse>

    @POST("api/token/refresh/")
    suspend fun refreshToken(@Body refreshRequest: RefreshRequest): LoginResponse

    @GET("api/profile/")
    fun getProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @GET("api/games/?format=json")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 20
    ): Response<GameListResponse>
}