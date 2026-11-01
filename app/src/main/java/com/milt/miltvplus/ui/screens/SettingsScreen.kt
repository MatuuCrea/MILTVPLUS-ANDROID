package com.milt.miltvplus.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milt.miltvplus.data.supabase.SupabaseClient
import com.milt.miltvplus.domain.model.AccentColor
import com.milt.miltvplus.domain.model.AppBackground
import com.milt.miltvplus.domain.model.AppTheme
import com.milt.miltvplus.domain.model.InterfaceStyle
import com.milt.miltvplus.domain.model.TextSizeSetting
import com.milt.miltvplus.domain.model.UserSettings
import com.milt.miltvplus.ui.components.ChangePinDialog
import com.milt.miltvplus.ui.state.MainUiState
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.AccentLime
import com.milt.miltvplus.ui.theme.AccentOrange
import com.milt.miltvplus.ui.theme.AccentRed
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.SurfaceDark
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary
import com.milt.miltvplus.ui.viewmodel.MainViewModel

enum class SettingsTab(val displayName: String, val icon: ImageVector) {
    ACCOUNT("Cuenta", Icons.Default.AccountCircle),
    GENERAL("General", Icons.Default.Info),
    IMAGE("Imagen", Icons.Default.Hd),
    AUDIO("Audio", Icons.Default.PlayCircle),
    SUBTITLES("Subtítulos", Icons.Default.Language),
    LANGUAGE("Idioma", Icons.Default.Language),
    REGION("Región", Icons.Default.Language),
    APPEARANCE("Apariencia", Icons.Default.Palette),
    PLAYBACK("Reproducción", Icons.Default.PlayCircle),
    CONNECTIVITY("Conectividad", Icons.Default.CloudDone),
    ACCESSIBILITY("Accesibilidad", Icons.Default.Accessibility),
    PRIVACY("Privacidad", Icons.Default.Lock),
    SECURITY("Seguridad (+18)", Icons.Default.Lock),
    NOTIFICATIONS("Notificaciones", Icons.Default.Notifications)
}

