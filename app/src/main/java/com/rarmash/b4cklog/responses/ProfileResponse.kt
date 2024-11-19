package com.rarmash.b4cklog.responses

data class ProfileResponse(
    val user: String,
    val image: String,
    val backlog_want_to_play: List<Int>,
    val backlog_playing: List<Int>,
    val backlog_played: List<Int>,
    val backlog_completed: List<Int>,
    val backlog_completed_100: List<Int>
)