package com.milt.miltvplus.domain.model

import kotlinx.serialization.Serializable

enum class SportsStatus {
    LIVE,
    UPCOMING,
    ENDED
}

@Serializable
data class SportsEvent(
    val id: String,
    val title: String,
    val league: String,
    val status: SportsStatus,
    val time: String,
    val team1Name: String,
    val team1Flag: String,
    val team2Name: String,
    val team2Flag: String,
    val score: String = "0 - 0",
    val channelName: String = "",
    val streamUrl: String,
    val isFavorite: Boolean = false
)