@Composable
fun SettingsScreen(
    uiState: MainUiState,
    viewModel: MainViewModel? = null,
    onUpdateTheme: (AppTheme) -> Unit = { viewModel?.updateTheme(it) },
    onUpdateAccentColor: (AccentColor) -> Unit = { viewModel?.updateAccentColor(it) },
    onUpdateBackground: (AppBackground) -> Unit = { viewModel?.updateBackground(it) },
    onUpdateTextSize: (TextSizeSetting) -> Unit = { viewModel?.updateTextSize(it) },
    onUpdateInterfaceStyle: (InterfaceStyle) -> Unit = { viewModel?.updateInterfaceStyle(it) },
    onUpdatePin: (String) -> Unit = { viewModel?.updateParentalPin(it) },
    onToggleParentalControl: (Boolean) -> Unit = { viewModel?.toggleParentalControl(it) },
    onToggleCinematicIntro: (Boolean) -> Unit = { viewModel?.toggleCinematicIntro(it) }
) {
    var selectedTab by remember { mutableStateOf(SettingsTab.APPEARANCE) }
    var isChangePinDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
    ) {
        val isNarrow = maxWidth < 700.dp

        Row(
            modifier = if (isNarrow) {
                Modifier
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState())
            } else {
                Modifier.fillMaxSize()
            }
        ) {
            // Left Settings Navigation Sidebar (14 Categories)
            Column(
                modifier = Modifier
                    .width(if (isNarrow) 220.dp else 250.dp)
                    .fillMaxHeight()
                    .background(SurfaceDark)
                    .border(width = 1.dp, color = Color.White.copy(alpha = 0.08f))
                    .padding(vertical = 16.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = "Ajustes MILTVPLUS",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.08f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(SettingsTab.entries.toTypedArray()) { tab ->
                        SidebarItem(
                            tab = tab,
                            isSelected = selectedTab == tab,
                            onClick = { selectedTab = tab }
                        )
                    }
                }
            }

            // Right Configuration Panel
            Box(
                modifier = if (isNarrow) {
                    Modifier
                        .width(440.dp)
                        .fillMaxHeight()
                        .padding(20.dp)
                } else {
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(24.dp)
                }
            ) {
                val panelScrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(panelScrollState)
                ) {
                    // Header of active panel
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = selectedTab.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = selectedTab.displayName,
                            color = TextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    when (selectedTab) {
                        SettingsTab.ACCOUNT -> {
                            AccountPanel(
                                settings = uiState.userSettings,
                                onUpdateProfile = { name, email -> viewModel?.updateProfileInfo(name, email) },
                                onToggleAuthState = { viewModel?.toggleAuthState(it) }
                            )
                        }

                        SettingsTab.GENERAL -> {
                            DiagnosticsPanel(
                                settings = uiState.userSettings,
                                tmdbConnected = uiState.tmdbConnected,
                                isUsingFallback = uiState.isUsingFallback,
                                onReset = {
                                    viewModel?.resetPreferences()
                                    Toast.makeText(context, "Ajustes restablecidos a valores de fábrica", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        SettingsTab.IMAGE -> {
                            PlaybackPanel(
                                settings = uiState.userSettings,
                                onUpdateQuality = { viewModel?.updateDefaultQuality(it) },
                                onToggleAutoPlay = { viewModel?.toggleAutoPlay(it) },
                                onToggleSubtitles = { viewModel?.toggleSubtitles(it) },
                                onUpdateAspectRatio = { viewModel?.updateAspectRatio(it) }
                            )
                        }

                        SettingsTab.AUDIO -> {
                            PlaybackPanel(
                                settings = uiState.userSettings,
                                onUpdateQuality = { viewModel?.updateDefaultQuality(it) },
                                onToggleAutoPlay = { viewModel?.toggleAutoPlay(it) },
                                onToggleSubtitles = { viewModel?.toggleSubtitles(it) },
                                onUpdateAspectRatio = { viewModel?.updateAspectRatio(it) }
                            )
                        }

                        SettingsTab.SUBTITLES -> {
                            PlaybackPanel(
                                settings = uiState.userSettings,
                                onUpdateQuality = { viewModel?.updateDefaultQuality(it) },
                                onToggleAutoPlay = { viewModel?.toggleAutoPlay(it) },
                                onToggleSubtitles = { viewModel?.toggleSubtitles(it) },
                                onUpdateAspectRatio = { viewModel?.updateAspectRatio(it) }
                            )
                        }

                        SettingsTab.LANGUAGE -> {
                            LanguageRegionPanel(
                                settings = uiState.userSettings,
                                onUpdateLanguage = { viewModel?.updateAppLanguage(it) },
                                onUpdateRegion = { viewModel?.updateContentRegion(it) }
                            )
                        }

                        SettingsTab.REGION -> {
                            LanguageRegionPanel(
                                settings = uiState.userSettings,
                                onUpdateLanguage = { viewModel?.updateAppLanguage(it) },
                                onUpdateRegion = { viewModel?.updateContentRegion(it) }
                            )
                        }

                        SettingsTab.APPEARANCE -> {
                            AppearancePanel(
                                userSettings = uiState.userSettings,
                                onUpdateTheme = onUpdateTheme,
                                onUpdateAccentColor = onUpdateAccentColor,
                                onUpdateBackground = onUpdateBackground,
                                onUpdateTextSize = onUpdateTextSize,
                                onUpdateInterfaceStyle = onUpdateInterfaceStyle,
                                onToggleCinematicIntro = onToggleCinematicIntro
                            )
                        }

                        SettingsTab.PLAYBACK -> {
                            PlaybackPanel(
                                settings = uiState.userSettings,
                                onUpdateQuality = { viewModel?.updateDefaultQuality(it) },
                                onToggleAutoPlay = { viewModel?.toggleAutoPlay(it) },
                                onToggleSubtitles = { viewModel?.toggleSubtitles(it) },
                                onUpdateAspectRatio = { viewModel?.updateAspectRatio(it) }
                            )
                        }

                        SettingsTab.CONNECTIVITY -> {
                            LiveTvSettingsPanel(
                                settings = uiState.userSettings,
                                onUpdatePlaylistUrl = { viewModel?.updatePlaylistUrl(it) },
                                onUpdateAutoUpdateInterval = { viewModel?.updateAutoUpdateInterval(it) },
                                onUpdateDefaultCountry = { viewModel?.updateDefaultCountry(it) },
                                onUpdateDefaultCategory = { viewModel?.updateDefaultLiveCategory(it) }
                            )
                        }

                        SettingsTab.ACCESSIBILITY -> {
                            AccessibilityPanel(
                                settings = uiState.userSettings,
                                onToggleHighContrast = { viewModel?.toggleHighContrast(it) },
                                onUpdateDpadSize = { viewModel?.updateDpadFocusHaloSize(it) }
                            )
                        }

                        SettingsTab.PRIVACY -> {
                            AdultSettingsPanel(
                                userSettings = uiState.userSettings,
                                onToggleParentalControl = onToggleParentalControl,
                                onChangePinClick = { isChangePinDialogOpen = true },
                                onToggleAutoLockOnBackground = { viewModel?.toggleAutoLockOnBackground(it) }
                            )
                        }

                        SettingsTab.SECURITY -> {
                            AdultSettingsPanel(
                                userSettings = uiState.userSettings,
                                onToggleParentalControl = onToggleParentalControl,
                                onChangePinClick = { isChangePinDialogOpen = true },
                                onToggleAutoLockOnBackground = { viewModel?.toggleAutoLockOnBackground(it) }
                            )
                        }

                        SettingsTab.NOTIFICATIONS -> {
                            NotificationsPanel(
                                settings = uiState.userSettings,
                                onToggleSystem = { viewModel?.toggleSystemNotifications(it) },
                                onToggleSports = { viewModel?.toggleLiveSportsAlerts(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (isChangePinDialogOpen) {
        ChangePinDialog(
            currentActualPin = uiState.userSettings.parentalControlPin,
            onPinChanged = { newPin ->
                onUpdatePin(newPin)
                Toast.makeText(context, "PIN actualizado correctamente", Toast.LENGTH_SHORT).show()
            },
            onDismissRequest = { isChangePinDialogOpen = false }
        )
    }
}

@Composable
private fun SidebarItem(
    tab: SettingsTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val bgColor = when {
        isFocused -> MaterialTheme.colorScheme.primary
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        else -> Color.Transparent
    }

    val contentColor = when {
        isFocused -> Color.White
        isSelected -> MaterialTheme.colorScheme.primary
        else -> TextSecondary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.displayName,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = tab.displayName,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

// 1. APARIENCIA PANEL
@Composable
private fun AppearancePanel(
    userSettings: UserSettings,
    onUpdateTheme: (AppTheme) -> Unit,
    onUpdateAccentColor: (AccentColor) -> Unit,
    onUpdateBackground: (AppBackground) -> Unit,
    onUpdateTextSize: (TextSizeSetting) -> Unit,
    onUpdateInterfaceStyle: (InterfaceStyle) -> Unit,
    onToggleCinematicIntro: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Tema de la aplicación") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                AppTheme.entries.forEach { theme ->
                    ChoiceChipCard(
                        title = theme.displayName,
                        isSelected = userSettings.theme == theme,
                        onClick = { onUpdateTheme(theme) },
                        modifier = Modifier.width(130.dp)
                    )
                }
            }
        }

        SettingsSectionCard(title = "Color Principal") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    AccentColor.entries.forEach { accent ->
                        ColorPickerButton(
                            accent = accent,
                            isSelected = userSettings.accentColor == accent,
                            onClick = { onUpdateAccentColor(accent) },
                            modifier = Modifier.width(90.dp)
                        )
                    }
                }
            }
        }

        SettingsSectionCard(title = "Fondo de Pantalla") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                AppBackground.entries.forEach { bg ->
                    BackgroundCard(
                        bg = bg,
                        isSelected = userSettings.background == bg,
                        onClick = { onUpdateBackground(bg) },
                        modifier = Modifier.width(110.dp)
                    )
                }
            }
        }

        SettingsSectionCard(title = "Tamaño de Texto") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                TextSizeSetting.entries.forEach { sizeSetting ->
                    ChoiceChipCard(
                        title = sizeSetting.displayName,
                        isSelected = userSettings.textSize == sizeSetting,
                        onClick = { onUpdateTextSize(sizeSetting) },
                        modifier = Modifier.width(120.dp)
                    )
                }
            }
        }

        SettingsSectionCard(title = "Diseño de Interfaz") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                InterfaceStyle.entries.forEach { style ->
                    ChoiceChipCard(
                        title = style.displayName,
                        isSelected = userSettings.interfaceDesign == style,
                        onClick = { onUpdateInterfaceStyle(style) },
                        modifier = Modifier.width(160.dp)
                    )
                }
            }
        }

        SettingsSectionCard(title = "Animación de Inicio") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Introducción cinemática al iniciar",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Muestra la intro MILTVPLUS al abrir la aplicación",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }

                Switch(
                    checked = userSettings.showCinematicIntro,
                    onCheckedChange = onToggleCinematicIntro,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

// 2. REPRODUCCIÓN PANEL
@Composable
private fun PlaybackPanel(
    settings: UserSettings,
    onUpdateQuality: (String) -> Unit,
    onToggleAutoPlay: (Boolean) -> Unit,
    onToggleSubtitles: (Boolean) -> Unit,
    onUpdateAspectRatio: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Calidad Preferida de Streaming") {
            val qualities = listOf("720p (HD)", "1080p (FullHD)", "2160p (4K UltraHD)")
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                qualities.forEach { q ->
                    val code = q.split(" ").first()
                    ChoiceChipCard(
                        title = q,
                        isSelected = settings.defaultQuality == code,
                        onClick = { onUpdateQuality(code) },
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }

        SettingsSectionCard(title = "Opciones de Reproducción Automática") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Reproducir siguiente episodio", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Inicia automáticamente el próximo capítulo en series", color = TextMuted, fontSize = 13.sp)
                    }
                    Switch(
                        checked = settings.autoPlayNextEpisode,
                        onCheckedChange = onToggleAutoPlay,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
                    )
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Subtítulos activados por defecto", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Muestra subtítulos automáticos en español cuando estén disponibles", color = TextMuted, fontSize = 13.sp)
                    }
                    Switch(
                        checked = settings.subtitlesEnabled,
                        onCheckedChange = onToggleSubtitles,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }

        SettingsSectionCard(title = "Relación de Aspecto") {
            val ratios = listOf("16:9", "4:3", "Ajustar", "Llenar")
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                ratios.forEach { ratio ->
                    ChoiceChipCard(
                        title = ratio,
                        isSelected = settings.aspectRatio == ratio,
                        onClick = { onUpdateAspectRatio(ratio) },
                        modifier = Modifier.width(110.dp)
                    )
                }
            }
        }
    }
}

