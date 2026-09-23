package com.tecsup.tecsupfit.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object Reservas : Screen("reservas")
    object Rutinas : Screen("rutinas")
    object Perfil : Screen("perfil")
    object Detalle : Screen("detalle/{claseId}") {
        fun createRoute(claseId: Int): String = "detalle/$claseId"
    }
    object Confirmacion : Screen("confirmacion/{claseId}/{horario}") {
        // Uri.encode porque el horario tiene espacios y ":"
        fun createRoute(claseId: Int, horario: String): String =
            "confirmacion/$claseId/${Uri.encode(horario)}"
    }
}
