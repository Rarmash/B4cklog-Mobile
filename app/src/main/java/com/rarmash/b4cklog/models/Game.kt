package com.rarmash.b4cklog.models

data class Game(
    val igdb_id: Int,
    val name: String,
    val summary: String,
    val cover: String,
    val first_release_date: String,
    val platforms: List<Platform>
)