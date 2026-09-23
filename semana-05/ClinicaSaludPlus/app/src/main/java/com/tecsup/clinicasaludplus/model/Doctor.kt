package com.tecsup.clinicasaludplus.model

data class Doctor(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val horario: String,
    val calificacion: Double,
    val experiencia: Int,
    val descripcion: String
)

enum class EstadoCita { CONFIRMADA, COMPLETADA, CANCELADA }

data class Cita(
    val id: Int,
    val doctor: Doctor,
    val fecha: String,
    val hora: String,
    val estado: EstadoCita
)

object DataSource {
    val especialidades = listOf("Todos", "Cardiología", "Pediatría", "Medicina General")

    val doctores = listOf(
        Doctor(1, "Dr. Carlos Pérez", "Cardiología", "09:00 AM - 01:00 PM", 4.9, 12,
            "Especialista en arritmias e hipertensión."),
        Doctor(2, "Dra. Ana Gómez", "Pediatría", "02:00 PM - 06:00 PM", 4.7, 8,
            "Atención integral del niño y control de crecimiento."),
        Doctor(3, "Dr. Luis Torres", "Medicina General", "08:00 AM - 12:00 PM", 4.8, 15,
            "Chequeos generales y prevención de enfermedades."),
        Doctor(4, "Dra. María Huamán", "Cardiología", "03:00 PM - 07:00 PM", 4.6, 10,
            "Rehabilitación cardiaca y control del colesterol.")
    )

    // Opciones de selección única para agendar
    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horas = listOf("9:00", "10:30", "3:00")

    // Cita de ejemplo ya atendida, para que "Mis citas" no empiece vacía
    val citasIniciales = listOf(
        Cita(1, doctores[2], "Mié 15", "3:00", EstadoCita.COMPLETADA)
    )
}
