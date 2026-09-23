package com.tecsup.tecsupfit.model

data class Clase(
    val id: Int,
    val nombre: String,
    val instructor: String,
    val cupos: Int,
    val dia: String, // "Hoy" o "Esta semana"
    val horarios: List<String>
)

data class Reserva(
    val clase: Clase,
    val horario: String,
    val estado: String // "Confirmada" o "Completada"
)

object DataSource {
    val filtros = listOf("Hoy", "Esta semana")

    val clases = listOf(
        Clase(1, "Spinning", "Carla Rojas", 12, "Hoy", listOf("07:00 AM", "12:00 PM", "06:00 PM")),
        Clase(2, "Yoga", "Miguel Soto", 8, "Hoy", listOf("08:00 AM", "05:00 PM", "07:00 PM")),
        Clase(3, "Crossfit", "Pedro Díaz", 10, "Esta semana", listOf("Mié 08:00 AM", "Jue 06:00 PM", "Sáb 10:00 AM")),
        Clase(4, "Zumba", "Lucía Flores", 15, "Esta semana", listOf("Mar 05:00 PM", "Vie 05:00 PM", "Sáb 09:00 AM"))
    )

    val reservasIniciales = listOf(
        Reserva(clases[1], "08:00 AM", "Completada"),
        Reserva(clases[3], "Mar 05:00 PM", "Completada")
    )
}
