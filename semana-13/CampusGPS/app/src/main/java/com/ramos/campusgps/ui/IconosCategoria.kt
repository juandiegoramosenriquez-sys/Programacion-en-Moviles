package com.ramos.campusgps.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.School
import androidx.compose.ui.graphics.vector.ImageVector

fun iconoCategoria(categoria: String): ImageVector = when (categoria) {
    "Estudio" -> Icons.Outlined.School
    "Comida" -> Icons.Outlined.LocalCafe
    "Eventos" -> Icons.Outlined.Event
    "Servicios" -> Icons.Outlined.LocalParking
    else -> Icons.Outlined.Place
}
