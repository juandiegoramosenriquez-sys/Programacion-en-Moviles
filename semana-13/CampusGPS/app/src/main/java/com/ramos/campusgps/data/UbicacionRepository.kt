package com.ramos.campusgps.data

import com.ramos.campusgps.model.Posicion
import kotlinx.coroutines.flow.Flow

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun seguirPosicion(): Flow<Posicion>
    fun playServicesDisponible(): Boolean
}
