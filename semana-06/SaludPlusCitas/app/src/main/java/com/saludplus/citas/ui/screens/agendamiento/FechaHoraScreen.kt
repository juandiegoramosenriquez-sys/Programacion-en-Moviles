package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (String, String) -> Unit,
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Fecha y hora",
        botones = listOf(
            "Continuar" to { onContinuar("2026-10-13", "09:00") }
        ),
        onBack = onBack
    )
}
