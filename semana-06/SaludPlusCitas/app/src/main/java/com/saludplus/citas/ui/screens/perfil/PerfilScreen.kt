package com.saludplus.citas.ui.screens.perfil

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun PerfilScreen(
    onCerrarSesion: () -> Unit,
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Perfil",
        botones = listOf(
            "Cerrar sesión" to onCerrarSesion
        ),
        onBack = onBack
    )
}