// 3. TV EN VIVO PANEL
@Composable
private fun LiveTvSettingsPanel(
    settings: UserSettings,
    onUpdatePlaylistUrl: (String) -> Unit,
    onUpdateAutoUpdateInterval: (Int) -> Unit,
    onUpdateDefaultCountry: (String) -> Unit,
    onUpdateDefaultCategory: (String) -> Unit
) {
    var customUrlInput by remember(settings.playlistUrl) { mutableStateOf(settings.playlistUrl) }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Lista de Canales M3U IPTV") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "URL de lista M3U o M3U8 importada:",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                OutlinedTextField(
                    value = customUrlInput,
                    onValueChange = { customUrlInput = it },
                    label = { Text("URL M3U Playlist") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onUpdatePlaylistUrl(customUrlInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sincronizar Lista", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            val defaultUrl = "https://iptv-org.github.io/iptv/index.m3u"
                            customUrlInput = defaultUrl
                            onUpdatePlaylistUrl(defaultUrl)
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cargar Default")
                    }
                }
            }
        }

        SettingsSectionCard(title = "Intervalo de Actualización Automática") {
            val intervals = listOf(6, 12, 24, 48)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                intervals.forEach { hours ->
                    ChoiceChipCard(
                        title = "Cada ${hours}h",
                        isSelected = settings.playlistAutoUpdateIntervalHours == hours,
                        onClick = { onUpdateAutoUpdateInterval(hours) },
                        modifier = Modifier.width(120.dp)
                    )
                }
            }
        }

        SettingsSectionCard(title = "País e Idioma de Canales Predeterminado") {
            val countries = listOf("Argentina", "México", "Colombia", "Chile", "España", "Todos")
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                countries.forEach { c ->
                    ChoiceChipCard(
                        title = c,
                        isSelected = settings.defaultCountry == c,
                        onClick = { onUpdateDefaultCountry(c) },
                        modifier = Modifier.width(110.dp)
                    )
                }
            }
        }
    }
}

