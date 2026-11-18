package com.ramos.campusgps.data

import kotlinx.coroutines.flow.Flow

interface PreferenciasRepository {
    val ultimoLugar: Flow<Int?>
    suspend fun guardarUltimoLugar(id: Int)
}
