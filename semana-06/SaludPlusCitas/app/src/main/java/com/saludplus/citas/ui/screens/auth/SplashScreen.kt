package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun SplashScreen(
    onIniciarSesion: () -> Unit,
    onRegistrarse: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Splash",
        botones = listOf(
            "Iniciar sesión" to onIniciarSesion,
            "Registrarme" to onRegistrarse
        )
    )
}
