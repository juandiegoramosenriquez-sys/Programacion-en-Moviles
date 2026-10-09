package com.saludplus.citas.data.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val calificacion: Double,
    val aniosExperiencia: Int,
    val sedeId: Int = 1
)
