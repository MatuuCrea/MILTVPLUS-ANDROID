package com.milt.miltvplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explicit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.milt.miltvplus.domain.model.AdultItem
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.toPlayableItem
import com.milt.miltvplus.ui.components.PinDialog
import com.milt.miltvplus.ui.state.MainUiState
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.AccentLime
import com.milt.miltvplus.ui.theme.AccentOrange
import com.milt.miltvplus.ui.theme.AccentPurple
import com.milt.miltvplus.ui.theme.AccentRed
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.LiveBadgeRed
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary

data class AdultCategoryInfo(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun AdultScreen(
    uiState: MainUiState,
    onPinDigitEntered: (String) -> Unit,
    onPinDelete: () -> Unit,
    onVerifyPin: () -> Unit,
    onPlayItem: (PlayableItem) -> Unit,
    onFavoriteToggle: (String) -> Unit
) {
    var isPinDialogOpen by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Todos") }

    val isVerified = uiState.pinState.isVerified || !uiState.userSettings.isParentalControlEnabled
    val isLockedOut = uiState.pinState.isLockedOut

    if (!isVerified) {
        // Unverified Gate Screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkNavyBackground)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                if (isLockedOut) AccentRed.copy(alpha = 0.25f) else AccentRed.copy(alpha = 0.15f),
                                CardSurfaceDark
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (isLockedOut) AccentRed else AccentRed.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(32.dp)
            ) {
                // Restricted Banner Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(LiveBadgeRed)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isLockedOut) "🔒 PIN BLOQUEADO POR SEGURIDAD" else "+18 CONTENIDO PARA MAYORES DE 18 AÑOS",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(AccentRed.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLockedOut) Icons.Default.LockClock else Icons.Default.Lock,
                        contentDescription = "Restricted Content",
                        tint = AccentRed,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = if (isLockedOut) "Acceso Bloqueado Temporalmente" else "Acceso Restringido",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isLockedOut)
                        "Has superado el límite de 3 intentos fallidos de PIN."
                    else
                        "Debes confirmar tu PIN de seguridad para acceder a esta sección",
                    color = TextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                if (isLockedOut) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AccentRed.copy(alpha = 0.15f))
                            .border(1.dp, AccentRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Temporizador de desbloqueo",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${uiState.pinState.lockoutRemainingSeconds} segundos",
                            color = AccentRed,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { uiState.pinState.lockoutRemainingSeconds / 30f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = AccentRed,
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Prominent Action Button
                var isButtonFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = { isPinDialogOpen = true },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(54.dp)
                        .onFocusChanged { isButtonFocused = it.isFocused }
                        .focusable(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLockedOut) AccentRed.copy(alpha = 0.5f) else if (isButtonFocused) AccentRed else LiveBadgeRed
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isLockedOut) Icons.Default.LockClock else Icons.Default.Explicit,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isLockedOut) "Bloqueado (${uiState.pinState.lockoutRemainingSeconds}s)" else "Ingresar PIN de 4 dígitos",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Pin Verification Overlay Dialog
        if (isPinDialogOpen) {
            PinDialog(
                enteredPin = uiState.pinState.enteredPin,
                errorMessage = uiState.pinState.errorMessage,
                isLockedOut = uiState.pinState.isLockedOut,
                lockoutRemainingSeconds = uiState.pinState.lockoutRemainingSeconds,
                onDigitEntered = { digit -> onPinDigitEntered(digit) },
                onDeleteDigit = { onPinDelete() },
                onConfirmPin = { onVerifyPin() },
                onDismissRequest = { isPinDialogOpen = false }
            )
        }
    } else {
        // Unlocked Adult Screen Content
        val categories = listOf(
            AdultCategoryInfo("Películas", Icons.Default.Movie, AccentCyan),
            AdultCategoryInfo("Series", Icons.Default.Tv, AccentPurple),
            AdultCategoryInfo("En vivo", Icons.Default.LiveTv, AccentRed),
            AdultCategoryInfo("Modelos", Icons.Default.Person, AccentOrange),
            AdultCategoryInfo("VRS", Icons.Default.VideoLibrary, AccentLime),
            AdultCategoryInfo("Otros", Icons.Default.VideoCameraFront, TextSecondary)
        )

        val filteredItems = if (selectedCategory == "Todos") {
            uiState.adultItems
        } else {
            uiState.adultItems.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkNavyBackground)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Screen Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AccentRed)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+18",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sección de Adultos",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(AccentLime.copy(alpha = 0.2f))
                        .border(1.dp, AccentLime, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✓ Edad Verificada",
                        color = AccentLime,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Neon Categories Cards Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    AdultCategoryChip(
                        name = "Todos",
                        icon = Icons.Default.Explicit,
                        accentColor = MaterialTheme.colorScheme.primary,
                        isSelected = selectedCategory == "Todos",
                        onClick = { selectedCategory = "Todos" }
                    )
                }

                items(categories) { cat ->
                    AdultCategoryChip(
                        name = cat.name,
                        icon = cat.icon,
                        accentColor = cat.color,
                        isSelected = selectedCategory == cat.name,
                        onClick = { selectedCategory = cat.name }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content Grid
            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron títulos en esta categoría",
                        color = TextMuted,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 220.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredItems) { item ->
                        AdultMediaCard(
                            item = item,
                            onClick = { onPlayItem(item.toPlayableItem()) },
                            onFavoriteToggle = { onFavoriteToggle(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdultCategoryChip(
    name: String,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val bgColor = when {
        isFocused -> accentColor
        isSelected -> accentColor.copy(alpha = 0.25f)
        else -> CardSurfaceDark
    }

    val borderColor = when {
        isFocused -> Color.White
        isSelected -> accentColor
        else -> Color.White.copy(alpha = 0.1f)
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = if (isFocused) Color.White else accentColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            color = if (isFocused) Color.White else TextPrimary,
            fontSize = 15.sp,
            fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun AdultMediaCard(
    item: AdultItem,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceDark),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFocused) 12.dp else 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.previewUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dark overlay gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Top Badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentRed)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.category,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (item.isFavorite) AccentRed else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Bottom Content Information
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = AccentOrange,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.rating.toString(),
                            color = AccentOrange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = item.duration,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Play icon overlay when focused
            if (isFocused) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .border(2.dp, AccentCyan, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(AccentCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Reproducir",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
