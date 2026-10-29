package com.milt.miltvplus.data.repository

import com.milt.miltvplus.domain.model.MusicPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MusicRepository {

    private val sampleAudioStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"

    private val playlists = MutableStateFlow(
        listOf(
            MusicPlaylist(
                id = "mus_1",
                title = "Top Hits 2024",
                category = "Pop & Chart",
                songCount = 50,
                coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600&auto=format&fit=crop",
                artistOrDescription = "The hottest global tracks right now on Spotify & Apple Music",
                streamUrl = sampleAudioStreamUrl,
                isFavorite = true,
                isFeatured = true
            ),
            MusicPlaylist(
                id = "mus_2",
                title = "Latin Pop Fiesta",
                category = "Latin & Reggaeton",
                songCount = 42,
                coverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&auto=format&fit=crop",
                artistOrDescription = "Bad Bunny, Karol G, Feid, Bizarrap & Shakira hits",
                streamUrl = sampleAudioStreamUrl,
                isFavorite = true,
                isFeatured = false
            ),
            MusicPlaylist(
                id = "mus_3",
                title = "Rock Classics 80s & 90s",
                category = "Rock",
                songCount = 65,
                coverUrl = "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=600&auto=format&fit=crop",
                artistOrDescription = "Guns N' Roses, Queen, AC/DC, Nirvana, Soda Stereo",
                streamUrl = sampleAudioStreamUrl,
                isFavorite = false
            ),
            MusicPlaylist(
                id = "mus_4",
                title = "Urban Flow & Trap",
                category = "Hip-Hop / Trap",
                songCount = 38,
                coverUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=600&auto=format&fit=crop",
                artistOrDescription = "Duki, Travis Scott, Drake, Kendrick Lamar & Trueno",
                streamUrl = sampleAudioStreamUrl,
                isFavorite = true
            ),
            MusicPlaylist(
                id = "mus_5",
                title = "Electronic Beats & Chill",
                category = "EDM / House",
                songCount = 30,
                coverUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=600&auto=format&fit=crop",
                artistOrDescription = "David Guetta, Calvin Harris, Tiësto & Martin Garrix",
                streamUrl = sampleAudioStreamUrl,
                isFavorite = false
            ),
            MusicPlaylist(
                id = "mus_6",
                title = "Smooth Jazz Lounge",
                category = "Jazz & Ambient",
                songCount = 35,
                coverUrl = "https://images.unsplash.com/photo-1511192336575-5a79af67a629?w=600&auto=format&fit=crop",
                artistOrDescription = "Relaxing jazz tunes for working, reading and chilling",
                streamUrl = sampleAudioStreamUrl,
                isFavorite = false
            )
        )
    )

    fun getPlaylists(): Flow<List<MusicPlaylist>> = playlists

    fun toggleFavorite(id: String) {
        playlists.value = playlists.value.map { item ->
            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
        }
    }
}
