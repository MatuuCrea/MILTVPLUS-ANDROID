package com.milt.miltvplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.RadioStation
import com.milt.miltvplus.domain.model.toPlayableItem
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

data class CountryRadioOption(val name: String, val flag: String)

@Composable
fun RadiosScreen(
    uiState: MainUiState,
    onPlayItem: (PlayableItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sampleAudio = "https://stream.zeno.fm/f3wvbbqmdg8uv"

    val radioList = uiState.radioStations.ifEmpty {
        listOf(
            RadioStation("rad_1", "Radio Mitre", "Argentina", "Noticias & Hablada", "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=400&auto=format&fit=crop", sampleAudio, "AM 790", "Lanata Sin Filtro", true),
            RadioStation("rad_2", "La 100 FM", "Argentina", "Pop & Éxitos", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400&auto=format&fit=crop", sampleAudio, "FM 99.9", "El Club del Moro", true),
            RadioStation("rad_3", "Aspen 102.3", "Argentina", "Clásicos 80s 90s", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=400&auto=format&fit=crop", sampleAudio, "FM 102.3", "Pura Música Aspen"),
            RadioStation("rad_4", "Rádio Globo FM", "Brasil", "Pop & Noticias", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400&auto=format&fit=crop", sampleAudio, "FM 98.1", "Manhã da Globo"),
            RadioStation("rad_5", "Jovem Pan FM", "Brasil", "Noticias & Pop", "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=400&auto=format&fit=crop", sampleAudio, "FM 100.9", "Os Pingos nos Is"),
            RadioStation("rad_6", "Antena 1", "Brasil", "Música Internacional", "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=400&auto=format&fit=crop", sampleAudio, "FM 94.7", "Lite Classics"),
            RadioStation("rad_7", "BandNews FM", "Brasil", "Noticias 24h", "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=400&auto=format&fit=crop", sampleAudio, "FM 96.9", "Jornal BandNews"),
            RadioStation("rad_8", "Radio Disney", "EE.UU.", "Pop Juvenil", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400&auto=format&fit=crop", sampleAudio, "FM 94.3", "Top Hits USA")
        )
    }

    val countriesSidebar = listOf(
        CountryRadioOption("Todos", "🌐"),
        CountryRadioOption("Argentina", "🇦🇷"),
        CountryRadioOption("Brasil", "🇧🇷"),
        CountryRadioOption("EE.UU.", "🇺🇸"),
        CountryRadioOption("México", "🇲🇽"),
        CountryRadioOption("España", "🇪🇸"),
        CountryRadioOption("Uruguay", "🇺🇾"),
        CountryRadioOption("Colombia", "🇨🇴"),
        CountryRadioOption("Perú", "🇵🇪"),
        CountryRadioOption("Venezuela", "🇻🇪")
    )

    var selectedCountryFilter by remember { mutableStateOf("Todos") }

    val filteredRadios = remember(selectedCountryFilter, radioList) {
        if (selectedCountryFilter.equals("Todos", ignoreCase = true)) {
            radioList
        } else {
            radioList.filter { it.country.contains(selectedCountryFilter, ignoreCase = true) }
        }
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Country Filter Sidebar (22% width)
        Card(
            modifier = Modifier
                .weight(0.24f)
                .fillMaxHeight(),
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
                        imageVector = Icons.Default.Radio,
                        contentDescription = "Radios",
                        tint = AccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Países",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(countriesSidebar) { countryOption ->
                        val isSelected = countryOption.name.equals(selectedCountryFilter, ignoreCase = true)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) PrimaryBlue.copy(alpha = 0.35f) else CardSurfaceDark,
                                    RoundedCornerShape(10.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) AccentCyan else CardSurfaceBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .dpadFocusable(
                                    shape = RoundedCornerShape(10.dp),
                                    onClick = { selectedCountryFilter = countryOption.name }
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = countryOption.flag, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = countryOption.name,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Stations Cards Grid Container (76% width)
        Column(
            modifier = Modifier
                .weight(0.76f)
                .fillMaxHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Estaciones de Radio en Vivo",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Frecuencias AM, FM y emisoras digitales en streaming de alta definición",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .background(LiveBadgeRed, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "TRANSMISIÓN EN DIRECTO",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Station Cards Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 220.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredRadios) { station ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .dpadFocusable(
                                shape = RoundedCornerShape(14.dp),
                                onClick = { onPlayItem(station.toPlayableItem()) }
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
                                AsyncImage(
                                    model = station.logoUrl,
                                    contentDescription = station.name,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.Black),
                                    contentScale = ContentScale.Crop
                                )

                                IconButton(
                                    onClick = { onFavoriteToggle(station.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (station.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                        contentDescription = "Favorite",
                                        tint = if (station.isFavorite) AccentOrange else TextMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = station.name,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "${station.frequency} • ${station.category}",
                                color = AccentCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = station.currentShow,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Play Live Button
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(PrimaryBlue, RoundedCornerShape(8.dp))
                                    .clickable { onPlayItem(station.toPlayableItem()) }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Escuchar",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Escuchar en Vivo",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
