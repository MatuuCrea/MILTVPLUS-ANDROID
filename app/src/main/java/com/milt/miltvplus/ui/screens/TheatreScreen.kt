package com.milt.miltvplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.milt.miltvplus.ui.theme.AccentOrange
import com.milt.miltvplus.ui.theme.AccentPurple
import com.milt.miltvplus.ui.theme.CardSurfaceBorder
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary
import com.milt.miltvplus.ui.theme.dpadFocusable

data class CountryBadge(val country: String, val flag: String)

@Composable
fun TheatreScreen(
    uiState: MainUiState,
    onPlayItem: (PlayableItem) -> Unit,
    onItemDetails: (PlayableItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val sampleStream = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutback2012.mp4"

    val theatreList = uiState.theatreItems.ifEmpty {
        listOf(
            MediaItem("th_1", "Hamilton: The Musical", "Alexander Hamilton story on Broadway.", "Musical / Drama", 2020, "2h 40m", "https://images.unsplash.com/photo-1507676184212-d03ab07a01bf?w=600&auto=format&fit=crop", "", MediaCategory.THEATRE, 8.5, sampleStream, isFeatured = true),
            MediaItem("th_2", "El Rey León", "Classic Broadway production.", "Musical / Drama", 2021, "2h 20m", "https://images.unsplash.com/photo-1503095396549-807759245b35?w=600&auto=format&fit=crop", "", MediaCategory.THEATRE, 8.9, sampleStream),
            MediaItem("th_3", "La Bella y la Bestia", "Live musical spectacle.", "Comedia / Fantasía", 2019, "2h 15m", "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop", "", MediaCategory.THEATRE, 8.6, sampleStream),
            MediaItem("th_4", "Romeo y Julieta", "Classic theatrical tragedy.", "Clásicos / Drama", 2022, "2h 10m", "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=600&auto=format&fit=crop", "", MediaCategory.THEATRE, 8.4, sampleStream),
            MediaItem("th_5", "Matilda: El Musical", "RSC Matilda live show.", "Musical / Comedia", 2022, "2h 18m", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&auto=format&fit=crop", "", MediaCategory.THEATRE, 8.7, sampleStream),
            MediaItem("th_6", "El Fantasma de la Ópera", "Royal Albert Hall 25th Anniversary.", "Musical / Ópera", 2011, "2h 38m", "https://images.unsplash.com/photo-1469488865564-c2de10f69f96?w=600&auto=format&fit=crop", "", MediaCategory.THEATRE, 8.8, sampleStream)
        )
    }

    val filterTabs = listOf("Todos", "Obras", "Musicales", "Comedias", "Clásicos", "Países")
    var selectedTab by remember { mutableStateOf("Todos") }

    val countryBadges = listOf(
        CountryBadge("Argentina", "🇦🇷"),
        CountryBadge("EE.UU. (Broadway)", "🇺🇸"),
        CountryBadge("Reino Unido (West End)", "🇬🇧"),
        CountryBadge("España", "🇪🇸"),
        CountryBadge("Francia", "🇫🇷")
    )
    var selectedCountryBadge by remember { mutableStateOf("Argentina") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Teatro & Artes Escénicas",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Obras destacadas de Broadway, West End y producciones dramáticas nacionales",
            color = TextMuted,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Filter tabs
        FilterChipGroup(
            options = filterTabs,
            selectedOption = selectedTab,
            onOptionSelected = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Por país selector bar with flag badges
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Por país de origen",
                color = AccentOrange,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(countryBadges) { badge ->
                    val isSelected = badge.country.equals(selectedCountryBadge, ignoreCase = true)

                    Row(
                        modifier = Modifier
                            .background(
                                if (isSelected) AccentPurple.copy(alpha = 0.35f) else CardSurfaceDark,
                                RoundedCornerShape(20.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) AccentOrange else CardSurfaceBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .dpadFocusable(
                                shape = RoundedCornerShape(20.dp),
                                onClick = { selectedCountryBadge = badge.country }
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = badge.flag, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = badge.country,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Obras destacadas
        SectionHeader(
            title = "Obras destacadas",
            subtitle = "Producciones galardonadas registradas en vivo en alta calidad",
            accentColor = AccentPurple
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(theatreList) { item ->
                MediaCard(
                    title = item.title,
                    subtitle = "${item.releaseYear} • ${item.genre.take(18)}",
                    posterUrl = item.posterUrl,
                    categoryTag = "TEATRO",
                    badgeText = "★ ${item.rating}",
                    isFavorite = item.isFavorite,
                    onClick = { onItemDetails(item.toPlayableItem()) },
                    onFavoriteToggle = { onFavoriteToggle(item.id) }
                )
            }
        }
    }
}
