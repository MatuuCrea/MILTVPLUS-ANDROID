package com.milt.miltvplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.milt.miltvplus.ui.components.CountrySelectorDialog
import com.milt.miltvplus.ui.components.FooterBanner
import com.milt.miltvplus.ui.components.TopNavBar
import com.milt.miltvplus.ui.navigation.NavDestination
import com.milt.miltvplus.ui.navigation.NavigationSection
import com.milt.miltvplus.ui.player.PlayerScreen
import com.milt.miltvplus.data.supabase.SupabaseClientManager
import com.milt.miltvplus.ui.screens.AdultScreen
import com.milt.miltvplus.ui.screens.CinematicIntroScreen
import com.milt.miltvplus.ui.screens.HomeScreen
import com.milt.miltvplus.ui.screens.LiveTvScreen
import com.milt.miltvplus.ui.screens.MoviesSeriesScreen
import com.milt.miltvplus.ui.screens.MusicScreen
import com.milt.miltvplus.ui.screens.RadiosScreen
import com.milt.miltvplus.ui.screens.SettingsScreen
import com.milt.miltvplus.ui.screens.SportsScreen
import com.milt.miltvplus.ui.screens.TheatreScreen
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.MILTVPLUSTheme
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        try {
            SupabaseClientManager.initializeConnection(applicationContext)
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error initializing Supabase connection", e)
        }
        setContent {
            MILTVPLUSTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen(
    viewModel: MainViewModel = viewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.initSettingsContext(context)
    }

    val uiState by viewModel.uiState.collectAsState()
    val currentDestination = uiState.backStack.lastOrNull() ?: NavDestination.Section(uiState.currentSection)

    var hasPlayedCinematicIntro by remember { mutableStateOf(false) }

    if (uiState.userSettings.showCinematicIntro && !hasPlayedCinematicIntro) {
        CinematicIntroScreen(
            onIntroFinished = { hasPlayedCinematicIntro = true }
        )
        return
    }

    BackHandler(enabled = uiState.backStack.size > 1) {
        viewModel.popBackStack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentDestination !is NavDestination.Player) {
                TopNavBar(
                    currentSection = uiState.currentSection,
                    selectedCountry = uiState.selectedCountry,
                    onSectionSelected = { viewModel.navigateToSection(it) },
                    onSearchClick = { viewModel.navigateToSection(NavigationSection.SEARCH) },
                    onCountrySelectorClick = { viewModel.setCountrySelectorOpen(true) }
                )
            }
        },
        containerColor = DarkNavyBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (currentDestination) {
                    is NavDestination.Player -> {
                        PlayerScreen(
                            item = currentDestination.item,
                            onBack = { viewModel.popBackStack() }
                        )
                    }

                    is NavDestination.Section -> {
                        when (currentDestination.section) {
                            NavigationSection.HOME -> {
                                HomeScreen(
                                    uiState = uiState,
                                    onPlayItem = { viewModel.navigateToPlayer(it) },
                                    onItemDetails = { viewModel.navigateToDetails(it) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                                )
                            }

                            NavigationSection.LIVE_TV -> {
                                LiveTvScreen(
                                    uiState = uiState,
                                    onPlayChannel = { viewModel.navigateToPlayer(it) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                    onRetry = { viewModel.retryLiveTvSync() }
                                )
                            }

                            NavigationSection.MOVIES, NavigationSection.SERIES -> {
                                MoviesSeriesScreen(
                                    uiState = uiState,
                                    onPlayItem = { viewModel.navigateToPlayer(it) },
                                    onItemDetails = { viewModel.navigateToDetails(it) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                                )
                            }

                            NavigationSection.SPORTS -> {
                                SportsScreen(
                                    uiState = uiState,
                                    onPlayItem = { viewModel.navigateToPlayer(it) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                                )
                            }

                            NavigationSection.RADIOS -> {
                                RadiosScreen(
                                    uiState = uiState,
                                    onPlayItem = { viewModel.navigateToPlayer(it) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                                )
                            }

                            NavigationSection.MUSIC -> {
                                MusicScreen(
                                    uiState = uiState,
                                    onPlayItem = { viewModel.navigateToPlayer(it) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                                )
                            }

                            NavigationSection.THEATRE -> {
                                TheatreScreen(
                                    uiState = uiState,
                                    onPlayItem = { viewModel.navigateToPlayer(it) },
                                    onItemDetails = { viewModel.navigateToDetails(it) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                                )
                            }

                            NavigationSection.ADULT -> {
                                AdultScreen(
                                    uiState = uiState,
                                    onPinDigitEntered = { viewModel.onPinDigitEntered(it) },
                                    onPinDelete = { viewModel.onPinDelete() },
                                    onVerifyPin = { viewModel.verifyPin() },
                                    onPlayItem = { viewModel.navigateToPlayer(it) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                                )
                            }

                            NavigationSection.SETTINGS -> {
                                SettingsScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                            }

                            else -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(DarkNavyBackground)
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = currentDestination.section.displayName,
                                            color = TextPrimary,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Sección disponible en el ecosistema MILTVPLUS",
                                            color = TextMuted,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        HomeScreen(
                            uiState = uiState,
                            onPlayItem = { viewModel.navigateToPlayer(it) },
                            onItemDetails = { viewModel.navigateToDetails(it) },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) }
                        )
                    }
                }
            }

            if (currentDestination !is NavDestination.Player) {
                FooterBanner()
            }
        }

        if (uiState.isCountrySelectorOpen) {
            CountrySelectorDialog(
                selectedCountry = uiState.selectedCountry,
                onCountrySelected = { viewModel.selectCountry(it) },
                onDismissRequest = { viewModel.setCountrySelectorOpen(false) }
            )
        }
    }
}
