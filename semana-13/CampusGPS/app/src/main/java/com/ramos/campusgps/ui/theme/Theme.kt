package com.ramos.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val EsquemaClaro = lightColorScheme(
    primary = Acento,
    onPrimary = Color.White,
    primaryContainer = AcentoSuave,
    onPrimaryContainer = AcentoTexto,
    secondary = Noche,
    onSecondary = Color.White,
    secondaryContainer = NocheMedio,
    onSecondaryContainer = Color.White,
    tertiary = Verde,
    onTertiary = Color.White,
    background = Fondo,
    onBackground = Texto,
    surface = Superficie,
    onSurface = Texto,
    surfaceVariant = SuperficieTenue,
    onSurfaceVariant = TextoSuave,
    outline = Borde,
    outlineVariant = Borde,
    error = Rojo,
    onError = Color.White,
    errorContainer = RojoSuave,
    onErrorContainer = RojoTexto
)

private val EsquemaOscuro = darkColorScheme(
    primary = AcentoCarbon,
    onPrimary = Color.White,
    primaryContainer = AcentoCarbonSuave,
    onPrimaryContainer = AcentoCarbonTexto,
    secondary = CarbonCabecera,
    onSecondary = TextoCarbon,
    secondaryContainer = CarbonElevado,
    onSecondaryContainer = TextoCarbon,
    tertiary = Verde,
    onTertiary = Color.White,
    background = Carbon,
    onBackground = TextoCarbon,
    surface = CarbonSuperficie,
    onSurface = TextoCarbon,
    surfaceVariant = CarbonElevado,
    onSurfaceVariant = TextoSuaveCarbon,
    outline = CarbonBorde,
    outlineVariant = CarbonBorde,
    error = Rojo,
    onError = Color.White,
    errorContainer = RojoCarbonSuave,
    onErrorContainer = RojoCarbonTexto
)

private val Formas = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(8.dp),
    extraLarge = RoundedCornerShape(8.dp)
)

@Composable
fun CampusGPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}