// 4. CUENTA Y SESIÓN PANEL
@Composable
private fun AccountPanel(
    settings: UserSettings,
    onUpdateProfile: (String, String) -> Unit,
    onToggleAuthState: (Boolean) -> Unit
) {
    var nameInput by remember(settings.userName) { mutableStateOf(settings.userName) }
    var emailInput by remember(settings.userEmail) { mutableStateOf(settings.userEmail) }
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Perfil de Usuario VIP") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = settings.userName,
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AccentOrange)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("VIP PRO", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(text = settings.userEmail, color = TextMuted, fontSize = 14.sp)
                    Text(
                        text = if (settings.isLoggedIn) "Estado: Sesión Activa (Suscripción ilimitada)" else "Estado: Sin Sesión",
                        color = if (settings.isLoggedIn) AccentLime else AccentRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        SettingsSectionCard(title = "Editar Datos Personales") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Nombre de Usuario") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = Color.White.copy(alpha = 0.2f), focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Correo Electrónico") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = Color.White.copy(alpha = 0.2f), focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onUpdateProfile(nameInput, emailInput)
                            Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Guardar Cambios", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        SettingsSectionCard(title = "Gestión de Sesión") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (settings.isLoggedIn) "Cerrar sesión actual" else "Iniciar sesión VIP",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Desvincula la cuenta de este dispositivo Android",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = {
                        onToggleAuthState(!settings.isLoggedIn)
                        Toast.makeText(context, if (settings.isLoggedIn) "Sesión cerrada" else "Sesión iniciada", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (settings.isLoggedIn) AccentRed else MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (settings.isLoggedIn) "Cerrar Sesión" else "Iniciar Sesión", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 5. ADULTOS +18 PANEL
@Composable
private fun AdultSettingsPanel(
    userSettings: UserSettings,
    onToggleParentalControl: (Boolean) -> Unit,
    onChangePinClick: () -> Unit,
    onToggleAutoLockOnBackground: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Control Parental (+18)") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Bloqueo de contenido para adultos",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Solicita PIN de 4 dígitos para acceder a la sección +18",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }

                    Switch(
                        checked = userSettings.isParentalControlEnabled,
                        onCheckedChange = onToggleParentalControl,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
                    )
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PIN de Seguridad",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Configurado actualmente: ****",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = onChangePinClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cambiar PIN", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        SettingsSectionCard(title = "Reglas de Bloqueo y Seguridad") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Límite de intentos fallidos", color = TextMuted, fontSize = 14.sp)
                    Text("${userSettings.pinMaxAttempts} intentos", color = AccentOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Temporizador de Bloqueo (Cooldown)", color = TextMuted, fontSize = 14.sp)
                    Text("${userSettings.pinLockoutDurationSeconds} segundos con temporizador visual", color = AccentRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Auto-bloqueo al salir", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Solicita PIN de nuevo si la app pasa a segundo plano o cambias de sección", color = TextMuted, fontSize = 13.sp)
                    }

                    Switch(
                        checked = userSettings.autoLockOnBackground,
                        onCheckedChange = onToggleAutoLockOnBackground,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}

// 6. NOTIFICACIONES PANEL
@Composable
private fun NotificationsPanel(
    settings: UserSettings,
    onToggleSystem: (Boolean) -> Unit,
    onToggleSports: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Notificaciones del Sistema") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Alertas de novedades y estrenos", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Notifica cuando hay nuevas películas o temporadas disponibles", color = TextMuted, fontSize = 13.sp)
                    }
                    Switch(
                        checked = settings.systemNotificationsEnabled,
                        onCheckedChange = onToggleSystem,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
                    )
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Alertas de Deportes y Partidos en Vivo", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Avisos 10 minutos antes del inicio de eventos deportivos destacados", color = TextMuted, fontSize = 13.sp)
                    }
                    Switch(
                        checked = settings.liveSportsAlertsEnabled,
                        onCheckedChange = onToggleSports,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}

// 7. IDIOMA Y REGIÓN PANEL
@Composable
private fun LanguageRegionPanel(
    settings: UserSettings,
    onUpdateLanguage: (String) -> Unit,
    onUpdateRegion: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Idioma de la Aplicación") {
            val languages = listOf("Español", "English", "Português")
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                languages.forEach { lang ->
                    ChoiceChipCard(
                        title = lang,
                        isSelected = settings.appLanguage == lang,
                        onClick = { onUpdateLanguage(lang) },
                        modifier = Modifier.width(130.dp)
                    )
                }
            }
        }

        SettingsSectionCard(title = "Región de Contenido") {
            val regions = listOf("Latinoamérica", "España", "Global")
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                regions.forEach { reg ->
                    ChoiceChipCard(
                        title = reg,
                        isSelected = settings.contentRegion == reg,
                        onClick = { onUpdateRegion(reg) },
                        modifier = Modifier.width(140.dp)
                    )
                }
            }
        }
    }
}

