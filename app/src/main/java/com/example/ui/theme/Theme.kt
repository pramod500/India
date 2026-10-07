package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmergencyCrimson,
    onPrimary = Color.White,
    primaryContainer = EmergencyCrimsonDark,
    onPrimaryContainer = EmergencyCrimsonContainer,
    secondary = NationalSaffron,
    onSecondary = Color.Black,
    secondaryContainer = NationalSaffronDark,
    onSecondaryContainer = Color.White,
    tertiary = NationalGreen,
    onTertiary = Color.White,
    background = GovNavyDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = EmergencyCrimson,
    onPrimary = Color.White,
    primaryContainer = EmergencyCrimsonContainer,
    onPrimaryContainer = EmergencyOnCrimsonContainer,
    secondary = GovNavyPrimary,
    onSecondary = Color.White,
    secondaryContainer = GovNavyContainer,
    onSecondaryContainer = GovOnNavyContainer,
    tertiary = NationalGreen,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    error = EmergencyCrimson,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Fixed public-service palette to maintain national tricolor and emergency branding
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
