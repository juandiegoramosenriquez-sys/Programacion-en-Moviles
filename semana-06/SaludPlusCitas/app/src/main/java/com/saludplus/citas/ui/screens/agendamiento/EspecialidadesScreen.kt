package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun EspecialidadesScreen(
    onEspecialidadClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Especialidades",
        botones = listOf(
            "Medicina General" to { onEspecialidadClick(1) }
        ),
        onBack = onBack
    )
}
