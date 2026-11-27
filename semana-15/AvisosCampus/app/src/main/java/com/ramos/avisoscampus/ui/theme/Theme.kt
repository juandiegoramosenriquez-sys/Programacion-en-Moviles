package com.ramos.avisoscampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalPaleta = staticCompositionLocalOf { PaletaClara }

private fun esquema(p: Paleta, oscuro: Boolean) = if (oscuro) {
    darkColorScheme(
        primary = p.boton, onPrimary = p.textoBoton,
        secondary = p.verde, tertiary = p.morado,
        background = p.fondo, onBackground = p.texto,
        surface = p.tarjeta, onSurface = p.texto,
        surfaceVariant = p.tarjeta, onSurfaceVariant = p.textoSecundario,
        outline = p.borde, outlineVariant = p.borde,
        error = p.rojo, errorContainer = p.etiquetaError.fondo, onErrorContainer = p.etiquetaError.texto
    )
} else {
    lightColorScheme(
        primary = p.boton, onPrimary = p.textoBoton,
        secondary = p.verde, tertiary = p.morado,
        background = p.fondo, onBackground = p.texto,
        surface = p.tarjeta, onSurface = p.texto,
        surfaceVariant = p.tarjeta, onSurfaceVariant = p.textoSecundario,
        outline = p.borde, outlineVariant = p.borde,
        error = p.rojo, errorContainer = p.etiquetaError.fondo, onErrorContainer = p.etiquetaError.texto
    )
}

@Composable
fun AvisosCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val paleta = if (darkTheme) PaletaOscura else PaletaClara
    CompositionLocalProvider(LocalPaleta provides paleta) {
        MaterialTheme(
            colorScheme = esquema(paleta, darkTheme),
            typography = Typography,
            content = content
        )
    }
}
