package com.milt.miltvplus.data.repository

import com.milt.miltvplus.domain.model.AdultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AdultRepository {

    private val sampleStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"

    private val categories = listOf("Todos", "Películas", "Series", "En vivo", "Modelos", "VRS", "Otros")

    private val adultItems = MutableStateFlow(
        listOf(
            AdultItem(
                id = "ad_1",
                title = "Noche de Gala Premium (Película)",
                previewUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600&auto=format&fit=crop",
                category = "Películas",
                duration = "1h 45m",
                rating = 4.9,
                streamUrl = sampleStreamUrl,
                isFavorite = true,
                isPinProtected = true
            ),
            AdultItem(
                id = "ad_2",
                title = "Secretos de Medianoche T1 (Serie)",
                previewUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&auto=format&fit=crop",
                category = "Series",
                duration = "8 Episodios",
                rating = 4.8,
                streamUrl = sampleStreamUrl,
                isFavorite = false,
                isPinProtected = true
            ),
            AdultItem(
                id = "ad_3",
                title = "Transmisión VIP En Vivo 4K",
                previewUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=600&auto=format&fit=crop",
                category = "En vivo",
                duration = "EN VIVO",
                rating = 5.0,
                streamUrl = sampleStreamUrl,
                isFavorite = true,
                isPinProtected = true
            ),
            AdultItem(
                id = "ad_4",
                title = "Pasarela Internacional de Modelos",
                previewUrl = "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=600&auto=format&fit=crop",
                category = "Modelos",
                duration = "52m",
                rating = 4.7,
                streamUrl = sampleStreamUrl,
                isFavorite = false,
                isPinProtected = true
            ),
            AdultItem(
                id = "ad_5",
                title = "Experiencia Inmersiva VR 360°",
                previewUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=600&auto=format&fit=crop",
                category = "VRS",
                duration = "35m VR",
                rating = 4.9,
                streamUrl = sampleStreamUrl,
                isFavorite = false,
                isPinProtected = true
            ),
            AdultItem(
                id = "ad_6",
                title = "Colección Exclusiva Especial",
                previewUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600&auto=format&fit=crop",
                category = "Otros",
                duration = "40m",
                rating = 4.6,
                streamUrl = sampleStreamUrl,
                isFavorite = false,
                isPinProtected = true
            )
        )
    )

    fun getCategories(): List<String> = categories

    fun getAdultItems(): Flow<List<AdultItem>> = adultItems

    fun getAdultItemsByCategory(category: String): Flow<List<AdultItem>> {
        return adultItems.map { list ->
            if (category.equals("Todos", ignoreCase = true) || category.equals("All", ignoreCase = true)) list
            else list.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    fun toggleFavorite(id: String) {
        adultItems.value = adultItems.value.map { item ->
            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
        }
    }
}
