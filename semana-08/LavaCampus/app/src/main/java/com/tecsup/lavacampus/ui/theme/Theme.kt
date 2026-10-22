package com.tecsup.lavacampus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.Rol

private val EstudianteClaro = lightColorScheme(
    primary = Turquesa,
    onPrimary = Color.White,
    primaryContainer = TurquesaContenedor,
    onPrimaryContainer = TurquesaOscuro,
    secondary = Color(0xFF06B6D4),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB6F4FF),
    onSecondaryContainer = TurquesaOscuro,
    tertiary = TurquesaElectrico,
    onTertiary = TurquesaOscuro,
    background = TurquesaFondo,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = TurquesaSuave,
    onSurfaceVariant = TintaSuave,
    outline = Borde,
    outlineVariant = Color(0xFFCDE8EF),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF7FDFF),
    surfaceContainer = Color.White,
    surfaceContainerHigh = TurquesaSuave,
    surfaceContainerHighest = Color(0xFFD3F1F8),
    error = CanceladoClaro
)

private val EstudianteOscuro = darkColorScheme(
    primary = NocheTurquesaAcento,
    onPrimary = NocheTurquesaSobreAcento,
    primaryContainer = NocheTurquesaContenedor,
    onPrimaryContainer = NocheTurquesaSobreContenedor,
    secondary = NocheTurquesaAcento,
    onSecondary = NocheTurquesaSobreAcento,
    secondaryContainer = NocheTurquesaContenedor,
    onSecondaryContainer = NocheTurquesaSobreContenedor,
    tertiary = NocheTurquesaAcento,
    onTertiary = NocheTurquesaSobreAcento,
    background = NocheTurquesaFondo,
    onBackground = NocheTurquesaTexto,
    surface = NocheTurquesaTarjeta,
    onSurface = NocheTurquesaTexto,
    surfaceVariant = NocheTurquesaTarjeta,
    onSurfaceVariant = NocheTurquesaTextoSuave,
    outline = NocheTurquesaApagado,
    outlineVariant = NocheTurquesaBorde,
    surfaceContainerLowest = NocheTurquesaFondo,
    surfaceContainerLow = NocheTurquesaFondo,
    surfaceContainer = NocheTurquesaFondo,
    surfaceContainerHigh = NocheTurquesaTarjeta,
    surfaceContainerHighest = NocheTurquesaBorde,
    error = CanceladoNeon
)

private val AdminClaro = lightColorScheme(
    primary = Violeta,
    onPrimary = Color.White,
    primaryContainer = VioletaContenedor,
    onPrimaryContainer = VioletaOscuro,
    secondary = Color(0xFF8B5CF6),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE4D7FF),
    onSecondaryContainer = VioletaOscuro,
    tertiary = VioletaElectrico,
    onTertiary = Color.White,
    background = VioletaFondo,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = VioletaSuave,
    onSurfaceVariant = TintaSuave,
    outline = Borde,
    outlineVariant = Color(0xFFE0D8F5),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFCFBFF),
    surfaceContainer = Color.White,
    surfaceContainerHigh = VioletaSuave,
    surfaceContainerHighest = Color(0xFFE5DCFA),
    error = CanceladoClaro
)

private val AdminOscuro = darkColorScheme(
    primary = NocheVioletaAcento,
    onPrimary = NocheVioletaSobreAcento,
    primaryContainer = NocheVioletaContenedor,
    onPrimaryContainer = NocheVioletaSobreContenedor,
    secondary = NocheVioletaAcento,
    onSecondary = NocheVioletaSobreAcento,
    secondaryContainer = NocheVioletaContenedor,
    onSecondaryContainer = NocheVioletaSobreContenedor,
    tertiary = NocheVioletaAcento,
    onTertiary = NocheVioletaSobreAcento,
    background = NocheVioletaFondo,
    onBackground = NocheVioletaTexto,
    surface = NocheVioletaTarjeta,
    onSurface = NocheVioletaTexto,
    surfaceVariant = NocheVioletaTarjeta,
    onSurfaceVariant = NocheVioletaTextoSuave,
    outline = NocheVioletaApagado,
    outlineVariant = NocheVioletaBorde,
    surfaceContainerLowest = NocheVioletaFondo,
    surfaceContainerLow = NocheVioletaFondo,
    surfaceContainer = NocheVioletaFondo,
    surfaceContainerHigh = NocheVioletaTarjeta,
    surfaceContainerHighest = NocheVioletaBorde,
    error = CanceladoNeon
)

private val Formas = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun degradadoElectrico(): Brush = if (esModoOscuro()) {
    SolidColor(MaterialTheme.colorScheme.primaryContainer)
} else {
    Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.onPrimaryContainer,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )
}

@Composable
fun textoSobreDestacado(): Color =
    if (esModoOscuro()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary

@Composable
fun LavaTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val colores = when (rol) {
        Rol.CLIENTE -> if (oscuro) EstudianteOscuro else EstudianteClaro
        Rol.ADMIN -> if (oscuro) AdminOscuro else AdminClaro
    }
    MaterialTheme(
        colorScheme = colores,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}
