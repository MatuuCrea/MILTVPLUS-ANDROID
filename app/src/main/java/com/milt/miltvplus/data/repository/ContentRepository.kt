package com.milt.miltvplus.data.repository

import com.milt.miltvplus.data.remote.tmdb.TmdbApiService
import com.milt.miltvplus.data.remote.tmdb.TmdbRepository
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.toPlayableItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ContentRepository(
    val liveTvRepository: LiveTvRepository = LiveTvRepository(),
    val moviesRepository: MoviesRepository = MoviesRepository(),
    val seriesRepository: SeriesRepository = SeriesRepository(),
    val sportsRepository: SportsRepository = SportsRepository(),
    val radiosRepository: RadiosRepository = RadiosRepository(),
    val musicRepository: MusicRepository = MusicRepository(),
    val theatreRepository: TheatreRepository = TheatreRepository(),
    val adultRepository: AdultRepository = AdultRepository(),
    val userSettingsRepository: UserSettingsRepository = UserSettingsRepository(),
    val tmdbRepository: TmdbRepository = TmdbRepository(
        apiService = TmdbApiService(),
        moviesRepository = moviesRepository,
        seriesRepository = seriesRepository
    )
) {

    fun getAllFavorites(): Flow<List<PlayableItem>> {
        val flow1 = combine(
            liveTvRepository.getChannels(),
            moviesRepository.getMovies(),
            seriesRepository.getSeries(),
            sportsRepository.getSportsEvents()
        ) { channels, movies, series, sports ->
            val list = mutableListOf<PlayableItem>()
            list.addAll(channels.filter { it.isFavorite }.map { it.toPlayableItem() })
            list.addAll(movies.filter { it.isFavorite }.map { it.toPlayableItem() })
            list.addAll(series.filter { it.isFavorite }.map { it.toPlayableItem() })
            list.addAll(sports.filter { it.isFavorite }.map { it.toPlayableItem() })
            list
        }

        val flow2 = combine(
            radiosRepository.getRadios(),
            musicRepository.getPlaylists(),
            theatreRepository.getTheatreItems(),
            adultRepository.getAdultItems()
        ) { radios, music, theatre, adult ->
            val list = mutableListOf<PlayableItem>()
            list.addAll(radios.filter { it.isFavorite }.map { it.toPlayableItem() })
            list.addAll(music.filter { it.isFavorite }.map { it.toPlayableItem() })
            list.addAll(theatre.filter { it.isFavorite }.map { it.toPlayableItem() })
            list.addAll(adult.filter { it.isFavorite }.map { it.toPlayableItem() })
            list
        }

        return combine(flow1, flow2) { part1, part2 -> part1 + part2 }
    }

    fun searchContent(query: String): Flow<List<PlayableItem>> {
        val flow1 = combine(
            liveTvRepository.getChannels(),
            moviesRepository.getMovies(),
            seriesRepository.getSeries(),
            sportsRepository.getSportsEvents()
        ) { channels, movies, series, sports ->
            if (query.isBlank()) {
                emptyList()
            } else {
                val q = query.lowercase().trim()
                val results = mutableListOf<PlayableItem>()

                channels.filter { it.name.lowercase().contains(q) || it.category.lowercase().contains(q) }
                    .forEach { results.add(it.toPlayableItem()) }

                movies.filter { it.title.lowercase().contains(q) || it.genre.lowercase().contains(q) }
                    .forEach { results.add(it.toPlayableItem()) }

                series.filter { it.title.lowercase().contains(q) || it.genre.lowercase().contains(q) }
                    .forEach { results.add(it.toPlayableItem()) }

                sports.filter { it.title.lowercase().contains(q) || it.league.lowercase().contains(q) || it.team1Name.lowercase().contains(q) || it.team2Name.lowercase().contains(q) }
                    .forEach { results.add(it.toPlayableItem()) }

                results
            }
        }

        val flow2 = combine(
            radiosRepository.getRadios(),
            musicRepository.getPlaylists(),
            theatreRepository.getTheatreItems(),
            adultRepository.getAdultItems()
        ) { radios, music, theatre, adult ->
            if (query.isBlank()) {
                emptyList()
            } else {
                val q = query.lowercase().trim()
                val results = mutableListOf<PlayableItem>()

                radios.filter { it.name.lowercase().contains(q) || it.category.lowercase().contains(q) }
                    .forEach { results.add(it.toPlayableItem()) }

                music.filter { it.title.lowercase().contains(q) || it.artistOrDescription.lowercase().contains(q) }
                    .forEach { results.add(it.toPlayableItem()) }

                theatre.filter { it.title.lowercase().contains(q) || it.genre.lowercase().contains(q) }
                    .forEach { results.add(it.toPlayableItem()) }

                adult.filter { it.title.lowercase().contains(q) || it.category.lowercase().contains(q) }
                    .forEach { results.add(it.toPlayableItem()) }

                results
            }
        }

        return combine(flow1, flow2) { part1, part2 -> part1 + part2 }
    }

    fun toggleFavorite(itemId: String) {
        liveTvRepository.toggleFavorite(itemId)
        moviesRepository.toggleFavorite(itemId)
        seriesRepository.toggleFavorite(itemId)
        sportsRepository.toggleFavorite(itemId)
        radiosRepository.toggleFavorite(itemId)
        musicRepository.toggleFavorite(itemId)
        theatreRepository.toggleFavorite(itemId)
        adultRepository.toggleFavorite(itemId)
    }
}
