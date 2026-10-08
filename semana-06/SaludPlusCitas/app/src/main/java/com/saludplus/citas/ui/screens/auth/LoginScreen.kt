package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit,
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Iniciar sesión",
        botones = listOf(
            "Ingresar" to onLoginExitoso,
            "Ir a registro" to onIrARegistro
        ),
        onBack = onBack
    )
}
