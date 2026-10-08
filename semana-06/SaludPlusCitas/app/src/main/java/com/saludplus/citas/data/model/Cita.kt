package com.saludplus.citas.data.model

data class Cita(
    val id: Int,
    val correoUsuario: String,
    val especialidadId: Int,
    val medicoId: Int,
    val fecha: String,
    val hora: String
)
