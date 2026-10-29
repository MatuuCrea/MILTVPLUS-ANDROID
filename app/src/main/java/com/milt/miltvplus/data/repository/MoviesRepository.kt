package com.milt.miltvplus.data.repository

import com.milt.miltvplus.domain.model.MediaCategory
import com.milt.miltvplus.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MoviesRepository {

    private val sampleStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"

    private val movies = MutableStateFlow(
        listOf(
            MediaItem(
                id = "mov_1",
                title = "John Wick: Chapter 4",
                overview = "John Wick uncovers a path to defeating The High Table. But before he can earn his freedom, Wick must face off against a new enemy with powerful alliances across the globe and forces that turn old friends into foes.",
                genre = "Action / Thriller / Crime",
                releaseYear = 2023,
                duration = "2h 49m",
                posterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&auto=format&fit=crop",
                category = MediaCategory.MOVIE,
                rating = 8.2,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Keanu Reeves, Donnie Yen, Bill Skarsgård",
                isFavorite = true,
                isFeatured = true,
                ageRating = "18+"
            ),
            MediaItem(
                id = "mov_2",
                title = "Oppenheimer",
                overview = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during World War II.",
                genre = "Biography / Drama / History",
                releaseYear = 2023,
                duration = "3h 00m",
                posterUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop",
                category = MediaCategory.MOVIE,
                rating = 8.9,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Cillian Murphy, Emily Blunt, Matt Damon, Robert Downey Jr.",
                isFavorite = true,
                isFeatured = false,
                ageRating = "16+"
            ),
            MediaItem(
                id = "mov_3",
                title = "Barbie",
                overview = "Barbie and Ken are having the time of their lives in the colorful and seemingly perfect world of Barbie Land. However, when they get a chance to go to the real world, they soon discover the joys and perils of living among humans.",
                genre = "Comedy / Fantasy / Adventure",
                releaseYear = 2023,
                duration = "1h 54m",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1200&auto=format&fit=crop",
                category = MediaCategory.MOVIE,
                rating = 7.3,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Margot Robbie, Ryan Gosling, America Ferrera",
                isFavorite = false,
                isFeatured = false,
                ageRating = "13+"
            ),
            MediaItem(
                id = "mov_4",
                title = "The Batman",
                overview = "When a sadistic serial killer begins murdering key political figures in Gotham, Batman is forced to investigate the city's hidden corruption and question his family's involvement.",
                genre = "Action / Crime / Drama",
                releaseYear = 2022,
                duration = "2h 56m",
                posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop",
                category = MediaCategory.MOVIE,
                rating = 7.8,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Robert Pattinson, Zoë Kravitz, Paul Dano",
                isFavorite = true,
                isFeatured = false,
                ageRating = "16+"
            ),
            MediaItem(
                id = "mov_5",
                title = "Avatar: The Way of Water",
                overview = "Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a familiar threat returns to finish what was previously started, Jake must work with Neytiri and the army of the Na'vi race to protect their home.",
                genre = "Sci-Fi / Action / Adventure",
                releaseYear = 2022,
                duration = "3h 12m",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1200&auto=format&fit=crop",
                category = MediaCategory.MOVIE,
                rating = 7.6,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Sam Worthington, Zoe Saldana, Sigourney Weaver",
                isFavorite = false,
                isFeatured = false
            ),
            MediaItem(
                id = "mov_6",
                title = "Spider-Man: Across the Spider-Verse",
                overview = "Miles Morales catapults across the Multiverse, where he encounters a team of Spider-People charged with protecting its very existence. When the heroes clash on how to handle a new threat, Miles must redefine what it means to be a hero.",
                genre = "Animation / Action / Adventure",
                releaseYear = 2023,
                duration = "2h 20m",
                posterUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&auto=format&fit=crop",
                category = MediaCategory.MOVIE,
                rating = 8.7,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Shameik Moore, Hailee Steinfeld, Oscar Isaac",
                isFavorite = true,
                isFeatured = false
            )
        )
    )

    fun getMovies(): Flow<List<MediaItem>> = movies

    fun getFeaturedMovie(): Flow<MediaItem?> = movies.map { list -> list.firstOrNull { it.isFeatured } ?: list.firstOrNull() }

    fun toggleFavorite(id: String) {
        movies.value = movies.value.map { item ->
            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
        }
    }
}
