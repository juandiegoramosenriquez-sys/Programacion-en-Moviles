package com.saludplus.citas.data.model

data class Usuario(
    val nombres: String,
    val apellidos: String,
    val dni: String,
    val correo: String,
    val celular: String,
    val password: String
)
