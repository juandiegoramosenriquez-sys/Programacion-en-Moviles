package com.saludplus.citas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresSaludPlus = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulPrimario,
    secondary = AzulPrimario,
    secondaryContainer = AzulClaro,
    onSecondaryContainer = AzulPrimario,
    background = Fondo,
    onBackground = TextoPrincipal,
    surface = Fondo,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color.White,
    error = Rojo
)

@Composable
fun SaludPlusCitasTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresSaludPlus,
        typography = Typography,
        content = content
    )
}
