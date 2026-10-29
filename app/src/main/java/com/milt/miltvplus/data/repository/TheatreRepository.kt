package com.milt.miltvplus.data.repository

import com.milt.miltvplus.domain.model.MediaCategory
import com.milt.miltvplus.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TheatreRepository {

    private val sampleStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutback2012.mp4"

    private val theatreItems = MutableStateFlow(
        listOf(
            MediaItem(
                id = "th_1",
                title = "Hamilton: The Musical",
                overview = "The real life of one of America's foremost founding fathers and first Secretary of the Treasury, Alexander Hamilton. Captured live on Broadway.",
                genre = "Musical / Drama / History",
                releaseYear = 2020,
                duration = "2h 40m",
                posterUrl = "https://images.unsplash.com/photo-1507676184212-d03ab07a01bf?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1469488865564-c2de10f69f96?w=1200&auto=format&fit=crop",
                category = MediaCategory.THEATRE,
                rating = 8.5,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Lin-Manuel Miranda, Leslie Odom Jr., Phillipa Soo",
                isFavorite = true,
                isFeatured = true
            ),
            MediaItem(
                id = "th_2",
                title = "The Phantom of the Opera - Live at Royal Albert Hall",
                overview = "To celebrate the 25th anniversary of Andrew Lloyd Webber's The Phantom of the Opera, a fully staged production is performed at the Royal Albert Hall.",
                genre = "Musical / Opera / Romance",
                releaseYear = 2011,
                duration = "2h 38m",
                posterUrl = "https://images.unsplash.com/photo-1503095396549-807759245b35?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=1200&auto=format&fit=crop",
                category = MediaCategory.THEATRE,
                rating = 8.8,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Ramin Karimloo, Sierra Boggess, Hadley Fraser",
                isFavorite = true
            ),
            MediaItem(
                id = "th_3",
                title = "Les Misérables: 25th Anniversary Concert",
                overview = "The legendary musical performed live at The O2 in London with an All-Star cast of world renowned performers.",
                genre = "Musical / Epic / Drama",
                releaseYear = 2010,
                duration = "2h 50m",
                posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1507676184212-d03ab07a01bf?w=1200&auto=format&fit=crop",
                category = MediaCategory.THEATRE,
                rating = 8.9,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Alfio Boe, Lea Salonga, Nick Jonas",
                isFavorite = false
            ),
            MediaItem(
                id = "th_4",
                title = "Cirque du Soleil: O",
                overview = "An aquatic tapestry of artistry, surrealism and theatrical romance woven together in a grand stage show.",
                genre = "Performance / Circus / Acrobatics",
                releaseYear = 2022,
                duration = "1h 30m",
                posterUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1503095396549-807759245b35?w=1200&auto=format&fit=crop",
                category = MediaCategory.THEATRE,
                rating = 8.6,
                streamUrl = sampleStreamUrl,
                directorOrCast = "Cirque du Soleil Cast",
                isFavorite = false
            )
        )
    )

    fun getTheatreItems(): Flow<List<MediaItem>> = theatreItems

    fun toggleFavorite(id: String) {
        theatreItems.value = theatreItems.value.map { item ->
            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
        }
    }
}
