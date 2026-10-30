package com.milt.miltvplus.data.remote.tmdb

import com.milt.miltvplus.data.repository.MoviesRepository
import com.milt.miltvplus.data.repository.SeriesRepository
import com.milt.miltvplus.domain.model.MediaCategory
import com.milt.miltvplus.domain.model.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class TmdbRepository(
    private val apiService: TmdbApiService = TmdbApiService(),
    private val moviesRepository: MoviesRepository = MoviesRepository(),
    private val seriesRepository: SeriesRepository = SeriesRepository()
) {
    private val _tmdbConnected = MutableStateFlow(false)
    val tmdbConnected: StateFlow<Boolean> = _tmdbConnected.asStateFlow()

    private val _isUsingFallback = MutableStateFlow(true)
    val isUsingFallback: StateFlow<Boolean> = _isUsingFallback.asStateFlow()

    suspend fun getTrendingMovies(apiKey: String? = null): List<MediaItem> {
        val result = apiService.getTrendingMovies(apiKey)
        if (result.isSuccess) {
            val dtos = result.getOrNull().orEmpty()
            if (dtos.isNotEmpty()) {
                _tmdbConnected.value = true
                _isUsingFallback.value = false
                return dtos.map { dto ->
                    val year = dto.release_date?.take(4)?.toIntOrNull() ?: 2023
                    MediaItem(
                        id = "tmdb_mov_${dto.id}",
                        title = dto.title ?: "Unknown Movie",
                        overview = dto.overview ?: "No synopsis available.",
                        genre = "TMDB Trending",
                        releaseYear = year,
                        duration = "2h 00m",
                        posterUrl = apiService.getImageUrl(dto.poster_path),
                        backdropUrl = apiService.getBackdropUrl(dto.backdrop_path),
                        category = MediaCategory.MOVIE,
                        rating = dto.vote_average,
                        streamUrl = "",
                        isFavorite = false,
                        isFeatured = false,
                        ageRating = if (dto.adult) "18+" else "13+"
                    )
                }
            }
        }
        // Fallback gracefully to local MoviesRepository
        _tmdbConnected.value = false
        _isUsingFallback.value = true
        return moviesRepository.getMovies().first()
    }

    suspend fun getTrendingTvShows(apiKey: String? = null): List<MediaItem> {
        val result = apiService.getTrendingTvShows(apiKey)
        if (result.isSuccess) {
            val dtos = result.getOrNull().orEmpty()
            if (dtos.isNotEmpty()) {
                _tmdbConnected.value = true
                _isUsingFallback.value = false
                return dtos.map { dto ->
                    val year = dto.first_air_date?.take(4)?.toIntOrNull() ?: 2023
                    MediaItem(
                        id = "tmdb_tv_${dto.id}",
                        title = dto.name ?: "Unknown Series",
                        overview = dto.overview ?: "No synopsis available.",
                        genre = "TMDB Series",
                        releaseYear = year,
                        duration = "1 Season",
                        posterUrl = apiService.getImageUrl(dto.poster_path),
                        backdropUrl = apiService.getBackdropUrl(dto.backdrop_path),
                        category = MediaCategory.SERIES,
                        rating = dto.vote_average,
                        streamUrl = "",
                        seasonsCount = dto.number_of_seasons ?: 1,
                        episodesCount = dto.number_of_episodes ?: 10,
                        isFavorite = false,
                        isFeatured = false,
                        ageRating = "13+"
                    )
                }
            }
        }
        // Fallback gracefully to local SeriesRepository
        _tmdbConnected.value = false
        _isUsingFallback.value = true
        return seriesRepository.getSeries().first()
    }

    suspend fun searchContent(query: String, apiKey: String? = null): List<MediaItem> {
        if (query.isBlank()) return emptyList()
        val moviesResult = apiService.searchMovies(query, apiKey)
        val tvResult = apiService.searchTvShows(query, apiKey)

        val items = mutableListOf<MediaItem>()
        if (moviesResult.isSuccess) {
            moviesResult.getOrNull().orEmpty().forEach { dto ->
                val year = dto.release_date?.take(4)?.toIntOrNull() ?: 2023
                items.add(
                    MediaItem(
                        id = "tmdb_search_mov_${dto.id}",
                        title = dto.title ?: "Unknown",
                        overview = dto.overview ?: "",
                        genre = "Movie Search",
                        releaseYear = year,
                        duration = "2h 00m",
                        posterUrl = apiService.getImageUrl(dto.poster_path),
                        backdropUrl = apiService.getBackdropUrl(dto.backdrop_path),
                        category = MediaCategory.MOVIE,
                        rating = dto.vote_average,
                        streamUrl = ""
                    )
                )
            }
        }
        if (tvResult.isSuccess) {
            tvResult.getOrNull().orEmpty().forEach { dto ->
                val year = dto.first_air_date?.take(4)?.toIntOrNull() ?: 2023
                items.add(
                    MediaItem(
                        id = "tmdb_search_tv_${dto.id}",
                        title = dto.name ?: "Unknown",
                        overview = dto.overview ?: "",
                        genre = "Series Search",
                        releaseYear = year,
                        duration = "1 Season",
                        posterUrl = apiService.getImageUrl(dto.poster_path),
                        backdropUrl = apiService.getBackdropUrl(dto.backdrop_path),
                        category = MediaCategory.SERIES,
                        rating = dto.vote_average,
                        streamUrl = ""
                    )
                )
            }
        }

        if (items.isEmpty()) {
            val localMovies = moviesRepository.getMovies().first().filter { it.title.contains(query, ignoreCase = true) }
            val localSeries = seriesRepository.getSeries().first().filter { it.title.contains(query, ignoreCase = true) }
            items.addAll(localMovies)
            items.addAll(localSeries)
        }

        return items
    }
}
