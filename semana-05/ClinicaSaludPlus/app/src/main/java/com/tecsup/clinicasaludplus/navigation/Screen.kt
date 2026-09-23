package com.tecsup.clinicasaludplus.navigation

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
    object Confirmacion : Screen("confirmacion/{doctorId}") {
        fun createRoute(doctorId: Int): String = "confirmacion/$doctorId"
    }
}
