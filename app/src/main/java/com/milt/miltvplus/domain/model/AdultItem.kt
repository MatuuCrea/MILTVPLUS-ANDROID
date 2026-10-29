package com.milt.miltvplus.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AdultItem(
    val id: String,
    val title: String,
    val previewUrl: String,
    val category: String,
    val duration: String,
    val rating: Double,
    val streamUrl: String,
    val isFavorite: Boolean = false,
    val isPinProtected: Boolean = true
)
