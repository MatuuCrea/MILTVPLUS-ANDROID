package com.milt.miltvplus.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.milt.miltvplus.domain.model.AppTheme
import com.milt.miltvplus.domain.model.UserSettings

@Composable
fun MILTVPLUSTheme(
    userSettings: UserSettings = UserSettings(),
    darkTheme: Boolean = when (userSettings.theme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    },
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val primaryColor = Color(userSettings.accentColor.colorValue)
    val backgroundColor = Color(userSettings.background.colorValue)

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(
            primary = primaryColor,
            secondary = primaryColor,
            tertiary = AccentPurple,
            background = backgroundColor,
            surface = SurfaceDark,
            surfaceVariant = CardSurfaceDark,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onTertiary = Color.White,
            onBackground = TextPrimary,
            onSurface = TextPrimary,
            onSurfaceVariant = TextSecondary,
            error = AccentRed
        )

        else -> lightColorScheme(
            primary = primaryColor,
            secondary = primaryColor,
            tertiary = AccentPurple,
            background = Color(0xFFF0F4F8),
            surface = Color.White,
            surfaceVariant = Color(0xFFE2E8F0),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFF0F172A),
            onSurface = Color(0xFF0F172A),
            onSurfaceVariant = Color(0xFF475569),
            error = AccentRed
        )
    }

    val textScale = userSettings.textSize.scale
    val scaledTypography = Typography(
        displayLarge = Typography.displayLarge.copy(fontSize = Typography.displayLarge.fontSize * textScale),
        displayMedium = Typography.displayMedium.copy(fontSize = Typography.displayMedium.fontSize * textScale),
        displaySmall = Typography.displaySmall.copy(fontSize = Typography.displaySmall.fontSize * textScale),
        headlineLarge = Typography.headlineLarge.copy(fontSize = Typography.headlineLarge.fontSize * textScale),
        headlineMedium = Typography.headlineMedium.copy(fontSize = Typography.headlineMedium.fontSize * textScale),
        headlineSmall = Typography.headlineSmall.copy(fontSize = Typography.headlineSmall.fontSize * textScale),
        titleLarge = Typography.titleLarge.copy(fontSize = Typography.titleLarge.fontSize * textScale),
        titleMedium = Typography.titleMedium.copy(fontSize = Typography.titleMedium.fontSize * textScale),
        titleSmall = Typography.titleSmall.copy(fontSize = Typography.titleSmall.fontSize * textScale),
        bodyLarge = Typography.bodyLarge.copy(fontSize = Typography.bodyLarge.fontSize * textScale),
        bodyMedium = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * textScale),
        bodySmall = Typography.bodySmall.copy(fontSize = Typography.bodySmall.fontSize * textScale),
        labelLarge = Typography.labelLarge.copy(fontSize = Typography.labelLarge.fontSize * textScale),
        labelMedium = Typography.labelMedium.copy(fontSize = Typography.labelMedium.fontSize * textScale),
        labelSmall = Typography.labelSmall.copy(fontSize = Typography.labelSmall.fontSize * textScale)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography,
        content = content
    )
}
