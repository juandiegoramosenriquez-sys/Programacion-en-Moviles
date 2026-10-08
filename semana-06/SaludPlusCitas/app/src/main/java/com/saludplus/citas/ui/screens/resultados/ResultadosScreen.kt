package com.saludplus.citas.ui.screens.resultados

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun ResultadosScreen(
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Resultados",
        onBack = onBack
    )
}
