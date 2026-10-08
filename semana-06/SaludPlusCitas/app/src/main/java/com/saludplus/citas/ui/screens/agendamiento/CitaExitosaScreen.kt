package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onVerMisCitas: () -> Unit,
    onIrAlInicio: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Cita agendada",
        botones = listOf(
            "Ver mis citas" to onVerMisCitas,
            "Ir al inicio" to onIrAlInicio
        )
    )
}
