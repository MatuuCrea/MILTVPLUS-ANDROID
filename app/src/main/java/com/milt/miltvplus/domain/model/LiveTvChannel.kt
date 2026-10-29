package com.milt.miltvplus.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LiveTvChannel(
    val id: String,
    val name: String,
    val logoUrl: String,
    val category: String,
    val country: String,
    val streamUrl: String,
    val epgCurrentProgram: String,
    val epgTime: String,
    val epgNextProgram: String = "",
    val isFavorite: Boolean = false,
    val resolution: String = "1080p HD"
)
