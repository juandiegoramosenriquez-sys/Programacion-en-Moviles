package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class InicioViewModel(repo: LavaRepository, usuarioId: Int) : ViewModel() {

    private val activos = setOf(EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO)

    private val pedidos = repo.observarPedidosDeUsuario(usuarioId)

    val pedidoActivo: StateFlow<PedidoConDetalle?> = pedidos
        .map { lista -> lista.firstOrNull { EstadoPedido.valueOf(it.pedido.estado) in activos } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val recientes: StateFlow<List<PedidoConDetalle>> = pedidos
        .map { it.take(2) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
