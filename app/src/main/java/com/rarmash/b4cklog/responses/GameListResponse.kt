package com.rarmash.b4cklog.responses

import com.rarmash.b4cklog.models.Game

data class GameListResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Game>
)
