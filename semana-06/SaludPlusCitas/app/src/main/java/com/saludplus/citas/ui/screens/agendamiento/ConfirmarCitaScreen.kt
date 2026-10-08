package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onCitaConfirmada: (Int) -> Unit,
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Confirmar cita",
        botones = listOf(
            "Confirmar" to { onCitaConfirmada(0) }
        ),
        onBack = onBack
    )
}
