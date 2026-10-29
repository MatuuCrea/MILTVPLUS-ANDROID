package com.milt.miltvplus.ui.state

import com.milt.miltvplus.data.repository.SyncMetrics
import com.milt.miltvplus.domain.model.AdultItem
import com.milt.miltvplus.domain.model.LiveTvChannel
import com.milt.miltvplus.domain.model.MediaItem
import com.milt.miltvplus.domain.model.MusicPlaylist
import com.milt.miltvplus.domain.model.PlayableItem
import com.milt.miltvplus.domain.model.RadioStation
import com.milt.miltvplus.domain.model.SportsEvent
import com.milt.miltvplus.domain.model.UserSettings
import com.milt.miltvplus.ui.navigation.NavDestination
import com.milt.miltvplus.ui.navigation.NavigationSection

data class PlaybackState(
    val currentItem: PlayableItem? = null,
    val isPlaying: Boolean = false,
    val isFullscreen: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isMuted: Boolean = false
)

data class PinVerificationState(
    val isVerified: Boolean = false,
    val enteredPin: String = "",
    val errorMessage: String? = null,
    val pendingSection: NavigationSection? = null,
    val failedAttempts: Int = 0,
    val maxAttempts: Int = 3,
    val isLockedOut: Boolean = false,
    val lockoutRemainingSeconds: Int = 0
)

data class MainUiState(
    val currentSection: NavigationSection = NavigationSection.HOME,
    val backStack: List<NavDestination> = listOf(NavDestination.Section(NavigationSection.HOME)),
    val searchQuery: String = "",
    val searchResults: List<PlayableItem> = emptyList(),
    val favorites: List<PlayableItem> = emptyList(),
    val liveChannels: List<LiveTvChannel> = emptyList(),
    val movies: List<MediaItem> = emptyList(),
    val featuredMovie: MediaItem? = null,
    val series: List<MediaItem> = emptyList(),
    val sportsEvents: List<SportsEvent> = emptyList(),
    val radioStations: List<RadioStation> = emptyList(),
    val musicPlaylists: List<MusicPlaylist> = emptyList(),
    val theatreItems: List<MediaItem> = emptyList(),
    val adultItems: List<AdultItem> = emptyList(),
    val userSettings: UserSettings = UserSettings(),
    val pinState: PinVerificationState = PinVerificationState(),
    val playbackState: PlaybackState = PlaybackState(),
    val selectedDetailItem: PlayableItem? = null,
    val selectedCountry: String = "Argentina",
    val isCountrySelectorOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val syncMetrics: SyncMetrics? = null,
    val tmdbConnected: Boolean = false,
    val isUsingFallback: Boolean = true
)
