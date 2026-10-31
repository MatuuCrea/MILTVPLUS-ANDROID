package com.milt.miltvplus.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milt.miltvplus.data.repository.ContentRepository
import com.milt.miltvplus.domain.model.AccentColor
import com.milt.miltvplus.domain.model.AppBackground
import com.milt.miltvplus.domain.model.AppTheme
import com.milt.miltvplus.domain.model.InterfaceStyle
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.TextSizeSetting
import com.milt.miltvplus.ui.navigation.NavDestination
import com.milt.miltvplus.ui.navigation.NavigationSection
import com.milt.miltvplus.ui.state.MainUiState
import com.milt.miltvplus.ui.state.PinVerificationState
import com.milt.miltvplus.ui.state.PlaybackState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    val repository: ContentRepository = ContentRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var lockoutJob: Job? = null

    init {
        observeDataSources()
        runCatching {
            syncRemotePlaylist("https://iptv-org.github.io/iptv/index.m3u", null)
        }
        runCatching {
            loadTmdbData()
        }
    }

    fun loadTmdbData() {
        viewModelScope.launch {
            repository.tmdbRepository.getTrendingMovies()
            repository.tmdbRepository.getTrendingTvShows()
        }
    }

    fun initSettingsContext(context: Context) {
        repository.userSettingsRepository.initContext(context)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    fun syncRemotePlaylist(url: String = _uiState.value.userSettings.playlistUrl, context: Context? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.liveTvRepository.syncRemotePlaylistUrl(url, context)
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.localizedMessage
                )
            }
        }
    }

    fun retryLiveTvSync() {
        syncRemotePlaylist()
    }

    private fun observeDataSources() {
        viewModelScope.launch {
            repository.liveTvRepository.getChannels().collectLatest { channels ->
                _uiState.update { it.copy(liveChannels = channels) }
            }
        }

        viewModelScope.launch {
            repository.liveTvRepository.syncMetrics.collectLatest { metrics ->
                _uiState.update { it.copy(syncMetrics = metrics) }
            }
        }

        viewModelScope.launch {
            repository.moviesRepository.getMovies().collectLatest { movies ->
                _uiState.update { it.copy(movies = movies) }
            }
        }

        viewModelScope.launch {
            repository.moviesRepository.getFeaturedMovie().collectLatest { featured ->
                _uiState.update { it.copy(featuredMovie = featured) }
            }
        }

        viewModelScope.launch {
            repository.seriesRepository.getSeries().collectLatest { series ->
                _uiState.update { it.copy(series = series) }
            }
        }

        viewModelScope.launch {
            repository.sportsRepository.getSportsEvents().collectLatest { sports ->
                _uiState.update { it.copy(sportsEvents = sports) }
            }
        }

        viewModelScope.launch {
            repository.radiosRepository.getRadios().collectLatest { radios ->
                _uiState.update { it.copy(radioStations = radios) }
            }
        }

        viewModelScope.launch {
            repository.musicRepository.getPlaylists().collectLatest { playlists ->
                _uiState.update { it.copy(musicPlaylists = playlists) }
            }
        }

        viewModelScope.launch {
            repository.theatreRepository.getTheatreItems().collectLatest { theatre ->
                _uiState.update { it.copy(theatreItems = theatre) }
            }
        }

        viewModelScope.launch {
            repository.adultRepository.getAdultItems().collectLatest { adult ->
                _uiState.update { it.copy(adultItems = adult) }
            }
        }

        viewModelScope.launch {
            repository.getAllFavorites().collectLatest { favorites ->
                _uiState.update { it.copy(favorites = favorites) }
            }
        }

        viewModelScope.launch {
            repository.userSettingsRepository.getSettings().collectLatest { settings ->
                _uiState.update { it.copy(userSettings = settings) }
            }
        }

        viewModelScope.launch {
            repository.tmdbRepository.tmdbConnected.collectLatest { connected ->
                _uiState.update { it.copy(tmdbConnected = connected) }
            }
        }

        viewModelScope.launch {
            repository.tmdbRepository.isUsingFallback.collectLatest { fallback ->
                _uiState.update { it.copy(isUsingFallback = fallback) }
            }
        }
    }

    fun navigateToSection(section: NavigationSection) {
        val settings = _uiState.value.userSettings
        val pinVerified = _uiState.value.pinState.isVerified

        if (section.isPinProtected && settings.isParentalControlEnabled && !pinVerified) {
            _uiState.update { state ->
                state.copy(
                    pinState = state.pinState.copy(
                        pendingSection = section,
                        errorMessage = null,
                        enteredPin = ""
                    ),
                    currentSection = section,
                    backStack = state.backStack + NavDestination.PinVerification(section)
                )
            }
            return
        }

        _uiState.update { state ->
            val updatedStack = state.backStack + NavDestination.Section(section)
            state.copy(
                currentSection = section,
                backStack = updatedStack
            )
        }
    }

    fun navigateToDetails(item: PlayableItem) {
        _uiState.update { state ->
            state.copy(
                selectedDetailItem = item,
                backStack = state.backStack + NavDestination.Details(item)
            )
        }
    }

    fun navigateToPlayer(item: PlayableItem) {
        playMedia(item)
        _uiState.update { state ->
            state.copy(
                backStack = state.backStack + NavDestination.Player(item)
            )
        }
    }

    fun popBackStack(): Boolean {
        val currentStack = _uiState.value.backStack
        if (currentStack.size > 1) {
            val newStack = currentStack.dropLast(1)
            val topDestination = newStack.last()
            val section = when (topDestination) {
                is NavDestination.Section -> topDestination.section
                else -> _uiState.value.currentSection
            }

            if (_uiState.value.userSettings.autoLockOnBackground && _uiState.value.currentSection.isPinProtected && section != NavigationSection.ADULT) {
                resetPinVerification()
            }

            _uiState.update { state ->
                state.copy(
                    backStack = newStack,
                    currentSection = section
                )
            }
            return true
        }
        return false
    }

    fun onPinDigitEntered(digit: String) {
        val pinState = _uiState.value.pinState
        if (pinState.isLockedOut) return
        val currentPin = pinState.enteredPin
        if (currentPin.length < 4) {
            val newPin = currentPin + digit
            _uiState.update { state ->
                state.copy(
                    pinState = state.pinState.copy(
                        enteredPin = newPin,
                        errorMessage = null
                    )
                )
            }
            if (newPin.length == 4) {
                verifyPin(newPin)
            }
        }
    }

    fun onPinDelete() {
        val pinState = _uiState.value.pinState
        if (pinState.isLockedOut) return
        val currentPin = pinState.enteredPin
        if (currentPin.isNotEmpty()) {
            _uiState.update { state ->
                state.copy(
                    pinState = state.pinState.copy(
                        enteredPin = currentPin.dropLast(1),
                        errorMessage = null
                    )
                )
            }
        }
    }

    fun verifyPin(pin: String = _uiState.value.pinState.enteredPin) {
        val pinState = _uiState.value.pinState
        if (pinState.isLockedOut) return

        val settings = _uiState.value.userSettings
        val correctPin = settings.parentalControlPin

        if (pin == correctPin) {
            val targetSection = pinState.pendingSection ?: NavigationSection.ADULT
            _uiState.update { state ->
                val cleanedStack = state.backStack.filterNot { it is NavDestination.PinVerification }
                state.copy(
                    pinState = PinVerificationState(
                        isVerified = true,
                        enteredPin = "",
                        errorMessage = null,
                        pendingSection = null,
                        failedAttempts = 0,
                        isLockedOut = false,
                        lockoutRemainingSeconds = 0
                    ),
                    currentSection = targetSection,
                    backStack = cleanedStack + NavDestination.Section(targetSection)
                )
            }
        } else {
            val newFailed = pinState.failedAttempts + 1
            val maxAttempts = settings.pinMaxAttempts
            val locked = newFailed >= maxAttempts

            val message = if (locked) {
                "Demasiados intentos fallidos (${newFailed}/${maxAttempts}). PIN bloqueado por ${settings.pinLockoutDurationSeconds}s."
            } else {
                "PIN incorrecto. Intento ${newFailed} de ${maxAttempts}."
            }

            _uiState.update { state ->
                state.copy(
                    pinState = state.pinState.copy(
                        enteredPin = "",
                        failedAttempts = newFailed,
                        isLockedOut = locked,
                        errorMessage = message,
                        lockoutRemainingSeconds = if (locked) settings.pinLockoutDurationSeconds else 0
                    )
                )
            }

            if (locked) {
                startLockoutCountdown(settings.pinLockoutDurationSeconds, newFailed)
            }
        }
    }

    private fun startLockoutCountdown(durationSeconds: Int, failedAttempts: Int) {
        lockoutJob?.cancel()
        lockoutJob = viewModelScope.launch {
            var remaining = durationSeconds
            while (remaining > 0) {
                _uiState.update { state ->
                    state.copy(
                        pinState = state.pinState.copy(
                            isLockedOut = true,
                            failedAttempts = failedAttempts,
                            lockoutRemainingSeconds = remaining,
                            errorMessage = "Demasiados intentos fallidos (${failedAttempts}/3). PIN bloqueado. Reintenta en ${remaining}s"
                        )
                    )
                }
                delay(1000L)
                remaining--
            }
            _uiState.update { state ->
                state.copy(
                    pinState = state.pinState.copy(
                        isLockedOut = false,
                        failedAttempts = 0,
                        lockoutRemainingSeconds = 0,
                        enteredPin = "",
                        errorMessage = null
                    )
                )
            }
        }
    }

    fun resetPinVerification() {
        _uiState.update { state ->
            state.copy(
                pinState = PinVerificationState(
                    isVerified = false,
                    enteredPin = "",
                    errorMessage = null,
                    pendingSection = null,
                    failedAttempts = state.pinState.failedAttempts,
                    isLockedOut = state.pinState.isLockedOut,
                    lockoutRemainingSeconds = state.pinState.lockoutRemainingSeconds
                )
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            repository.searchContent(query).collectLatest { results ->
                _uiState.update { it.copy(searchResults = results) }
            }
        }
    }

    fun toggleFavorite(itemId: String) {
        repository.toggleFavorite(itemId)
    }

    fun playMedia(item: PlayableItem) {
        _uiState.update { state ->
            state.copy(
                playbackState = PlaybackState(
                    currentItem = item,
                    isPlaying = true,
                    currentPositionMs = 0L,
                    durationMs = 120_000L
                )
            )
        }
    }

    fun pauseMedia() {
        _uiState.update { state ->
            state.copy(
                playbackState = state.playbackState.copy(isPlaying = false)
            )
        }
    }

    fun resumeMedia() {
        _uiState.update { state ->
            state.copy(
                playbackState = state.playbackState.copy(isPlaying = true)
            )
        }
    }

    fun toggleFullscreen() {
        _uiState.update { state ->
            state.copy(
                playbackState = state.playbackState.copy(isFullscreen = !state.playbackState.isFullscreen)
            )
        }
    }

    fun stopMedia() {
        _uiState.update { state ->
            state.copy(
                playbackState = PlaybackState()
            )
        }
    }

    fun setCountrySelectorOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isCountrySelectorOpen = isOpen) }
    }

    fun selectCountry(country: String) {
        _uiState.update { it.copy(selectedCountry = country, isCountrySelectorOpen = false) }
    }

    // --- Settings Binds ---
    // 1. Apariencia
    fun updateTheme(theme: AppTheme) {
        repository.userSettingsRepository.updateTheme(theme)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun updateAccentColor(color: AccentColor) {
        repository.userSettingsRepository.updateAccentColor(color)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun updateBackground(bg: AppBackground) {
        repository.userSettingsRepository.updateBackground(bg)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun updateTextSize(textSize: TextSizeSetting) {
        repository.userSettingsRepository.updateTextSize(textSize)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun updateInterfaceStyle(style: InterfaceStyle) {
        repository.userSettingsRepository.updateInterfaceStyle(style)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun toggleCinematicIntro(enabled: Boolean) {
        repository.userSettingsRepository.toggleCinematicIntro(enabled)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    // 2. Reproducción
    fun updateDefaultQuality(quality: String) {
        repository.userSettingsRepository.updateDefaultQuality(quality)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun toggleAutoPlay(enabled: Boolean) {
        repository.userSettingsRepository.toggleAutoPlay(enabled)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun toggleSubtitles(enabled: Boolean) {
        repository.userSettingsRepository.toggleSubtitles(enabled)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun updateAspectRatio(ratio: String) {
        repository.userSettingsRepository.updateAspectRatio(ratio)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    // 3. TV en Vivo
    fun updatePlaylistUrl(url: String) {
        repository.userSettingsRepository.updatePlaylistUrl(url)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
        syncRemotePlaylist(url)
    }
    fun updateAutoUpdateInterval(hours: Int) {
        repository.userSettingsRepository.updateAutoUpdateInterval(hours)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun updateDefaultCountry(country: String) {
        repository.userSettingsRepository.updateDefaultCountry(country)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun updateDefaultLiveCategory(category: String) {
        repository.userSettingsRepository.updateDefaultLiveCategory(category)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    // 4. Cuenta y Sesión
    fun updateProfileInfo(name: String, email: String) {
        repository.userSettingsRepository.updateProfileInfo(name, email)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun toggleAuthState(isLoggedIn: Boolean) {
        repository.userSettingsRepository.toggleAuthState(isLoggedIn)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    // 5. Adultos +18
    fun updateParentalPin(pin: String) {
        repository.userSettingsRepository.updatePin(pin)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun toggleParentalControl(enabled: Boolean) {
        repository.userSettingsRepository.toggleParentalControl(enabled)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun toggleAutoLockOnBackground(enabled: Boolean) {
        repository.userSettingsRepository.toggleAutoLockOnBackground(enabled)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    // 6. Notificaciones
    fun toggleSystemNotifications(enabled: Boolean) {
        repository.userSettingsRepository.toggleSystemNotifications(enabled)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun toggleLiveSportsAlerts(enabled: Boolean) {
        repository.userSettingsRepository.toggleLiveSportsAlerts(enabled)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    // 7. Idioma y Región
    fun updateAppLanguage(language: String) {
        repository.userSettingsRepository.updateAppLanguage(language)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun updateContentRegion(region: String) {
        repository.userSettingsRepository.updateContentRegion(region)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    // 8. Almacenamiento
    fun clearCache() {
        repository.userSettingsRepository.clearCache()
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    // 9. Accesibilidad
    fun toggleHighContrast(enabled: Boolean) {
        repository.userSettingsRepository.toggleHighContrast(enabled)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
    fun updateDpadFocusHaloSize(size: String) {
        repository.userSettingsRepository.updateDpadFocusHaloSize(size)
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }

    // 10. Diagnóstico
    fun resetPreferences() {
        repository.userSettingsRepository.resetPreferences()
        _uiState.update { it.copy(userSettings = repository.userSettingsRepository.getSettings().value) }
    }
}
