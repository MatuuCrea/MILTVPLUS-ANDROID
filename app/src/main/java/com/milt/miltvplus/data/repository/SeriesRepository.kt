package com.milt.miltvplus.data.repository

import com.milt.miltvplus.domain.model.MediaCategory
import com.milt.miltvplus.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SeriesRepository {

    private val series = MutableStateFlow(
        listOf(
            MediaItem(
                id = "ser_1",
                title = "Breaking Bad",
                overview = "A chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine with a former student in order to secure his family's financial future.",
                genre = "Crime / Drama / Thriller",
                releaseYear = 2008,
                duration = "5 Seasons",
                posterUrl = "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop",
                category = MediaCategory.SERIES,
                rating = 9.5,
                streamUrl = "", // Stream de reproducción legítimo no configurado en demo
                seasonsCount = 5,
                episodesCount = 62,
                directorOrCast = "Bryan Cranston, Aaron Paul, Anna Gunn",
                isFavorite = true,
                isFeatured = true,
                ageRating = "18+"
            ),
            MediaItem(
                id = "ser_2",
                title = "Stranger Things",
                overview = "When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces and one strange little girl.",
                genre = "Sci-Fi / Horror / Drama",
                releaseYear = 2016,
                duration = "4 Seasons",
                posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&auto=format&fit=crop",
                category = MediaCategory.SERIES,
                rating = 8.7,
                streamUrl = "",
                seasonsCount = 4,
                episodesCount = 34,
                directorOrCast = "Millie Bobby Brown, Finn Wolfhard, Winona Ryder",
                isFavorite = true
            ),
            MediaItem(
                id = "ser_3",
                title = "The Last of Us",
                overview = "After a global pandemic destroys civilization, a hardened survivor takes charge of a 14-year-old girl who may be humanity's last hope.",
                genre = "Action / Adventure / Drama",
                releaseYear = 2023,
                duration = "1 Season",
                posterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=1200&auto=format&fit=crop",
                category = MediaCategory.SERIES,
                rating = 8.8,
                streamUrl = "",
                seasonsCount = 1,
                episodesCount = 9,
                directorOrCast = "Pedro Pascal, Bella Ramsey",
                isFavorite = false
            ),
            MediaItem(
                id = "ser_4",
                title = "House of the Dragon",
                overview = "An internal conflict within the House Targaryen at the height of its power, 172 years before the birth of Daenerys Targaryen.",
                genre = "Action / Adventure / Drama",
                releaseYear = 2022,
                duration = "2 Seasons",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop",
                category = MediaCategory.SERIES,
                rating = 8.5,
                streamUrl = "",
                seasonsCount = 2,
                episodesCount = 18,
                directorOrCast = "Emma D'Arcy, Matt Smith, Olivia Cooke",
                isFavorite = true
            )
        )
    )

    fun getSeries(): Flow<List<MediaItem>> = series

    fun toggleFavorite(id: String) {
        series.value = series.value.map { item ->
            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
        }
    }
}
