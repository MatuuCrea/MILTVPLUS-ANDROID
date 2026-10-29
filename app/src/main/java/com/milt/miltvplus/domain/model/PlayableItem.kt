package com.milt.miltvplus.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayableItem(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val description: String = "",
    val streamUrl: String,
    val posterUrl: String = "",
    val backdropUrl: String = "",
    val categoryName: String = "",
    val isLiveStream: Boolean = false
)

fun LiveTvChannel.toPlayableItem() = PlayableItem(
    id = id,
    title = name,
    subtitle = epgCurrentProgram,
    description = "$category • $country • $resolution",
    streamUrl = streamUrl,
    posterUrl = logoUrl,
    categoryName = "Live TV",
    isLiveStream = true
)

fun MediaItem.toPlayableItem() = PlayableItem(
    id = id,
    title = title,
    subtitle = "$releaseYear • $genre • $duration",
    description = overview,
    streamUrl = streamUrl,
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
    categoryName = category.name
)

fun SportsEvent.toPlayableItem() = PlayableItem(
    id = id,
    title = title,
    subtitle = "$team1Name vs $team2Name ($league)",
    description = "Match Status: ${status.name} • Time: $time",
    streamUrl = streamUrl,
    posterUrl = team1Flag,
    categoryName = "Sports",
    isLiveStream = status == SportsStatus.LIVE
)

fun RadioStation.toPlayableItem() = PlayableItem(
    id = id,
    title = name,
    subtitle = currentShow,
    description = "$category • $country ${if (frequency.isNotEmpty()) "• $frequency" else ""}",
    streamUrl = streamUrl,
    posterUrl = logoUrl,
    categoryName = "Radio",
    isLiveStream = true
)

fun MusicPlaylist.toPlayableItem() = PlayableItem(
    id = id,
    title = title,
    subtitle = artistOrDescription,
    description = "$category • $songCount Tracks",
    streamUrl = streamUrl,
    posterUrl = coverUrl,
    categoryName = "Music"
)

fun AdultItem.toPlayableItem() = PlayableItem(
    id = id,
    title = title,
    subtitle = "$category • $duration",
    description = "Rating: ★ $rating",
    streamUrl = streamUrl,
    posterUrl = previewUrl,
    categoryName = "Adult"
)
