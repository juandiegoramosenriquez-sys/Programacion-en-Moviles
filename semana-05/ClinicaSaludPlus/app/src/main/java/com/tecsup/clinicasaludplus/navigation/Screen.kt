package com.tecsup.clinicasaludplus.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MisCitas : Screen("mis_citas")
    object Historial : Screen("historial")
    object Doctor : Screen("doctor/{doctorId}") {
        fun createRoute(doctorId: Int): String = "doctor/$doctorId"
    }
    object Agendar : Screen("agendar/{doctorId}") {
        fun createRoute(doctorId: Int): String = "agendar/$doctorId"
    }
    object Confirmacion : Screen("confirmacion/{doctorId}/{fecha}/{hora}") {
        // Uri.encode porque la fecha tiene espacios ("Vie 27") y la hora dos puntos ("10:30")
        fun createRoute(doctorId: Int, fecha: String, hora: String): String =
            "confirmacion/$doctorId/${Uri.encode(fecha)}/${Uri.encode(hora)}"
    }
}
