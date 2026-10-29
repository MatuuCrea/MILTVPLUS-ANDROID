package com.milt.miltvplus.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MusicPlaylist(
    val id: String,
    val title: String,
    val category: String,
    val songCount: Int,
    val coverUrl: String,
    val artistOrDescription: String = "",
    val streamUrl: String,
    val isFavorite: Boolean = false,
    val isFeatured: Boolean = false
)
