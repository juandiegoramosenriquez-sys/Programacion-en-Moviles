package com.tecsup.lavacampus.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Titulos = FontFamily.Serif
private val Texto = FontFamily.SansSerif

private fun estilo(fuente: FontFamily, peso: FontWeight, tamano: Int, linea: Int, espaciado: Double = 0.0) = TextStyle(
    fontFamily = fuente,
    fontWeight = peso,
    fontSize = tamano.sp,
    lineHeight = linea.sp,
    letterSpacing = espaciado.sp
)

val Typography = Typography(
    displaySmall = estilo(Titulos, FontWeight.Bold, 30, 36),
    headlineMedium = estilo(Titulos, FontWeight.Bold, 25, 32),
    headlineSmall = estilo(Titulos, FontWeight.Bold, 21, 28),
    titleLarge = estilo(Titulos, FontWeight.Bold, 19, 26),
    titleMedium = estilo(Texto, FontWeight.SemiBold, 15, 20, 0.1),
    titleSmall = estilo(Texto, FontWeight.SemiBold, 13, 18, 0.1),
    bodyLarge = estilo(Texto, FontWeight.Normal, 15, 22, 0.2),
    bodyMedium = estilo(Texto, FontWeight.Normal, 13, 18, 0.2),
    bodySmall = estilo(Texto, FontWeight.Normal, 11, 15, 0.3),
    labelLarge = estilo(Texto, FontWeight.SemiBold, 13, 18, 0.1),
    labelMedium = estilo(Texto, FontWeight.Medium, 11, 15, 0.3),
    labelSmall = estilo(Texto, FontWeight.Medium, 10, 14, 0.3)
)
