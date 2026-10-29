package com.milt.miltvplus.domain.model

import kotlinx.serialization.Serializable

enum class AppTheme(val displayName: String) {
    DARK("Oscuro"),
    LIGHT("Claro"),
    SYSTEM("Automático")
}

enum class AccentColor(val displayName: String, val hex: String, val colorValue: Long) {
    BLUE("Azul Predeterminado", "#1E88E5", 0xFF1E88E5),
    NAVY("Marina Oscuro", "#0B192C", 0xFF0B192C),
    LIME("Lima Neón", "#76FF03", 0xFF76FF03),
    RED("Rojo Vivo", "#FF5252", 0xFFFF5252),
    CYAN("Cian Neón", "#00E5FF", 0xFF00E5FF),
    ORANGE("Naranja Fuego", "#FF9100", 0xFFFF9100)
}

enum class AppBackground(val displayName: String, val hex: String, val colorValue: Long) {
    AMOLED_BLACK("Negro AMOLED", "#000000", 0xFF000000),
    DARK_GRAY("Gris Oscuro", "#121212", 0xFF121212),
    DEEP_BLUE("Azul Profundo", "#0B192C", 0xFF0B192C),
    MIDNIGHT("Medianoche", "#050814", 0xFF050814)
}

enum class TextSizeSetting(val displayName: String, val scale: Float) {
    MEDIUM("Normal", 1.0f),
    LARGE("Grande", 1.15f),
    EXTRA_LARGE("Extra grande", 1.30f)
}

enum class InterfaceStyle(val displayName: String) {
    MODERN("Estándar (Moderno)"),
    EXPRESSIVE("Compacto (Expresivo)")
}

@Serializable
data class UserSettings(
    // 1. Apariencia
    val theme: AppTheme = AppTheme.DARK,
    val accentColor: AccentColor = AccentColor.BLUE,
    val background: AppBackground = AppBackground.AMOLED_BLACK,
    val textSize: TextSizeSetting = TextSizeSetting.MEDIUM,
    val interfaceDesign: InterfaceStyle = InterfaceStyle.MODERN,
    val showCinematicIntro: Boolean = true,

    // 2. Reproducción
    val defaultQuality: String = "1080p",
    val autoPlayNextEpisode: Boolean = true,
    val subtitlesEnabled: Boolean = false,
    val aspectRatio: String = "16:9",

    // 3. TV en Vivo
    val playlistUrl: String = "https://iptv-org.github.io/iptv/index.m3u",
    val playlistAutoUpdateIntervalHours: Int = 24,
    val defaultCountry: String = "Argentina",
    val defaultLiveCategory: String = "Todos",

    // 4. Cuenta y Sesión
    val userName: String = "Usuario VIP MILTVPLUS",
    val userEmail: String = "usuario.vip@miltvplus.tv",
    val isLoggedIn: Boolean = true,

    // 5. Adultos +18
    val parentalControlPin: String = "0000",
    val isParentalControlEnabled: Boolean = true,
    val pinMaxAttempts: Int = 3,
    val pinLockoutDurationSeconds: Int = 30,
    val autoLockOnBackground: Boolean = true,

    // 6. Notificaciones
    val systemNotificationsEnabled: Boolean = true,
    val liveSportsAlertsEnabled: Boolean = true,

    // 7. Idioma y Región
    val appLanguage: String = "Español",
    val contentRegion: String = "Latinoamérica",

    // 8. Almacenamiento
    val cacheSizeBytes: Long = 134_217_728L, // 128 MB

    // 9. Accesibilidad
    val highContrastMode: Boolean = false,
    val dpadFocusHaloSize: String = "Normal",

    // 10. Diagnóstico
    val appVersion: String = "1.0.0",
    val serviceHealthStatus: String = "Excelente"
)
