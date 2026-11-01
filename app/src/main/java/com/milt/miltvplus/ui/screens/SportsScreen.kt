package com.milt.miltvplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.SportsEvent
import com.milt.miltvplus.domain.model.SportsStatus
import com.milt.miltvplus.domain.model.toPlayableItem
import com.milt.miltvplus.ui.components.ActionButton
import com.milt.miltvplus.ui.components.ActionButtonType
import com.milt.miltvplus.ui.components.FilterChipGroup
import com.milt.miltvplus.ui.state.MainUiState
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.AccentLime
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

@Composable
fun SportsScreen(
    uiState: MainUiState,
    onPlayItem: (PlayableItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sportsList = uiState.sportsEvents.ifEmpty {
        listOf(
            SportsEvent("sp_1", "ARG vs BRA - Eliminatorias CONMEBOL", "CONMEBOL World Cup Qualifiers", SportsStatus.LIVE, "Live 72'", "Argentina", "", "Brasil", "", "1 - 0", "TyC Sports / TV Pública", "", isFavorite = true),
            SportsEvent("sp_2", "Real Madrid vs Barcelona - LaLiga", "LaLiga Santander", SportsStatus.LIVE, "Live 35'", "Real Madrid", "", "FC Barcelona", "", "2 - 1", "ESPN 1 HD", "", isFavorite = true),
            SportsEvent("sp_3", "Boca Juniors vs River Plate - Superclásico", "Liga Profesional Argentina", SportsStatus.UPCOMING, "Hoy 21:00", "Boca Juniors", "", "River Plate", "", "0 - 0", "TNT Sports", ""),
            SportsEvent("sp_4", "UFC 296 - Edwards vs Covington", "UFC World Championship", SportsStatus.UPCOMING, "Mañana 22:00", "Leon Edwards", "", "Colby Covington", "", "0 - 0", "Star+ / Fox Sports", ""),
            SportsEvent("sp_5", "F1 Abu Dhabi Grand Prix", "Formula 1", SportsStatus.ENDED, "Finalizado", "Verstappen", "", "Leclerc", "", "P1: VER", "Fox Sports 1", "", isFavorite = true)
        )
    }

    var selectedTab by remember { mutableStateOf("Todos") }
    var searchQuery by remember { mutableStateOf("") }

    val filterTabs = listOf("Todos", "Fútbol", "Básquet", "Tenis", "Automovilismo", "Otros")

    val filteredEvents = remember(selectedTab, searchQuery, sportsList) {
        sportsList.filter { event ->
            val matchesTab = when (selectedTab) {
                "Fútbol" -> event.league.contains("CONMEBOL", true) || event.league.contains("LaLiga", true) || event.league.contains("Liga", true)
                "Básquet" -> event.league.contains("NBA", true) || event.league.contains("Basketball", true)
                "Tenis" -> event.league.contains("ATP", true) || event.league.contains("Tennis", true)
                "Automovilismo" -> event.league.contains("Formula", true) || event.league.contains("F1", true)
                else -> true
            }

            val matchesSearch = if (searchQuery.isBlank()) true else {
                event.title.contains(searchQuery, true) ||
                event.league.contains(searchQuery, true) ||
                event.team1Name.contains(searchQuery, true) ||
                event.team2Name.contains(searchQuery, true)
            }

            matchesTab && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
    ) {
        // Title Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Deportes en Vivo & Eventos",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Ligas nacionales, torneos internacionales y combates en directo",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar & Filter Tabs Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar evento o equipo...", color = TextMuted, fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = AccentCyan,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier
                    .weight(0.4f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardSurfaceDark,
                    unfocusedContainerColor = CardSurfaceDark,
                    focusedBorderColor = AccentCyan,
                    unfocusedBorderColor = CardSurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )

            Box(modifier = Modifier.weight(0.6f)) {
                FilterChipGroup(
                    options = filterTabs,
                    selectedOption = selectedTab,
                    onOptionSelected = { selectedTab = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live & Upcoming Events List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredEvents) { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dpadFocusable(
                            shape = RoundedCornerShape(14.dp),
                            onClick = { onPlayItem(event.toPlayableItem()) }
                        ),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(CardSurfaceBorder, Color.Transparent))
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Status Badge + League + Teams
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(PrimaryBlue.copy(alpha = 0.2f), CircleShape)
                                    .border(1.dp, PrimaryBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SportsSoccer,
                                    contentDescription = "Sports",
                                    tint = AccentCyan,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val isLive = event.status == SportsStatus.LIVE
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (isLive) LiveBadgeRed else PrimaryBlue,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (isLive) "EN VIVO ${event.time}" else event.time,
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = event.league,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = event.title,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "Canal: ${event.channelName}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Right: Score + Ver Action Button + Star Favorite
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(DarkNavyBackground, RoundedCornerShape(8.dp))
                                    .border(1.dp, CardSurfaceBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = event.score,
                                    color = AccentCyan,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            ActionButton(
                                text = "Ver",
                                icon = Icons.Default.PlayArrow,
                                type = ActionButtonType.ACCENT,
                                onClick = { onPlayItem(event.toPlayableItem()) }
                            )

                            IconButton(
                                onClick = { onFavoriteToggle(event.id) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (event.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "Favorite",
                                    tint = if (event.isFavorite) AccentOrange else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
