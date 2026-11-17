package com.ramos.centrotareas.data

import kotlinx.coroutines.flow.Flow

interface HistorialRepository {
    val historial: Flow<List<String>>
    suspend fun agregar(resultado: String)
}
