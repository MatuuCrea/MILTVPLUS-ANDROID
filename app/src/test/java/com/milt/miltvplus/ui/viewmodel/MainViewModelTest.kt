package com.milt.miltvplus.ui.viewmodel

import com.milt.miltvplus.domain.model.AccentColor
import com.milt.miltvplus.domain.model.AppBackground
import com.milt.miltvplus.domain.model.AppTheme
import com.milt.miltvplus.domain.model.InterfaceStyle
import com.milt.miltvplus.domain.model.TextSizeSetting
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MainViewModelTest {

    @Test
    fun testPinVerificationSuccessAndFailureLockout() {
        val viewModel = MainViewModel()

        // Default PIN is "0000"
        // Test incorrect PIN attempt 1
        viewModel.verifyPin("1234")
        var state = viewModel.uiState.value
        assertFalse(state.pinState.isVerified)
        assertEquals(1, state.pinState.failedAttempts)
        assertFalse(state.pinState.isLockedOut)

        // Test incorrect PIN attempt 2
        viewModel.verifyPin("1234")
        state = viewModel.uiState.value
        assertEquals(2, state.pinState.failedAttempts)
        assertFalse(state.pinState.isLockedOut)

        // Test incorrect PIN attempt 3 (Max attempts reached -> Locked out)
        viewModel.verifyPin("1234")
        state = viewModel.uiState.value
        assertEquals(3, state.pinState.failedAttempts)
        assertTrue(state.pinState.isLockedOut)
        assertEquals(30, state.pinState.lockoutRemainingSeconds)

        // While locked out, trying correct PIN should remain locked out until reset
        viewModel.verifyPin("0000")
        state = viewModel.uiState.value
        assertFalse(state.pinState.isVerified)
        assertTrue(state.pinState.isLockedOut)
    }

    @Test
    fun testPinCorrectFirstTry() {
        val viewModel = MainViewModel()
        viewModel.verifyPin("0000")
        val state = viewModel.uiState.value
        assertTrue(state.pinState.isVerified)
        assertEquals(0, state.pinState.failedAttempts)
        assertFalse(state.pinState.isLockedOut)
    }

    @Test
    fun testUserSettingsExpansionAndUpdates() {
        val viewModel = MainViewModel()

        viewModel.updateTheme(AppTheme.LIGHT)
        assertEquals(AppTheme.LIGHT, viewModel.uiState.value.userSettings.theme)

        viewModel.updateAccentColor(AccentColor.LIME)
        assertEquals(AccentColor.LIME, viewModel.uiState.value.userSettings.accentColor)

        viewModel.updateBackground(AppBackground.DEEP_BLUE)
        assertEquals(AppBackground.DEEP_BLUE, viewModel.uiState.value.userSettings.background)

        viewModel.updateTextSize(TextSizeSetting.LARGE)
        assertEquals(TextSizeSetting.LARGE, viewModel.uiState.value.userSettings.textSize)

        viewModel.updateInterfaceStyle(InterfaceStyle.EXPRESSIVE)
        assertEquals(InterfaceStyle.EXPRESSIVE, viewModel.uiState.value.userSettings.interfaceDesign)

        viewModel.updateDefaultQuality("2160p")
        assertEquals("2160p", viewModel.uiState.value.userSettings.defaultQuality)

        viewModel.toggleAutoPlay(false)
        assertFalse(viewModel.uiState.value.userSettings.autoPlayNextEpisode)

        viewModel.toggleSubtitles(true)
        assertTrue(viewModel.uiState.value.userSettings.subtitlesEnabled)

        viewModel.updateAspectRatio("4:3")
        assertEquals("4:3", viewModel.uiState.value.userSettings.aspectRatio)

        viewModel.updateAutoUpdateInterval(12)
        assertEquals(12, viewModel.uiState.value.userSettings.playlistAutoUpdateIntervalHours)

        viewModel.updateDefaultCountry("Chile")
        assertEquals("Chile", viewModel.uiState.value.userSettings.defaultCountry)

        viewModel.updateProfileInfo("VIP User", "vip@miltv.tv")
        assertEquals("VIP User", viewModel.uiState.value.userSettings.userName)
        assertEquals("vip@miltv.tv", viewModel.uiState.value.userSettings.userEmail)

        viewModel.toggleSystemNotifications(false)
        assertFalse(viewModel.uiState.value.userSettings.systemNotificationsEnabled)

        viewModel.updateAppLanguage("English")
        assertEquals("English", viewModel.uiState.value.userSettings.appLanguage)

        viewModel.updateContentRegion("España")
        assertEquals("España", viewModel.uiState.value.userSettings.contentRegion)

        viewModel.clearCache()
        assertEquals(0L, viewModel.uiState.value.userSettings.cacheSizeBytes)

        viewModel.toggleHighContrast(true)
        assertTrue(viewModel.uiState.value.userSettings.highContrastMode)

        viewModel.updateDpadFocusHaloSize("Grande")
        assertEquals("Grande", viewModel.uiState.value.userSettings.dpadFocusHaloSize)

        viewModel.resetPreferences()
        val resetSettings = viewModel.uiState.value.userSettings
        assertEquals(AppTheme.DARK, resetSettings.theme)
        assertEquals("1080p", resetSettings.defaultQuality)
        assertEquals(134_217_728L, resetSettings.cacheSizeBytes)
    }
}