// 8. ALMACENAMIENTO PANEL
@Composable
private fun StoragePanel(
    cacheSizeBytes: Long,
    onClearCache: () -> Unit
) {
    val cacheMb = cacheSizeBytes / (1024 * 1024)

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Caché de Aplicación e Imágenes") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Espacio Ocupado por Caché", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("$cacheMb MB de imágenes y miniaturas almacenadas", color = TextMuted, fontSize = 13.sp)
                    }

                    Button(
                        onClick = onClearCache,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Borrar Caché", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 9. ACCESIBILIDAD PANEL
@Composable
private fun AccessibilityPanel(
    settings: UserSettings,
    onToggleHighContrast: (Boolean) -> Unit,
    onUpdateDpadSize: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Modo de Alto Contraste") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Mejorar visibilidad de texto", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Aumenta el contraste de bordes y texto para pantallas de TV", color = TextMuted, fontSize = 13.sp)
                }
                Switch(
                    checked = settings.highContrastMode,
                    onCheckedChange = onToggleHighContrast,
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
                )
            }
        }

        SettingsSectionCard(title = "Tamaño del Halo de Enfoque D-Pad (Control Remoto)") {
            val sizes = listOf("Normal", "Grande", "Extra Grande")
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                sizes.forEach { size ->
                    ChoiceChipCard(
                        title = size,
                        isSelected = settings.dpadFocusHaloSize == size,
                        onClick = { onUpdateDpadSize(size) },
                        modifier = Modifier.width(130.dp)
                    )
                }
            }
        }
    }
}

