package com.hanzdev.hanzai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = HanzIndigo,
    onPrimary = Color.White,
    primaryContainer = HanzIndigoDark,
    onPrimaryContainer = Color.White,
    secondary = HanzIndigoLight,
    onSecondary = Color(0xFF14172B),
    tertiary = HanzCyan,
    onTertiary = Color.Black,
    background = DarkBg,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFB8C0DC),
)

private val LightColorScheme = lightColorScheme(
    primary = HanzIndigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E3FF),
    onPrimaryContainer = HanzIndigoDark,
    secondary = HanzIndigoDark,
    onSecondary = Color.White,
    tertiary = Color(0xFF0891B2),
    onTertiary = Color.White,
    background = LightBg,
    onBackground = LightText,
    surface = LightSurface,
    onSurface = LightText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF454A66),
)

@Composable
fun HanzAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
