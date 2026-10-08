package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun NotificacionesScreen(
    onBack: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Notificaciones",
        onBack = onBack
    )
}
