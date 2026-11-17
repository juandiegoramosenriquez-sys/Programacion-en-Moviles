package com.ramos.centrotareas.data

import kotlinx.coroutines.delay
import java.io.IOException

class TareasRepositoryLocal : TareasRepository {
    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    override suspend fun contarPrimos(hasta: Int): Int {
        return 0
    }
}
