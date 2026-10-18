package com.tecsup.lavacampus.data.repository

import com.tecsup.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

interface LavaRepository {
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
}
