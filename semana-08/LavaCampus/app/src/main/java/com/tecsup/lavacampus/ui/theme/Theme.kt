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
    secondary = Color(0xFF00C2E0),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF8FEFFF),
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
    outlineVariant = Color(0xFF9FE8F5),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF7FDFF),
    surfaceContainer = Color.White,
    surfaceContainerHigh = TurquesaSuave,
    surfaceContainerHighest = Color(0xFFA8EEFA),
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
    secondary = Color(0xFF9333FF),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD0B5FF),
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
    outlineVariant = Color(0xFFCBB5F5),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFCFBFF),
    surfaceContainer = Color.White,
    surfaceContainerHigh = VioletaSuave,
    surfaceContainerHighest = Color(0xFFD4BDFF),
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

private val SoporteClaro = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = AzulOscuro,
    secondary = Color(0xFF1A75FF),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFA8C9FF),
    onSecondaryContainer = AzulOscuro,
    tertiary = AzulElectrico,
    onTertiary = Color.White,
    background = AzulFondo,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = AzulSuave,
    onSurfaceVariant = TintaSuave,
    outline = Borde,
    outlineVariant = Color(0xFFA9C9F7),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = AzulFondo,
    surfaceContainer = Color.White,
    surfaceContainerHigh = AzulSuave,
    surfaceContainerHighest = AzulContenedor,
    error = CanceladoClaro
)

private val SoporteOscuro = darkColorScheme(
    primary = NocheAzulAcento,
    onPrimary = NocheAzulSobreAcento,
    primaryContainer = NocheAzulContenedor,
    onPrimaryContainer = NocheAzulSobreContenedor,
    secondary = NocheAzulAcento,
    onSecondary = NocheAzulSobreAcento,
    secondaryContainer = NocheAzulContenedor,
    onSecondaryContainer = NocheAzulSobreContenedor,
    tertiary = NocheAzulAcento,
    onTertiary = NocheAzulSobreAcento,
    background = NocheAzulFondo,
    onBackground = NocheAzulTexto,
    surface = NocheAzulTarjeta,
    onSurface = NocheAzulTexto,
    surfaceVariant = NocheAzulTarjeta,
    onSurfaceVariant = NocheAzulTextoSuave,
    outline = NocheAzulApagado,
    outlineVariant = NocheAzulBorde,
    surfaceContainerLowest = NocheAzulFondo,
    surfaceContainerLow = NocheAzulFondo,
    surfaceContainer = NocheAzulFondo,
    surfaceContainerHigh = NocheAzulTarjeta,
    surfaceContainerHighest = NocheAzulBorde,
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
        //Rol.ADMIN -> if (oscuro) AdminOscuro else AdminClaro
        Rol.ADMIN -> if (oscuro) AdminOscuro else AdminClaro
        Rol.SOPORTE -> if (oscuro) SoporteOscuro else SoporteClaro
    }
    MaterialTheme(
        colorScheme = colores,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}
