package com.tecsup.authfirebaseramos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColoresCeleste = lightColorScheme(
    primary = Celeste,
    onPrimary = Blanco,
    primaryContainer = CelesteClaro,
    onPrimaryContainer = Celeste,
    secondary = CelesteVivo,
    onSecondary = Blanco,
    background = CelesteFondo,
    onBackground = TextoOscuro,
    surface = Blanco,
    onSurface = TextoOscuro,
    surfaceVariant = CelesteClaro,
    onSurfaceVariant = TextoSecundario,
    outline = BordeCeleste
)

@Composable
fun AuthFirebaseRamosTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresCeleste,
        typography = Typography,
        content = content
    )
}
