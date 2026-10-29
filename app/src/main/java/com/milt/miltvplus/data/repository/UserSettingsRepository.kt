package com.milt.miltvplus.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.milt.miltvplus.domain.model.AccentColor
import com.milt.miltvplus.domain.model.AppBackground
import com.milt.miltvplus.domain.model.AppTheme
import com.milt.miltvplus.domain.model.InterfaceStyle
import com.milt.miltvplus.domain.model.TextSizeSetting
import com.milt.miltvplus.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSettingsRepository(context: Context? = null) {

    private var sharedPrefs: SharedPreferences? = context?.getSharedPreferences("miltvplus_user_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettingsFromPrefs())

    fun initContext(context: Context) {
        if (sharedPrefs == null) {
            sharedPrefs = context.getSharedPreferences("miltvplus_user_prefs", Context.MODE_PRIVATE)
            _settings.value = loadSettingsFromPrefs()
        }
    }

    private fun loadSettingsFromPrefs(): UserSettings {
        val p = sharedPrefs ?: return UserSettings()
        return UserSettings(
            // 1. Apariencia
            theme = try { AppTheme.valueOf(p.getString("theme", AppTheme.DARK.name) ?: AppTheme.DARK.name) } catch (e: Exception) { AppTheme.DARK },
            accentColor = try { AccentColor.valueOf(p.getString("accentColor", AccentColor.BLUE.name) ?: AccentColor.BLUE.name) } catch (e: Exception) { AccentColor.BLUE },
            background = try { AppBackground.valueOf(p.getString("background", AppBackground.AMOLED_BLACK.name) ?: AppBackground.AMOLED_BLACK.name) } catch (e: Exception) { AppBackground.AMOLED_BLACK },
            textSize = try { TextSizeSetting.valueOf(p.getString("textSize", TextSizeSetting.MEDIUM.name) ?: TextSizeSetting.MEDIUM.name) } catch (e: Exception) { TextSizeSetting.MEDIUM },
            interfaceDesign = try { InterfaceStyle.valueOf(p.getString("interfaceDesign", InterfaceStyle.MODERN.name) ?: InterfaceStyle.MODERN.name) } catch (e: Exception) { InterfaceStyle.MODERN },
            showCinematicIntro = p.getBoolean("showCinematicIntro", true),

            // 2. Reproducción
            defaultQuality = p.getString("defaultQuality", "1080p") ?: "1080p",
            autoPlayNextEpisode = p.getBoolean("autoPlayNextEpisode", true),
            subtitlesEnabled = p.getBoolean("subtitlesEnabled", false),
            aspectRatio = p.getString("aspectRatio", "16:9") ?: "16:9",

            // 3. TV en Vivo
            playlistUrl = p.getString("playlistUrl", "https://iptv-org.github.io/iptv/index.m3u") ?: "https://iptv-org.github.io/iptv/index.m3u",
            playlistAutoUpdateIntervalHours = p.getInt("playlistAutoUpdateIntervalHours", 24),
            defaultCountry = p.getString("defaultCountry", "Argentina") ?: "Argentina",
            defaultLiveCategory = p.getString("defaultLiveCategory", "Todos") ?: "Todos",

            // 4. Cuenta y Sesión
            userName = p.getString("userName", "Usuario VIP MILTVPLUS") ?: "Usuario VIP MILTVPLUS",
            userEmail = p.getString("userEmail", "usuario.vip@miltvplus.tv") ?: "usuario.vip@miltvplus.tv",
            isLoggedIn = p.getBoolean("isLoggedIn", true),

            // 5. Adultos +18
            parentalControlPin = p.getString("parentalControlPin", "0000") ?: "0000",
            isParentalControlEnabled = p.getBoolean("isParentalControlEnabled", true),
            pinMaxAttempts = p.getInt("pinMaxAttempts", 3),
            pinLockoutDurationSeconds = p.getInt("pinLockoutDurationSeconds", 30),
            autoLockOnBackground = p.getBoolean("autoLockOnBackground", true),

            // 6. Notificaciones
            systemNotificationsEnabled = p.getBoolean("systemNotificationsEnabled", true),
            liveSportsAlertsEnabled = p.getBoolean("liveSportsAlertsEnabled", true),

            // 7. Idioma y Región
            appLanguage = p.getString("appLanguage", "Español") ?: "Español",
            contentRegion = p.getString("contentRegion", "Latinoamérica") ?: "Latinoamérica",

            // 8. Almacenamiento
            cacheSizeBytes = p.getLong("cacheSizeBytes", 134_217_728L),

            // 9. Accesibilidad
            highContrastMode = p.getBoolean("highContrastMode", false),
            dpadFocusHaloSize = p.getString("dpadFocusHaloSize", "Normal") ?: "Normal",

            // 10. Diagnóstico
            appVersion = p.getString("appVersion", "1.0.0") ?: "1.0.0",
            serviceHealthStatus = p.getString("serviceHealthStatus", "Excelente") ?: "Excelente"
        )
    }

    private fun saveSettingsToPrefs(settings: UserSettings) {
        sharedPrefs?.edit()?.apply {
            putString("theme", settings.theme.name)
            putString("accentColor", settings.accentColor.name)
            putString("background", settings.background.name)
            putString("textSize", settings.textSize.name)
            putString("interfaceDesign", settings.interfaceDesign.name)
            putBoolean("showCinematicIntro", settings.showCinematicIntro)

            putString("defaultQuality", settings.defaultQuality)
            putBoolean("autoPlayNextEpisode", settings.autoPlayNextEpisode)
            putBoolean("subtitlesEnabled", settings.subtitlesEnabled)
            putString("aspectRatio", settings.aspectRatio)

            putString("playlistUrl", settings.playlistUrl)
            putInt("playlistAutoUpdateIntervalHours", settings.playlistAutoUpdateIntervalHours)
            putString("defaultCountry", settings.defaultCountry)
            putString("defaultLiveCategory", settings.defaultLiveCategory)

            putString("userName", settings.userName)
            putString("userEmail", settings.userEmail)
            putBoolean("isLoggedIn", settings.isLoggedIn)

            putString("parentalControlPin", settings.parentalControlPin)
            putBoolean("isParentalControlEnabled", settings.isParentalControlEnabled)
            putInt("pinMaxAttempts", settings.pinMaxAttempts)
            putInt("pinLockoutDurationSeconds", settings.pinLockoutDurationSeconds)
            putBoolean("autoLockOnBackground", settings.autoLockOnBackground)

            putBoolean("systemNotificationsEnabled", settings.systemNotificationsEnabled)
            putBoolean("liveSportsAlertsEnabled", settings.liveSportsAlertsEnabled)

            putString("appLanguage", settings.appLanguage)
            putString("contentRegion", settings.contentRegion)

            putLong("cacheSizeBytes", settings.cacheSizeBytes)

            putBoolean("highContrastMode", settings.highContrastMode)
            putString("dpadFocusHaloSize", settings.dpadFocusHaloSize)

            putString("appVersion", settings.appVersion)
            putString("serviceHealthStatus", settings.serviceHealthStatus)

            apply()
        }
    }

    fun getSettings(): kotlinx.coroutines.flow.StateFlow<UserSettings> = _settings.asStateFlow()
    fun currentSettings(): UserSettings = _settings.value

    private fun updateAndSave(update: (UserSettings) -> UserSettings) {
        val updated = update(_settings.value)
        _settings.value = updated
        saveSettingsToPrefs(updated)
    }

    // 1. Apariencia
    fun updateTheme(theme: AppTheme) = updateAndSave { it.copy(theme = theme) }
    fun updateAccentColor(color: AccentColor) = updateAndSave { it.copy(accentColor = color) }
    fun updateBackground(background: AppBackground) = updateAndSave { it.copy(background = background) }
    fun updateTextSize(textSize: TextSizeSetting) = updateAndSave { it.copy(textSize = textSize) }
    fun updateInterfaceStyle(style: InterfaceStyle) = updateAndSave { it.copy(interfaceDesign = style) }
    fun toggleCinematicIntro(enabled: Boolean) = updateAndSave { it.copy(showCinematicIntro = enabled) }

    // 2. Reproducción
    fun updateDefaultQuality(quality: String) = updateAndSave { it.copy(defaultQuality = quality) }
    fun toggleAutoPlay(enabled: Boolean) = updateAndSave { it.copy(autoPlayNextEpisode = enabled) }
    fun toggleSubtitles(enabled: Boolean) = updateAndSave { it.copy(subtitlesEnabled = enabled) }
    fun updateAspectRatio(aspectRatio: String) = updateAndSave { it.copy(aspectRatio = aspectRatio) }

    // 3. TV en Vivo
    fun updatePlaylistUrl(url: String) = updateAndSave { it.copy(playlistUrl = url) }
    fun updateAutoUpdateInterval(hours: Int) = updateAndSave { it.copy(playlistAutoUpdateIntervalHours = hours) }
    fun updateDefaultCountry(country: String) = updateAndSave { it.copy(defaultCountry = country) }
    fun updateDefaultLiveCategory(category: String) = updateAndSave { it.copy(defaultLiveCategory = category) }

    // 4. Cuenta y Sesión
    fun updateProfileInfo(name: String, email: String) = updateAndSave { it.copy(userName = name, userEmail = email) }
    fun toggleAuthState(isLoggedIn: Boolean) = updateAndSave { it.copy(isLoggedIn = isLoggedIn) }

    // 5. Adultos +18
    fun updatePin(newPin: String) {
        if (newPin.length == 4 && newPin.all { it.isDigit() }) {
            updateAndSave { it.copy(parentalControlPin = newPin) }
        }
    }
    fun toggleParentalControl(enabled: Boolean) = updateAndSave { it.copy(isParentalControlEnabled = enabled) }
    fun toggleAutoLockOnBackground(enabled: Boolean) = updateAndSave { it.copy(autoLockOnBackground = enabled) }

    // 6. Notificaciones
    fun toggleSystemNotifications(enabled: Boolean) = updateAndSave { it.copy(systemNotificationsEnabled = enabled) }
    fun toggleLiveSportsAlerts(enabled: Boolean) = updateAndSave { it.copy(liveSportsAlertsEnabled = enabled) }

    // 7. Idioma y Región
    fun updateAppLanguage(language: String) = updateAndSave { it.copy(appLanguage = language) }
    fun updateContentRegion(region: String) = updateAndSave { it.copy(contentRegion = region) }

    // 8. Almacenamiento
    fun clearCache() = updateAndSave { it.copy(cacheSizeBytes = 0L) }

    // 9. Accesibilidad
    fun toggleHighContrast(enabled: Boolean) = updateAndSave { it.copy(highContrastMode = enabled) }
    fun updateDpadFocusHaloSize(size: String) = updateAndSave { it.copy(dpadFocusHaloSize = size) }

    // 10. Diagnóstico
    fun resetPreferences() {
        val defaultSettings = UserSettings()
        _settings.value = defaultSettings
        saveSettingsToPrefs(defaultSettings)
    }
}
