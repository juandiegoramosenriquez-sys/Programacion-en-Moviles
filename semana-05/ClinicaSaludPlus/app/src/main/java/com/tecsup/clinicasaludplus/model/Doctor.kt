package com.tecsup.clinicasaludplus.model

data class Doctor(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val horario: String
)

object DataSource {
    val especialidades = listOf("Todos", "Cardiología", "Pediatría", "Medicina General")
    
    val doctores = listOf(
        Doctor(1, "Dr. Carlos Pérez", "Cardiología", "09:00 AM - 01:00 PM"),
        Doctor(2, "Dra. Ana Gómez", "Pediatría", "02:00 PM - 06:00 PM"),
        Doctor(3, "Dr. Luis Torres", "Medicina General", "08:00 AM - 12:00 PM"),
        Doctor(4, "Dra. María Huamán", "Cardiología", "03:00 PM - 07:00 PM")
    )
}
