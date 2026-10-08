package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onMedicoClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Médicos",
        botones = listOf(
            "Dr. Carlos Mendoza" to { onMedicoClick(1) }
        ),
        onBack = onBack
    )
}
