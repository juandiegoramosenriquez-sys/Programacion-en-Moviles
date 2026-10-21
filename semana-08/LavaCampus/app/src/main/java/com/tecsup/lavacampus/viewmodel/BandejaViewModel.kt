package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BandejaViewModel(private val repo: LavaRepository) : ViewModel() {

    private val _filtro = MutableStateFlow<EstadoPedido?>(null)
    val filtro: StateFlow<EstadoPedido?> = _filtro

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    val pedidos: StateFlow<List<PedidoConDetalle>> = combine(
        repo.observarPedidos(),
        _filtro,
        _busqueda
    ) { lista, filtro, texto ->
        lista.filter { p ->
            val coincideEstado = filtro == null || p.pedido.estado == filtro.name
            val coincideTexto = texto.isBlank() ||
                p.usuario.nombre.contains(texto.trim(), ignoreCase = true) ||
                p.pedido.id.toString() == texto.trim().removePrefix("#")
            coincideEstado && coincideTexto
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirFiltro(estado: EstadoPedido?) {
        _filtro.value = estado
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun iniciar(p: PedidoConDetalle) {
        if (p.pedido.estado != EstadoPedido.RECIBIDO.name) return
        viewModelScope.launch {
            repo.cambiarEstado(p.pedido.id, EstadoPedido.EN_PROCESO, null)
        }
    }
}
