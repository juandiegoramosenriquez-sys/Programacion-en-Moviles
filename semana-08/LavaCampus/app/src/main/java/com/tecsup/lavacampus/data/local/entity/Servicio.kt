package com.tecsup.lavacampus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicios")
data class Servicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val unidad: String,
    val precio: Double,
    val horasEntrega: Int,
    val activo: Boolean = true,
    val icono: String
)
