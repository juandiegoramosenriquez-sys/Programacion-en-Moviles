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
import kotlinx.coroutines.launch

class DetallePedidoViewModel(
    private val repo: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val puedeCancelar: StateFlow<Boolean> = pedido
        .map { it?.pedido?.estado == EstadoPedido.RECIBIDO.name }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val puedeCalificar: StateFlow<Boolean> = pedido
        .map { it?.pedido?.estado == EstadoPedido.ENTREGADO.name }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun cancelar() {
        if (!puedeCancelar.value) return
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null)
        }
    }

    fun calificar(estrellas: Int) {
        if (!puedeCalificar.value || estrellas !in 1..5) return
        viewModelScope.launch {
            repo.calificarPedido(pedidoId, estrellas)
        }
    }
}
