package com.ramos.centrotareas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaAzulNoche = lightColorScheme(
    primary = Noche800,
    onPrimary = Blanco,
    primaryContainer = Tinte,
    onPrimaryContainer = Noche900,
    secondary = Cielo600,
    onSecondary = Blanco,
    secondaryContainer = Cielo100,
    onSecondaryContainer = Cielo700,
    tertiary = Verde700,
    background = FondoNiebla,
    onBackground = Noche900,
    surface = Blanco,
    onSurface = Noche900,
    surfaceVariant = Pizarra200,
    onSurfaceVariant = Pizarra500,
    outline = Pizarra200,
    outlineVariant = Pizarra300,
    error = Rojo600,
    errorContainer = Rojo50,
    onErrorContainer = Rojo800
)

@Composable
fun CentroTareasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaAzulNoche,
        typography = Typography,
        content = content
    )
}
