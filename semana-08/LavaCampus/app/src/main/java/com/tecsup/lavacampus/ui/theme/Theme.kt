package com.tecsup.lavacampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.lavacampus.model.Rol

private val EstudianteClaro = lightColorScheme(
    primary = Turquesa,
    onPrimary = Color.White,
    primaryContainer = TurquesaContenedor,
    onPrimaryContainer = TurquesaOscuro,
    secondary = TurquesaOscuro,
    background = TurquesaFondo,
    surface = Color.White
)

private val EstudianteOscuro = darkColorScheme(
    primary = Color(0xFF22D3EE),
    onPrimary = Color(0xFF083344),
    primaryContainer = Color(0xFF155E75),
    onPrimaryContainer = TurquesaContenedor,
    background = FondoOscuro,
    surface = SuperficieOscura
)

private val AdminClaro = lightColorScheme(
    primary = Violeta,
    onPrimary = Color.White,
    primaryContainer = VioletaContenedor,
    onPrimaryContainer = VioletaOscuro,
    secondary = VioletaOscuro,
    background = VioletaFondo,
    surface = Color.White
)

private val AdminOscuro = darkColorScheme(
    primary = Color(0xFFA78BFA),
    onPrimary = Color(0xFF2E1065),
    primaryContainer = Color(0xFF5B21B6),
    onPrimaryContainer = VioletaContenedor,
    background = FondoOscuro,
    surface = SuperficieOscura
)

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
        content = content
    )
}
