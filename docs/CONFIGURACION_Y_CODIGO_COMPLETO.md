# MILTVPLUS v1.11.95 — Configuraciones y Código Fuente Completo

Este documento contiene la especificación completa de configuración, scripts de Gradle, Manifiesto y código fuente principal de la aplicación Android **MILTVPLUS v1.11.95**.

---

## 1. Configuración de Gradle del Proyecto (`build.gradle.kts` Raíz)

```kotlin
// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization) apply false
}
```

---

## 2. Configuración del Módulo App (`app/build.gradle.kts`)

```kotlin
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.milt.miltvplus"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.milt.miltvplus"
        minSdk = 24
        targetSdk = 37
        versionCode = 95
        versionName = "1.11.95"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val tmdbKey = project.findProperty("TMDB_API_KEY") as? String
            ?: System.getenv("TMDB_API_KEY")
            ?: ""
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbKey\"")
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists() && keystoreProperties.containsKey("storeFile")) {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            if (keystorePropertiesFile.exists() && keystoreProperties.containsKey("storeFile")) {
                signingConfig = signingConfigs.getByName("release")
            } else {
                val isPackagingRelease = project.gradle.startParameter.taskNames.any {
                    it.contains("assembleRelease", ignoreCase = true) ||
                    it.contains("bundleRelease", ignoreCase = true) ||
                    it.contains("packageRelease", ignoreCase = true)
                }
                if (isPackagingRelease) {
                    throw GradleException("ERROR: Production release keystore (keystore.properties) is missing. Release distribution packages cannot use debug signing.")
                }
            }
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.compose.adaptive.layout)
    implementation(libs.androidx.compose.adaptive.navigation3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.coil.compose)
    implementation(libs.converter.moshi)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.logging.interceptor)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp)
    implementation(libs.play.services.location)
    implementation(libs.retrofit)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.runner)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    "ksp"(libs.androidx.room.compiler)
    "ksp"(libs.moshi.kotlin.codegen)
}
```

---

## 3. Manifiesto de la Aplicación (`app/src/main/AndroidManifest.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <uses-feature
        android:name="android.hardware.touchscreen"
        android:required="false" />
    <uses-feature
        android:name="android.software.leanback"
        android:required="false" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MILTVPLUS"
        android:usesCleartextTraffic="true"
        tools:targetApi="31">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.MILTVPLUS">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
                <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
            </intent-filter>
        </activity>

    </application>

</manifest>
```

---

## 4. Actividad Principal Defensiva (`MainActivity.kt`)

```kotlin
package com.milt.miltvplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.milt.miltvplus.data.supabase.SupabaseClientManager
import com.milt.miltvplus.ui.components.CountrySelectorDialog
import com.milt.miltvplus.ui.components.FooterBanner
import com.milt.miltvplus.ui.components.TopNavBar
import com.milt.miltvplus.ui.navigation.NavDestination
import com.milt.miltvplus.ui.navigation.NavigationSection
import com.milt.miltvplus.ui.player.PlayerScreen
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

                            NavigationSection.FAVORITES, NavigationSection.SEARCH -> {
                                HomeScreen(
                                    uiState = uiState,
                                    onPlayItem = { viewModel.navigateToPlayer(it) },
                                    onItemDetails = { viewModel.navigateToDetails(it) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                                )
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
```

---

## 5. ViewModel Principal (`MainViewModel.kt`)

```kotlin
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
import kotlinx.coroutines.Job
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

    init {
        observeDataSources()
        try {
            syncRemotePlaylist("https://iptv-org.github.io/iptv/index.m3u", null)
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("MainViewModel", "Error syncing remote playlist on init", e)
        }

        try {
            loadTmdbData()
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("MainViewModel", "Error loading TMDB data on init", e)
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

    fun navigateToSection(section: NavigationSection) {
        _uiState.update { state ->
            val updatedBackStack = state.backStack + NavDestination.Section(section)
            state.copy(currentSection = section, backStack = updatedBackStack)
        }
    }

    fun navigateToPlayer(item: PlayableItem) {
        _uiState.update { state ->
            val updatedBackStack = state.backStack + NavDestination.Player(item)
            state.copy(backStack = updatedBackStack)
        }
    }

    fun popBackStack() {
        _uiState.update { state ->
            if (state.backStack.size > 1) {
                val newStack = state.backStack.dropLast(1)
                val newDestination = newStack.last()
                val newSection = when (newDestination) {
                    is NavDestination.Section -> newDestination.section
                    else -> state.currentSection
                }
                state.copy(backStack = newStack, currentSection = newSection)
            } else {
                state
            }
        }
    }

    fun toggleFavorite(itemId: String) {
        repository.toggleFavorite(itemId)
    }

    private fun observeDataSources() {
        viewModelScope.launch {
            repository.liveTvRepository.getChannels().collectLatest { channels ->
                _uiState.update { it.copy(liveChannels = channels) }
            }
        }

        viewModelScope.launch {
            repository.moviesRepository.getMovies().collectLatest { movies ->
                _uiState.update { it.copy(movies = movies) }
            }
        }

        viewModelScope.launch {
            repository.seriesRepository.getSeries().collectLatest { series ->
                _uiState.update { it.copy(series = series) }
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
    }

    fun updateTheme(theme: AppTheme) {
        repository.userSettingsRepository.updateTheme(theme)
    }

    fun updateAccentColor(accentColor: AccentColor) {
        repository.userSettingsRepository.updateAccentColor(accentColor)
    }

    fun updateBackground(background: AppBackground) {
        repository.userSettingsRepository.updateBackground(background)
    }

    fun updateTextSize(textSize: TextSizeSetting) {
        repository.userSettingsRepository.updateTextSize(textSize)
    }

    fun updateInterfaceStyle(interfaceStyle: InterfaceStyle) {
        repository.userSettingsRepository.updateInterfaceStyle(interfaceStyle)
    }

    fun updateParentalPin(newPin: String) {
        repository.userSettingsRepository.updateParentalPin(newPin)
    }

    fun toggleParentalControl(enabled: Boolean) {
        repository.userSettingsRepository.toggleParentalControl(enabled)
    }
}
```

---

## 6. Workflows de CI/CD para GitHub Actions (`.github/workflows/android-ci.yml`)

> **Nota de Seguridad y CI/CD**:
> - `compileReleaseKotlin`: Tarea ejecutada en la integración continua (CI) para validar estáticamente la sintaxis y compilación del código Release en GitHub Actions sin exponer archivos o contraseñas del Keystore de producción.
> - `assembleRelease` y `bundleRelease`: Tareas de empaquetado y firma V2 que requieren `keystore.properties` para generar los binarios oficiales distribuidos (`app-release.apk` y `app-release.aab`).

```yaml
name: Android CI

on:
  push:
    branches: [ "main", "fix/production-tmdb-key-validation" ]
  pull_request:
    branches: [ "main" ]
  workflow_dispatch:

jobs:
  build-and-test:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout repository
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        distribution: 'temurin'
        java-version: '17'
        cache: 'gradle'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Run unit tests
      run: ./gradlew :app:testDebugUnitTest

    - name: Build Debug APK
      run: ./gradlew :app:assembleDebug

    - name: Compile Release Sources (CI Check)
      run: ./gradlew :app:compileReleaseKotlin

    - name: Run Lint
      run: ./gradlew :app:lintDebug

    - name: Upload Debug APK artifact
      uses: actions/upload-artifact@v4
      with:
        name: debug-apk
        path: app/build/outputs/apk/debug/app-debug.apk
```
