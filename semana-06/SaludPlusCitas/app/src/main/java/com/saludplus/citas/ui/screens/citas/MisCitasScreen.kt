package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun MisCitasScreen(
    onCitaClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Mis citas",
        onBack = onBack
    )
}
