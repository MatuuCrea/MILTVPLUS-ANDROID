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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.milt.miltvplus.domain.model.MediaItem
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.toPlayableItem
import com.milt.miltvplus.ui.components.ActionButton
import com.milt.miltvplus.ui.components.ActionButtonType
import com.milt.miltvplus.ui.components.MediaCard
import com.milt.miltvplus.ui.components.SectionHeader
import com.milt.miltvplus.ui.state.MainUiState
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.AccentOrange
import com.milt.miltvplus.ui.theme.CardSurfaceBorder
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.LiveBadgeRed
import com.milt.miltvplus.ui.theme.PrimaryBlue
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary
import com.milt.miltvplus.ui.theme.dpadFocusable
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    uiState: MainUiState,
    onPlayItem: (PlayableItem) -> Unit,
    onItemDetails: (PlayableItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // Featured Hero list
    val featuredMovies = uiState.movies.ifEmpty {
        listOf(
            MediaItem(
                id = "mov_1",
                title = "John Wick: Chapter 4",
                overview = "John Wick uncovers a path to defeating The High Table. But before he can earn his freedom, Wick must face off against a new enemy with powerful alliances across the globe.",
                genre = "Acción / Thriller / Crimen",
                releaseYear = 2023,
                duration = "2h 49m",
                posterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop",
                backdropUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&auto=format&fit=crop",
                category = com.milt.miltvplus.domain.model.MediaCategory.MOVIE,
                rating = 8.2,
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                ageRating = "18+"
            )
        )
    }

    var selectedHeroIndex by remember { mutableIntStateOf(0) }
    val currentHeroItem = featuredMovies.getOrElse(selectedHeroIndex) { featuredMovies.first() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        // Spotlight Hero Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(CardSurfaceDark)
        ) {
            // Backdrop Image
            AsyncImage(
                model = currentHeroItem.backdropUrl.ifEmpty { currentHeroItem.posterUrl },
                contentDescription = currentHeroItem.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Multi-side Dark Gradient Overlays
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.4f),
                                DarkNavyBackground.copy(alpha = 0.85f),
                                DarkNavyBackground
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                DarkNavyBackground.copy(alpha = 0.95f),
                                DarkNavyBackground.copy(alpha = 0.6f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Hero Content Info
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(PrimaryBlue, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "SPOTLIGHT DESTACADO",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = currentHeroItem.genre,
                        color = AccentCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentHeroItem.title,
                    color = TextPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Metadata tags row: Year • Duration • Age rating • Rating star
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${currentHeroItem.releaseYear} • ${currentHeroItem.duration}",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (currentHeroItem.ageRating.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .border(1.dp, CardSurfaceBorder, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = currentHeroItem.ageRating,
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = AccentOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentHeroItem.rating.toString(),
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = currentHeroItem.overview,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.75f)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Hero Action Buttons & Indicator Dots Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ActionButton(
                            text = "Reproducir",
                            icon = Icons.Default.PlayArrow,
                            type = ActionButtonType.ACCENT,
                            onClick = { onPlayItem(currentHeroItem.toPlayableItem()) }
                        )
                        ActionButton(
                            text = "Más info",
                            icon = Icons.Default.Info,
                            type = ActionButtonType.SECONDARY,
                            onClick = { onItemDetails(currentHeroItem.toPlayableItem()) }
                        )
                    }

                    // Indicator Dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        featuredMovies.indices.forEach { index ->
                            val isSelected = index == selectedHeroIndex
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 12.dp else 8.dp)
                                    .background(
                                        if (isSelected) AccentCyan else CardSurfaceBorder,
                                        CircleShape
                                    )
                                    .dpadFocusable(
                                        shape = CircleShape,
                                        onClick = { selectedHeroIndex = index }
                                    )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tendencias ahora Carousel
        val tendenciasListState = rememberLazyListState()
        val tendenciasItems = (uiState.movies + uiState.series).ifEmpty {
            listOf(currentHeroItem)
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionHeader(
                title = "Tendencias ahora",
                subtitle = "Las películas y series más vistas en MILTVPLUS",
                accentColor = AccentCyan,
                onScrollLeft = {
                    scope.launch {
                        tendenciasListState.animateScrollToItem(
                            (tendenciasListState.firstVisibleItemIndex - 2).coerceAtLeast(0)
                        )
                    }
                },
                onScrollRight = {
                    scope.launch {
                        tendenciasListState.animateScrollToItem(
                            (tendenciasListState.firstVisibleItemIndex + 2).coerceAtMost(tendenciasItems.size - 1)
                        )
                    }
                }
            )

            LazyRow(
                state = tendenciasListState,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(tendenciasItems) { media ->
                    MediaCard(
                        title = media.title,
                        subtitle = "${media.releaseYear} • ${media.genre.take(18)}",
                        posterUrl = media.posterUrl,
                        categoryTag = media.category.name,
                        badgeText = "★ ${media.rating}",
                        isFavorite = media.isFavorite,
                        onClick = { onItemDetails(media.toPlayableItem()) },
                        onFavoriteToggle = { onFavoriteToggle(media.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Eventos en vivo destacados Row
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionHeader(
                title = "Eventos en vivo destacados",
                subtitle = "Partidos y transmisiones deportivas en directo",
                accentColor = LiveBadgeRed
            )

            val liveEvents = uiState.sportsEvents.ifEmpty {
                emptyList()
            }

            if (liveEvents.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(liveEvents) { sports ->
                        Card(
                            modifier = Modifier
                                .width(280.dp)
                                .dpadFocusable(
                                    shape = RoundedCornerShape(14.dp),
                                    onClick = { onPlayItem(sports.toPlayableItem()) }
                                ),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurfaceDark),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.linearGradient(listOf(CardSurfaceBorder, Color.Transparent))
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(LiveBadgeRed, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = sports.status.name,
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = sports.league,
                                        color = TextMuted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = sports.title,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "⚽", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${sports.team1Name} vs ${sports.team2Name}",
                                            color = TextSecondary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(PrimaryBlue.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = sports.score,
                                            color = AccentCyan,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                ActionButton(
                                    text = "Ver Transmisión",
                                    icon = Icons.Default.PlayArrow,
                                    type = ActionButtonType.ACCENT,
                                    onClick = { onPlayItem(sports.toPlayableItem()) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
