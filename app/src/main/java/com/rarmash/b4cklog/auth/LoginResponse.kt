package com.rarmash.b4cklog.auth

data class LoginResponse(
    val access: String,
    val refresh: String
)