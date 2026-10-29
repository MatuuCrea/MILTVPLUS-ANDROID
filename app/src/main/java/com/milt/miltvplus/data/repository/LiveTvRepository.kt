package com.milt.miltvplus.data.repository

import android.content.Context
import com.milt.miltvplus.domain.model.LiveTvChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LiveTvRepository {

    private val sampleStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    private val curatedChannels = listOf(
        LiveTvChannel(
            id = "tv_1",
            name = "TyC Sports",
            logoUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=400&auto=format&fit=crop",
            category = "Sports",
            country = "Argentina",
            streamUrl = sampleStreamUrl,
            epgCurrentProgram = "ARG vs BRA - Pre-Match Show",
            epgTime = "19:30 - 21:00",
            epgNextProgram = "Eliminatorias Sudamericanas LIVE",
            resolution = "1080p 60fps",
            isFavorite = true
        ),
        LiveTvChannel(
            id = "tv_2",
            name = "ESPN HD",
            logoUrl = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=400&auto=format&fit=crop",
            category = "Sports",
            country = "USA",
            streamUrl = sampleStreamUrl,
            epgCurrentProgram = "SportsCenter Nightly",
            epgTime = "20:00 - 21:30",
            epgNextProgram = "UEFA Champions League Highlights",
            resolution = "1080p HD",
            isFavorite = false
        ),
        LiveTvChannel(
            id = "tv_3",
            name = "Telefe HD",
            logoUrl = "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=400&auto=format&fit=crop",
            category = "Entertainment",
            country = "Argentina",
            streamUrl = sampleStreamUrl,
            epgCurrentProgram = "Gran Hermano en Vivo",
            epgTime = "21:45 - 23:30",
            epgNextProgram = "Telefe Noticias Edition",
            resolution = "1080p HD",
            isFavorite = true
        ),
        LiveTvChannel(
            id = "tv_4",
            name = "El Trece",
            logoUrl = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=400&auto=format&fit=crop",
            category = "General",
            country = "Argentina",
            streamUrl = sampleStreamUrl,
            epgCurrentProgram = "Telenoche en Vivo",
            epgTime = "20:00 - 21:30",
            epgNextProgram = "Los 8 Escalones del Millón",
            resolution = "1080p HD"
        ),
        LiveTvChannel(
            id = "tv_5",
            name = "CNN en Español",
            logoUrl = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=400&auto=format&fit=crop",
            category = "News",
            country = "USA / LATAM",
            streamUrl = sampleStreamUrl,
            epgCurrentProgram = "Conclusiones con Fernando del Rincón",
            epgTime = "21:00 - 22:00",
            epgNextProgram = "CNN Dinero",
            resolution = "720p HD"
        ),
        LiveTvChannel(
            id = "tv_6",
            name = "HBO Live HD",
            logoUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=400&auto=format&fit=crop",
            category = "Movies",
            country = "Global",
            streamUrl = sampleStreamUrl,
            epgCurrentProgram = "Dune: Part Two (Movie Premiere)",
            epgTime = "20:00 - 22:45",
            epgNextProgram = "House of the Dragon S2E1",
            resolution = "4K UHD"
        ),
        LiveTvChannel(
            id = "tv_7",
            name = "Star Channel",
            logoUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=400&auto=format&fit=crop",
            category = "Series",
            country = "Global",
            streamUrl = sampleStreamUrl,
            epgCurrentProgram = "The Simpsons Marathon",
            epgTime = "18:00 - 22:00",
            epgNextProgram = "Futurama New Season",
            resolution = "1080p HD"
        ),
        LiveTvChannel(
            id = "tv_8",
            name = "Discovery Channel",
            logoUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400&auto=format&fit=crop",
            category = "Documentary",
            country = "Global",
            streamUrl = sampleStreamUrl,
            epgCurrentProgram = "MythBusters & Science Wonders",
            epgTime = "20:00 - 21:00",
            epgNextProgram = "Gold Rush Alaska",
            resolution = "1080p HD"
        )
    )

    private val parsedM3uChannels = M3uPlaylistParser.parseM3u(M3uPlaylistParser.defaultM3uPlaylist)

    private val channels = MutableStateFlow(curatedChannels + parsedM3uChannels)
    private var cachedCustomChannels = listOf<LiveTvChannel>()

    private val _syncMetrics = MutableStateFlow<SyncMetrics?>(null)
    val syncMetrics: StateFlow<SyncMetrics?> = _syncMetrics.asStateFlow()

    fun getChannels(): Flow<List<LiveTvChannel>> = channels

    fun getChannelsByCategory(category: String): Flow<List<LiveTvChannel>> {
        return channels.map { list ->
            if (category.equals("All", ignoreCase = true) || category.equals("Todos los canales", ignoreCase = true)) list
            else list.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    fun getCategories(): Flow<List<String>> {
        return channels.map { list ->
            listOf("All") + list.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()
        }
    }

    fun getCountries(): Flow<List<String>> {
        return channels.map { list ->
            listOf("All") + list.map { it.country }.filter { it.isNotBlank() }.distinct().sorted()
        }
    }

    fun getChannelsByCategoryAndCountry(category: String, country: String): Flow<List<LiveTvChannel>> {
        return channels.map { list ->
            list.filter { channel ->
                val catMatch = category.equals("All", ignoreCase = true) || category.equals("Todos los canales", ignoreCase = true) || channel.category.equals(category, ignoreCase = true)
                val countryMatch = country.equals("All", ignoreCase = true) || channel.country.contains(country, ignoreCase = true)
                catMatch && countryMatch
            }
        }
    }

    fun loadCustomM3uPlaylist(m3uContent: String) {
        try {
            val customParsed = M3uPlaylistParser.parseM3u(m3uContent)
            if (customParsed.isNotEmpty()) {
                cachedCustomChannels = customParsed
                channels.value = curatedChannels + cachedCustomChannels
            }
        } catch (e: Exception) {
            if (cachedCustomChannels.isNotEmpty()) {
                channels.value = curatedChannels + cachedCustomChannels
            }
        }
    }

    suspend fun fetchAndLoadRemotePlaylist(url: String): Result<Int> {
        val syncResult = syncRemotePlaylistUrl(url, null)
        return if (syncResult.isSuccess) {
            Result.success(syncResult.getOrNull()?.imported ?: 0)
        } else {
            Result.failure(syncResult.exceptionOrNull() ?: Exception("Failed to fetch remote playlist"))
        }
    }

    suspend fun syncRemotePlaylistUrl(
        url: String = "https://iptv-org.github.io/iptv/index.m3u",
        context: Context? = null
    ): Result<SyncMetrics> {
        return withContext(Dispatchers.IO) {
            val service = PlaylistSyncService(context)
            val (remoteParsed, metrics) = service.syncPlaylist(url)
            _syncMetrics.value = metrics

            if (remoteParsed.isNotEmpty()) {
                cachedCustomChannels = remoteParsed
                channels.value = curatedChannels + cachedCustomChannels
            }

            if (metrics.success) {
                Result.success(metrics)
            } else {
                Result.failure(Exception(metrics.errorMessage ?: "Remote playlist synchronization failed"))
            }
        }
    }

    fun toggleFavorite(id: String) {
        channels.value = channels.value.map { channel ->
            if (channel.id == id) channel.copy(isFavorite = !channel.isFavorite) else channel
        }
    }
}
