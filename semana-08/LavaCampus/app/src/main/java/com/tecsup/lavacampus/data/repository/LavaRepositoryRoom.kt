package com.tecsup.lavacampus.data.repository

import com.tecsup.lavacampus.data.local.LavaDatabase
import com.tecsup.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

class LavaRepositoryRoom(private val db: LavaDatabase) : LavaRepository {
    private val servicioDao = db.servicioDao()

    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        servicioDao.insertar(s)
    }

    override suspend fun eliminarServicio(s: Servicio) {
        servicioDao.eliminar(s)
    }

    override suspend fun cambiarActivo(id: Int, activo: Boolean) {
        servicioDao.cambiarActivo(id, activo)
    }
}
