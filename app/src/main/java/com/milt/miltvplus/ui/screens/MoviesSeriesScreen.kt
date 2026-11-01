package com.milt.miltvplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milt.miltvplus.domain.model.MediaCategory
import com.milt.miltvplus.domain.model.MediaItem
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.toPlayableItem
import com.milt.miltvplus.ui.components.FilterChipGroup
import com.milt.miltvplus.ui.components.MediaCard
import com.milt.miltvplus.ui.components.SectionHeader
import com.milt.miltvplus.ui.state.MainUiState
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.PrimaryBlue
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary

@Composable
fun MoviesSeriesScreen(
    uiState: MainUiState,
    onPlayItem: (PlayableItem) -> Unit,
    onItemDetails: (PlayableItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val moviesList = uiState.movies.ifEmpty {
        listOf(
            MediaItem("m1", "Oppenheimer", "The story of J. Robert Oppenheimer.", "Drama / Historia", 2023, "3h 00m", "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600&auto=format&fit=crop", "", MediaCategory.MOVIE, 8.9, "", ageRating = "16+"),
            MediaItem("m2", "Barbie", "Barbie and Ken in Barbie Land.", "Comedia / Fantasía", 2023, "1h 54m", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop", "", MediaCategory.MOVIE, 7.3, "", ageRating = "13+"),
            MediaItem("m3", "The Batman", "Gotham serial killer mystery.", "Acción / Crimen", 2022, "2h 56m", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop", "", MediaCategory.MOVIE, 7.8, "", ageRating = "16+"),
            MediaItem("m4", "Top Gun: Maverick", "Naval aviator Pete Mitchell.", "Acción / Drama", 2022, "2h 10m", "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop", "", MediaCategory.MOVIE, 8.3, ""),
            MediaItem("m5", "Avatar: El camino del agua", "Jake Sully and Pandora.", "Sci-Fi / Aventura", 2022, "3h 12m", "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop", "", MediaCategory.MOVIE, 7.6, ""),
            MediaItem("m6", "Interstellar", "Space exploration for humanity.", "Sci-Fi / Drama", 2014, "2h 49m", "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&auto=format&fit=crop", "", MediaCategory.MOVIE, 8.7, "")
        )
    }

    val seriesList = uiState.series.ifEmpty {
        listOf(
            MediaItem("s1", "The Last of Us", "Global post-apocalyptic journey.", "Acción / Drama", 2023, "1 Temporada", "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop", "", MediaCategory.SERIES, 8.8, "", ageRating = "18+"),
            MediaItem("s2", "Breaking Bad", "Chemistry teacher turns meth maker.", "Crimen / Drama", 2008, "5 Temporadas", "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=600&auto=format&fit=crop", "", MediaCategory.SERIES, 9.5, "", ageRating = "18+"),
            MediaItem("s3", "Juego de Tronos", "Nine noble families fight for Westeros.", "Fantasía / Drama", 2011, "8 Temporadas", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop", "", MediaCategory.SERIES, 9.2, ""),
            MediaItem("s4", "Stranger Things", "Hawkins secret experiments and Upside Down.", "Sci-Fi / Terror", 2016, "4 Temporadas", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop", "", MediaCategory.SERIES, 8.7, ""),
            MediaItem("s5", "The Walking Dead", "Zombie apocalypse survivors.", "Terror / Drama", 2010, "11 Temporadas", "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop", "", MediaCategory.SERIES, 8.1, ""),
            MediaItem("s6", "La Casa del Dragón", "Targaryen family civil war.", "Acción / Drama", 2022, "2 Temporadas", "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600&auto=format&fit=crop", "", MediaCategory.SERIES, 8.5, "")
        )
    }

    val filterOptions = listOf("Todos", "Películas", "Series", "Acción", "Drama", "Sci-Fi", "Comedia", "Terror")
    var selectedFilter by remember { mutableStateOf("Todos") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Title & Filter Bar
        Text(
            text = "Catálogo de Películas & Series",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Explora miles de títulos en HD y 4K sin interrupciones",
            color = TextMuted,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Filter chips
        FilterChipGroup(
            options = filterOptions,
            selectedOption = selectedFilter,
            onOptionSelected = { selectedFilter = it }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Películas populares section
        if (selectedFilter in listOf("Todos", "Películas", "Acción", "Drama", "Sci-Fi", "Comedia")) {
            SectionHeader(
                title = "Películas populares",
                subtitle = "Las producciones de cine más aclamadas por el público",
                accentColor = AccentCyan
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(moviesList) { item ->
                    MediaCard(
                        title = item.title,
                        subtitle = "${item.releaseYear} • ${item.genre.take(16)}",
                        posterUrl = item.posterUrl,
                        categoryTag = "PELÍCULA",
                        badgeText = "★ ${item.rating}",
                        isFavorite = item.isFavorite,
                        onClick = { onItemDetails(item.toPlayableItem()) },
                        onFavoriteToggle = { onFavoriteToggle(item.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Series populares section
        if (selectedFilter in listOf("Todos", "Series", "Acción", "Drama", "Sci-Fi", "Terror")) {
            SectionHeader(
                title = "Series populares",
                subtitle = "Temporadas completas y estrenos semanales exclusivos",
                accentColor = PrimaryBlue
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(seriesList) { item ->
                    MediaCard(
                        title = item.title,
                        subtitle = "${item.duration} • ★ ${item.rating}",
                        posterUrl = item.posterUrl,
                        categoryTag = "SERIE",
                        badgeText = "HD",
                        isFavorite = item.isFavorite,
                        onClick = { onItemDetails(item.toPlayableItem()) },
                        onFavoriteToggle = { onFavoriteToggle(item.id) }
                    )
                }
            }
        }
    }
}