// 10. DIAGNÓSTICO PANEL
@Composable
private fun DiagnosticsPanel(
    settings: UserSettings,
    tmdbConnected: Boolean,
    isUsingFallback: Boolean,
    onReset: () -> Unit
) {
    val isSupabaseConnected = SupabaseClient.isConfigured()
    val supabaseStatusText = SupabaseClient.getStatusMessage()

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SettingsSectionCard(title = "Estado y Diagnóstico del Sistema") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Versión de la App", color = TextMuted, fontSize = 14.sp)
                    Text("MILTVPLUS v${settings.appVersion} (PRO Build 2025)", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Conexión Supabase Backend", color = TextMuted, fontSize = 14.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isSupabaseConnected) Icons.Default.CloudDone else Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = if (isSupabaseConnected) AccentLime else AccentOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = supabaseStatusText,
                            color = if (isSupabaseConnected) AccentLime else AccentOrange,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Estado TMDB API", color = TextMuted, fontSize = 14.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (tmdbConnected) Icons.Default.CloudDone else Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = if (tmdbConnected) AccentLime else AccentOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (tmdbConnected) "Conectado (Live TMDB)" else "Modo Fallback Local",
                            color = if (tmdbConnected) AccentLime else AccentOrange,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Salud del Servicio IPTV", color = TextMuted, fontSize = 14.sp)
                    Text(settings.serviceHealthStatus, color = AccentLime, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        SettingsSectionCard(title = "Restablecimiento de Fábrica") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Restablecer todos los ajustes", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Restaura las preferencias de apariencia, calidad y PIN predeterminado (0000)", color = TextMuted, fontSize = 13.sp)
                }

                Button(
                    onClick = onReset,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Restablecer", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ColorPickerButton(
    accent: AccentColor,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardSurfaceDark)
            .border(
                width = if (isSelected || isFocused) 2.dp else 1.dp,
                color = if (isSelected || isFocused) Color(accent.colorValue) else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(accent.colorValue)),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = if (accent == AccentColor.LIME || accent == AccentColor.CYAN) Color.Black else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = accent.displayName.split(" ").first(),
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BackgroundCard(
    bg: AppBackground,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardSurfaceDark)
            .border(
                width = if (isSelected || isFocused) 2.dp else 1.dp,
                color = if (isSelected || isFocused) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(bg.colorValue))
                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = bg.displayName,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 6.dp)
        )
    }
}

@Composable
private fun ChoiceChipCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else CardSurfaceDark)
            .border(
                width = if (isSelected || isFocused) 2.dp else 1.dp,
                color = if (isSelected || isFocused) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected || isFocused) TextPrimary else TextSecondary,
            fontSize = 13.sp,
            fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                listOf(Color.White.copy(alpha = 0.12f), Color.Transparent)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}
