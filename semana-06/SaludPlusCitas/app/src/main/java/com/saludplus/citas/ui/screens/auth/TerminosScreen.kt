package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun TerminosScreen(
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Términos y condiciones",
        onBack = onBack
    )
}
