package com.tecsup.catalogo.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ColoresAurora = lightColorScheme(
    primary = Violeta,
    onPrimary = Color.White,
    primaryContainer = VioletaSuave,
    onPrimaryContainer = VioletaOscuro,
    secondary = Cian,
    onSecondary = Medianoche,
    secondaryContainer = CianSuave,
    onSecondaryContainer = CianOscuro,
    tertiary = Rosa,
    onTertiary = Color.White,
    tertiaryContainer = RosaSuave,
    onTertiaryContainer = RosaOscuro,
    background = Lavanda,
    onBackground = Texto,
    surface = Tarjeta,
    onSurface = Texto,
    surfaceVariant = FondoImagen,
    onSurfaceVariant = TextoSecundario,
    surfaceContainer = Tarjeta,
    surfaceContainerLow = Tarjeta,
    surfaceContainerHigh = Tarjeta,
    surfaceContainerHighest = Tarjeta,
    inverseSurface = Medianoche,
    inverseOnSurface = TextoEncabezado,
    outline = Borde,
    outlineVariant = Borde,
    error = Rojo,
    errorContainer = RojoSuave
)

private val Formas = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(12.dp),
    extraLarge = RoundedCornerShape(14.dp)
)

@Composable
fun CatalogoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresAurora,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}
