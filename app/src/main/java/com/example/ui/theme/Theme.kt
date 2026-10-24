package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LiquidDarkColorScheme = darkColorScheme(
    primary = LiquidCyan,
    onPrimary = DeepAbyss,
    primaryContainer = Color(0xFF123648),
    onPrimaryContainer = LiquidCyanBright,
    secondary = LiquidViolet,
    onSecondary = DeepAbyss,
    secondaryContainer = Color(0xFF2B224D),
    onSecondaryContainer = Color(0xFFDDD6FE),
    tertiary = LiquidMagenta,
    onTertiary = DeepAbyss,
    background = DeepAbyss,
    onBackground = GlassWhitePure,
    surface = DeepCosmicSurface,
    onSurface = GlassWhitePure,
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = GlassFrostLight
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LiquidDarkColorScheme,
        typography = Typography,
        content = content
    )
}
