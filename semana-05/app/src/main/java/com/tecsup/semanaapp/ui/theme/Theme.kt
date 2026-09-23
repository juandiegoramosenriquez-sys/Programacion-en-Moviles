package com.tecsup.semanaapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    secondary = LavenderLight,
    tertiary = PurpleGrey80,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    error = RedPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = Color.White,
    primaryContainer = LavenderLight,
    onPrimaryContainer = PrimaryPurple,
    secondary = PurpleGrey40,
    background = BackgroundLight,
    surface = Color.White,
    onBackground = TextDark,
    onSurface = TextDark,
    error = RedPrimary,
    errorContainer = RedContainer,
    onErrorContainer = RedPrimary
)

@Composable
fun SemanaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
