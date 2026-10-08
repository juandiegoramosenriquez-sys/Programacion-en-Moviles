package com.saludplus.citas.ui.screens.home

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun HomeScreen(
    onAgendarCita: () -> Unit,
    onEspecialidadClick: (Int) -> Unit,
    onNotificaciones: () -> Unit,
    onMisCitas: () -> Unit,
    onResultados: () -> Unit,
    onPerfil: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Inicio",
        botones = listOf(
            "Agendar cita" to onAgendarCita,
            "Mis citas" to onMisCitas,
            "Resultados" to onResultados,
            "Perfil" to onPerfil,
            "Notificaciones" to onNotificaciones
        )
    )
}
