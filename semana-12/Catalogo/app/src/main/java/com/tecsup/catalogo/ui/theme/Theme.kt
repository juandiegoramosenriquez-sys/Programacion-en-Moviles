package com.tecsup.catalogo.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ColoresClaros = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    primaryContainer = AzulSuave,
    onPrimaryContainer = AzulOscuro,
    secondary = Turquesa,
    onSecondary = Color.White,
    secondaryContainer = TurquesaSuave,
    onSecondaryContainer = TurquesaOscuro,
    tertiary = Coral,
    onTertiary = Color.White,
    tertiaryContainer = CoralSuave,
    background = Fondo,
    onBackground = Texto,
    surface = Tarjeta,
    onSurface = Texto,
    surfaceVariant = FondoImagen,
    onSurfaceVariant = TextoSecundario,
    surfaceContainer = Tarjeta,
    surfaceContainerLow = Tarjeta,
    surfaceContainerHigh = Tarjeta,
    surfaceContainerHighest = Tarjeta,
    outline = Borde,
    outlineVariant = Borde,
    error = Rojo,
    errorContainer = RojoSuave
)

private val Formas = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp)
)

@Composable
fun CatalogoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresClaros,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}
