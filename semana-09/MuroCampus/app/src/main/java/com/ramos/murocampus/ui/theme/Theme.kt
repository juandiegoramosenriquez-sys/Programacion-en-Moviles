package com.ramos.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Vino,
    onPrimary = Color.White,
    primaryContainer = VinoClaro,
    onPrimaryContainer = VinoProfundo,
    secondary = Color(0xFF75565B),
    onSecondary = Color.White,
    secondaryContainer = VinoClaro,
    onSecondaryContainer = Color(0xFF2C1519),
    tertiary = Oro,
    onTertiary = Color.White,
    tertiaryContainer = OroBrillante,
    onTertiaryContainer = OroTinta,
    background = Crema,
    onBackground = Tinta,
    surface = Crema,
    onSurface = Tinta,
    surfaceVariant = Color(0xFFF4DDDF),
    onSurfaceVariant = TintaSuave,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFFF0EF),
    surfaceContainer = Color(0xFFFBEAE9),
    surfaceContainerHigh = Color(0xFFF7E8E6),
    surfaceContainerHighest = Color(0xFFEFDEDE),
    outline = Color(0xFF857374),
    outlineVariant = Color(0xFFD8C2C3),
    inverseSurface = Color(0xFF382E2F),
    inverseOnSurface = Color(0xFFFBEEEC),
    inversePrimary = OroNoche,
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

private val EsquemaOscuro = darkColorScheme(
    primary = VinoRosa,
    onPrimary = VinoRosaTinta,
    primaryContainer = Vino,
    onPrimaryContainer = VinoClaro,
    secondary = Color(0xFFE5BDC2),
    onSecondary = Color(0xFF43292D),
    secondaryContainer = Color(0xFF5C3F43),
    onSecondaryContainer = VinoClaro,
    tertiary = OroNoche,
    onTertiary = OroNocheTinta,
    tertiaryContainer = OroNoche,
    onTertiaryContainer = OroTinta,
    background = Noche,
    onBackground = NocheTexto,
    surface = Noche,
    onSurface = NocheTexto,
    surfaceVariant = TintaSuave,
    onSurfaceVariant = NocheTextoSuave,
    surfaceContainerLowest = Color(0xFF140C0D),
    surfaceContainerLow = Color(0xFF22191A),
    surfaceContainer = Color(0xFF271D1E),
    surfaceContainerHigh = Color(0xFF322728),
    surfaceContainerHighest = Color(0xFF3D3233),
    outline = Color(0xFFA08C8D),
    outlineVariant = TintaSuave,
    inverseSurface = NocheTexto,
    inverseOnSurface = Color(0xFF382E2F),
    inversePrimary = Oro,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun MuroCampusTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}
