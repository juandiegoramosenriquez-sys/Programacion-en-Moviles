package com.ramos.centrotareas.data

import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    fun tareaConProgreso(): Flow<Float>
    suspend fun contarPrimos(hasta: Int): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}
