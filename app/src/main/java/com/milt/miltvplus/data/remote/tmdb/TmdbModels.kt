package com.milt.miltvplus.data.remote.tmdb

import kotlinx.serialization.Serializable

@Serializable
data class TmdbPagedResponse<T>(
    val page: Int = 1,
    val results: List<T> = emptyList(),
    val total_pages: Int = 1,
    val total_results: Int = 0
)

@Serializable
data class TmdbMovieDto(
    val id: Int,
    val title: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val release_date: String? = null,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val popularity: Double = 0.0,
    val adult: Boolean = false,
    val genre_ids: List<Int>? = null
)

@Serializable
data class TmdbTvDto(
    val id: Int,
    val name: String? = null,
    val original_name: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val first_air_date: String? = null,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val popularity: Double = 0.0,
    val genre_ids: List<Int>? = null,
    val number_of_seasons: Int? = null,
    val number_of_episodes: Int? = null
)

@Serializable
data class TmdbDetailDto(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val release_date: String? = null,
    val first_air_date: String? = null,
    val vote_average: Double = 0.0,
    val runtime: Int? = null,
    val number_of_seasons: Int? = null,
    val number_of_episodes: Int? = null,
    val genres: List<TmdbGenreDto>? = null
)

@Serializable
data class TmdbGenreDto(
    val id: Int,
    val name: String
)
