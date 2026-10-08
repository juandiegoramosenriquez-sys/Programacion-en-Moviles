package com.saludplus.citas.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Registro : Rutas("registro")
    object Login : Rutas("login")
    object Terminos : Rutas("terminos")
    object Home : Rutas("home")
    object Especialidades : Rutas("especialidades")
    object Medicos : Rutas("medicos/{especialidadId}") {
        fun crearRuta(especialidadId: Int) = "medicos/$especialidadId"
    }
    object FechaHora : Rutas("fechaHora/{medicoId}") {
        fun crearRuta(medicoId: Int) = "fechaHora/$medicoId"
    }
    object ConfirmarCita : Rutas("confirmarCita/{medicoId}/{fecha}/{hora}") {
        fun crearRuta(medicoId: Int, fecha: String, hora: String) = "confirmarCita/$medicoId/$fecha/$hora"
    }
    object CitaExitosa : Rutas("citaExitosa/{citaId}") {
        fun crearRuta(citaId: Int) = "citaExitosa/$citaId"
    }
    object MisCitas : Rutas("misCitas")
    object DetalleCita : Rutas("detalleCita/{citaId}") {
        fun crearRuta(citaId: Int) = "detalleCita/$citaId"
    }
    object Perfil : Rutas("perfil")
    object Resultados : Rutas("resultados")
    object Notificaciones : Rutas("notificaciones")
}
