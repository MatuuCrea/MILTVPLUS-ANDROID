package com.milt.miltvplus.domain.model

import kotlinx.serialization.Serializable

enum class MediaCategory {
    MOVIE,
    SERIES,
    THEATRE
}

@Serializable
data class MediaItem(
    val id: String,
    val title: String,
    val overview: String,
    val genre: String,
    val releaseYear: Int,
    val duration: String,
    val posterUrl: String,
    val backdropUrl: String,
    val category: MediaCategory,
    val rating: Double,
    val streamUrl: String,
    val seasonsCount: Int? = null,
    val episodesCount: Int? = null,
    val directorOrCast: String = "",
    val isFavorite: Boolean = false,
    val isFeatured: Boolean = false,
    val ageRating: String = "13+"
)
