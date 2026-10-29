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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.milt.miltvplus.domain.model.MusicPlaylist
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.toPlayableItem
import com.milt.miltvplus.ui.components.ActionButton
import com.milt.miltvplus.ui.components.ActionButtonType
import com.milt.miltvplus.ui.components.FilterChipGroup
import com.milt.miltvplus.ui.components.MediaCard
import com.milt.miltvplus.ui.components.SectionHeader
import com.milt.miltvplus.ui.state.MainUiState
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.AccentLime
import com.milt.miltvplus.ui.theme.CardSurfaceBorder
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.PrimaryBlue
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary
import com.milt.miltvplus.ui.theme.dpadFocusable

@Composable
fun MusicScreen(
    uiState: MainUiState,
    onPlayItem: (PlayableItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val sampleStream = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"

    val playlistsList = uiState.musicPlaylists.ifEmpty {
        listOf(
            MusicPlaylist("mus_1", "Éxitos 2024", "Pop & Chart", 50, "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600&auto=format&fit=crop", "Los mejores hits globales", sampleStream, true, true),
            MusicPlaylist("mus_2", "Rock Clásico", "Rock 80s 90s", 65, "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=600&auto=format&fit=crop", "Guns N Roses, Queen, AC/DC, Soda Stereo", sampleStream, true),
            MusicPlaylist("mus_3", "Reggaetón & Urbano", "Latino / Flow", 42, "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&auto=format&fit=crop", "Bad Bunny, Karol G, Feid, Bizarrap", sampleStream, true),
            MusicPlaylist("mus_4", "Lo Mejor del 2024", "Top Global", 38, "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=600&auto=format&fit=crop", "Canciones del año en reproducciones", sampleStream),
            MusicPlaylist("mus_5", "Música Latina", "Pop & Salsa", 45, "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=600&auto=format&fit=crop", "Ritmos latinos para todo momento", sampleStream)
        )
    }

    val musicTabs = listOf("Inicio", "Explorar", "Listas", "Artistas", "Álbumes", "Géneros")
    var selectedTab by remember { mutableStateOf("Inicio") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header & Category Tabs
        Text(
            text = "Centro de Música & Audio",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Sintoniza playlists temáticas, álbumes en alta fidelidad y vincula Spotify",
            color = TextMuted,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        FilterChipGroup(
            options = musicTabs,
            selectedOption = selectedTab,
            onOptionSelected = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Spotify Integration Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .dpadFocusable(shape = RoundedCornerShape(16.dp), onClick = {}),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(Color(0xFF1DB954), PrimaryBlue))
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color(0xFF1DB954), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Spotify",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Integración con Spotify",
                            color = Color(0xFF1DB954),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Spotify - Conecta tu cuenta y disfruta de tu música favorita",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Accede a tus me gusta, listas personalizadas y recomendadas desde MILTVPLUS",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                ActionButton(
                    text = "Conectar Spotify",
                    type = ActionButtonType.ACCENT,
                    onClick = { }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Playlists populares Horizontal List
        SectionHeader(
            title = "Playlists populares",
            subtitle = "Selección de temas con mayor reproducción de la semana",
            accentColor = AccentLime
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(playlistsList) { playlist ->
                MediaCard(
                    title = playlist.title,
                    subtitle = "${playlist.songCount} Temas • ${playlist.category}",
                    posterUrl = playlist.coverUrl,
                    categoryTag = "PLAYLIST",
                    badgeText = "MÚSICA",
                    aspectRatio = 1.0f, // Square 1:1 artwork ratio for music playlists
                    width = 170.dp,
                    isFavorite = playlist.isFavorite,
                    onClick = { onPlayItem(playlist.toPlayableItem()) },
                    onFavoriteToggle = { onFavoriteToggle(playlist.id) }
                )
            }
        }
    }
}
