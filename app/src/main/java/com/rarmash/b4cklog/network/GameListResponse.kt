package com.rarmash.b4cklog.network

import com.rarmash.b4cklog.models.Game

data class GameListResponse(
    val results: List<Game>
)