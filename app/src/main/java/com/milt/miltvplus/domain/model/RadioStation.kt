package com.milt.miltvplus.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RadioStation(
    val id: String,
    val name: String,
    val country: String,
    val category: String,
    val logoUrl: String,
    val streamUrl: String,
    val frequency: String = "",
    val currentShow: String = "Live Station Broadcast",
    val isFavorite: Boolean = false
)
