package com.milt.miltvplus.data.repository

import com.milt.miltvplus.domain.model.RadioStation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class RadiosRepository {

    private val sampleAudioStreamUrl = "https://stream.zeno.fm/f3wvbbqmdg8uv" // Live stream radio sample

    private val radios = MutableStateFlow(
        listOf(
            RadioStation(
                id = "rad_1",
                name = "Radio Mitre",
                country = "Argentina",
                category = "News & Talk",
                logoUrl = "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=400&auto=format&fit=crop",
                streamUrl = sampleAudioStreamUrl,
                frequency = "AM 790",
                currentShow = "Lanata Sin Filtro",
                isFavorite = true
            ),
            RadioStation(
                id = "rad_2",
                name = "La 100 FM",
                country = "Argentina",
                category = "Pop & Hits",
                logoUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400&auto=format&fit=crop",
                streamUrl = sampleAudioStreamUrl,
                frequency = "FM 99.9",
                currentShow = "El Club del Moro",
                isFavorite = true
            ),
            RadioStation(
                id = "rad_3",
                name = "Aspen 102.3",
                country = "Argentina",
                category = "Classic Hits 80s 90s",
                logoUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=400&auto=format&fit=crop",
                streamUrl = sampleAudioStreamUrl,
                frequency = "FM 102.3",
                currentShow = "Pura Música Aspen",
                isFavorite = false
            ),
            RadioStation(
                id = "rad_4",
                name = "Radio Disney",
                country = "LATAM",
                category = "Youth / Pop",
                logoUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400&auto=format&fit=crop",
                streamUrl = sampleAudioStreamUrl,
                frequency = "FM 94.3",
                currentShow = "El Despertador Disney",
                isFavorite = false
            ),
            RadioStation(
                id = "rad_5",
                name = "Los 40 Principales",
                country = "Spain / LATAM",
                category = "Top 40 World",
                logoUrl = "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=400&auto=format&fit=crop",
                streamUrl = sampleAudioStreamUrl,
                frequency = "FM 105.5",
                currentShow = "Del 40 al 1 Countdown",
                isFavorite = true
            ),
            RadioStation(
                id = "rad_6",
                name = "Radio Continental",
                country = "Argentina",
                category = "Sports & News",
                logoUrl = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=400&auto=format&fit=crop",
                streamUrl = sampleAudioStreamUrl,
                frequency = "AM 590",
                currentShow = "Fútbol Continental",
                isFavorite = false
            )
        )
    )

    fun getRadios(): Flow<List<RadioStation>> = radios

    fun toggleFavorite(id: String) {
        radios.value = radios.value.map { item ->
            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
        }
    }
}
