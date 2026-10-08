package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Detalle de cita",
        onBack = onBack
    )
}
