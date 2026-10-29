package com.milt.miltvplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.milt.miltvplus.domain.model.LiveTvChannel
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.toPlayableItem
import com.milt.miltvplus.ui.components.ActionButton
import com.milt.miltvplus.ui.components.ActionButtonType
import com.milt.miltvplus.ui.player.MiniPlayerView
import com.milt.miltvplus.ui.state.MainUiState
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.AccentOrange
import com.milt.miltvplus.ui.theme.CardSurfaceBorder
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.LiveBadgeRed
import com.milt.miltvplus.ui.theme.PrimaryBlue
import com.milt.miltvplus.ui.theme.SurfaceDark
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary
import com.milt.miltvplus.ui.theme.dpadFocusable

data class CategoryBreakdown(val name: String, val count: String, val flag: String = "")

@Composable
fun LiveTvScreen(
    uiState: MainUiState,
    onPlayChannel: (PlayableItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val channels = uiState.liveChannels.ifEmpty {
        listOf(
            LiveTvChannel(
                id = "tv_1",
                name = "TyC Sports",
                logoUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=400&auto=format&fit=crop",
                category = "Sports",
                country = "Argentina",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
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
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                epgCurrentProgram = "SportsCenter Nightly",
                epgTime = "20:00 - 21:30",
                epgNextProgram = "UEFA Champions League Highlights",
                resolution = "1080p HD"
            ),
            LiveTvChannel(
                id = "tv_3",
                name = "Telefe HD",
                logoUrl = "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=400&auto=format&fit=crop",
                category = "Entertainment",
                country = "Argentina",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                epgCurrentProgram = "Gran Hermano en Vivo",
                epgTime = "21:45 - 23:30",
                epgNextProgram = "Telefe Noticias Edition",
                resolution = "1080p HD",
                isFavorite = true
            )
        )
    }

    var selectedCategoryFilter by remember { mutableStateOf("Todos los canales") }

    val categoriesList = remember(channels) {
        val list = mutableListOf<CategoryBreakdown>()
        list.add(CategoryBreakdown("Todos los canales", "${channels.size}", "📺"))

        val countries = channels.map { it.country }.filter { it.isNotBlank() && !it.equals("Global", ignoreCase = true) }.distinct().sorted()
        for (country in countries) {
            val count = channels.count { it.country.equals(country, ignoreCase = true) }
            val flag = when {
                country.contains("Argentina", true) -> "🇦🇷"
                country.contains("Brasil", true) || country.contains("Brazil", true) -> "🇧🇷"
                country.contains("USA", true) || country.contains("United States", true) -> "🇺🇸"
                country.contains("Germany", true) -> "🇩🇪"
                country.contains("France", true) -> "🇫🇷"
                country.contains("Austria", true) -> "🇦🇹"
                country.contains("Spain", true) || country.contains("España", true) -> "🇪🇸"
                else -> "🌍"
            }
            list.add(CategoryBreakdown(country, "$count", flag))
        }

        val categories = channels.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()
        for (cat in categories) {
            val count = channels.count { it.category.equals(cat, ignoreCase = true) }
            val flag = when {
                cat.contains("Sport", true) -> "⚽"
                cat.contains("News", true) -> "📰"
                cat.contains("Entertainment", true) -> "🎭"
                cat.contains("Movie", true) || cat.contains("Cine", true) -> "🎬"
                cat.contains("Music", true) -> "🎵"
                cat.contains("Documentary", true) -> "🔬"
                else -> "📺"
            }
            list.add(CategoryBreakdown(cat, "$count", flag))
        }
        list
    }

    val filteredChannels = remember(selectedCategoryFilter, channels) {
        if (selectedCategoryFilter.equals("Todos los canales", ignoreCase = true) || selectedCategoryFilter.equals("All", ignoreCase = true)) {
            channels
        } else {
            channels.filter { channel ->
                channel.category.equals(selectedCategoryFilter, ignoreCase = true) ||
                channel.country.equals(selectedCategoryFilter, ignoreCase = true)
            }
        }
    }

    var selectedChannel by remember(filteredChannels) {
        mutableStateOf(filteredChannels.firstOrNull() ?: channels.firstOrNull() ?: channels[0])
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
    ) {
        if (uiState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = AccentCyan
            )
        }

        if (uiState.errorMessage != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LiveBadgeRed.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aviso: ${uiState.errorMessage}",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                ActionButton(
                    text = "Reintentar",
                    icon = Icons.Default.PlayArrow,
                    type = ActionButtonType.ACCENT,
                    onClick = onRetry,
                    modifier = Modifier.width(140.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val isNarrow = maxWidth < 760.dp

            Row(
                modifier = if (isNarrow) {
                    Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState())
                } else {
                    Modifier.fillMaxSize()
                },
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // PANE 1: Left Categories List
                Card(
                    modifier = if (isNarrow) {
                        Modifier
                            .width(220.dp)
                            .fillMaxHeight()
                    } else {
                        Modifier
                            .weight(0.22f)
                            .fillMaxHeight()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(listOf(CardSurfaceBorder, Color.Transparent))
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Tv,
                                contentDescription = "TV",
                                tint = AccentCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Categorías",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(categoriesList) { cat ->
                                val isSelected = cat.name.equals(selectedCategoryFilter, ignoreCase = true)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (isSelected) PrimaryBlue.copy(alpha = 0.3f) else CardSurfaceDark,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected) AccentCyan else CardSurfaceBorder,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .dpadFocusable(
                                            shape = RoundedCornerShape(10.dp),
                                            onClick = { selectedCategoryFilter = cat.name }
                                        )
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f, fill = false),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = cat.flag, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = cat.name,
                                            color = if (isSelected) Color.White else TextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = cat.count,
                                        color = AccentCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                // PANE 2: Middle Mini-Player Preview Container & EPG Info
                Card(
                    modifier = if (isNarrow) {
                        Modifier
                            .width(360.dp)
                            .fillMaxHeight()
                    } else {
                        Modifier
                            .weight(0.48f)
                            .fillMaxHeight()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(listOf(CardSurfaceBorder, Color.Transparent))
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        MiniPlayerView(
                            item = selectedChannel.toPlayableItem(),
                            onExpandToFullscreen = { onPlayChannel(selectedChannel.toPlayableItem()) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = selectedChannel.name,
                                    color = TextPrimary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${selectedChannel.category} • ${selectedChannel.country}",
                                    color = AccentCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = { onFavoriteToggle(selectedChannel.id) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(CardSurfaceDark, CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (selectedChannel.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "Favorite",
                                    tint = if (selectedChannel.isFavorite) AccentOrange else TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
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
                                    Text(
                                        text = "PROGRAMA ACTUAL",
                                        color = AccentCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = selectedChannel.epgTime,
                                        color = TextMuted,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = selectedChannel.epgCurrentProgram,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                LinearProgressIndicator(
                                    progress = { 0.65f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = AccentCyan,
                                    trackColor = CardSurfaceBorder
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "A continuación: ${selectedChannel.epgNextProgram}",
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ActionButton(
                                text = "Reproducir Pantalla Completa",
                                icon = Icons.Default.PlayArrow,
                                type = ActionButtonType.ACCENT,
                                onClick = { onPlayChannel(selectedChannel.toPlayableItem()) },
                                modifier = Modifier.weight(1f)
                            )
                            ActionButton(
                                text = "Guía de Programación",
                                icon = Icons.Default.List,
                                type = ActionButtonType.SECONDARY,
                                onClick = { },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // PANE 3: Right "Canales destacados" Vertical List
                Card(
                    modifier = if (isNarrow) {
                        Modifier
                            .width(280.dp)
                            .fillMaxHeight()
                    } else {
                        Modifier
                            .weight(0.30f)
                            .fillMaxHeight()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(listOf(CardSurfaceBorder, Color.Transparent))
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Canales destacados",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${filteredChannels.size} canales",
                                color = TextMuted,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredChannels) { channel ->
                                val isCurrent = channel.id == selectedChannel.id

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (isCurrent) PrimaryBlue.copy(alpha = 0.35f) else CardSurfaceDark,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (isCurrent) AccentCyan else CardSurfaceBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .dpadFocusable(
                                            shape = RoundedCornerShape(12.dp),
                                            onClick = { selectedChannel = channel }
                                        )
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f, fill = false),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = channel.logoUrl,
                                            contentDescription = channel.name,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.Black),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f, fill = false)) {
                                            Text(
                                                text = channel.name,
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = channel.epgCurrentProgram,
                                                color = TextSecondary,
                                                fontSize = 10.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(4.dp))

                                    IconButton(
                                        onClick = { onFavoriteToggle(channel.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (channel.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                            contentDescription = "Favorite",
                                            tint = if (channel.isFavorite) AccentOrange else TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
