package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onVerTerminos: () -> Unit,
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Registro",
        botones = listOf(
            "Registrarme" to onRegistroExitoso,
            "Ver términos" to onVerTerminos
        ),
        onBack = onBack
    )
}
