package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.ui.graphics.Color

data class DatoGrafico(
    val etiqueta: String,
    val valor: Double,
    val color: Color = Color.Unspecified
)
