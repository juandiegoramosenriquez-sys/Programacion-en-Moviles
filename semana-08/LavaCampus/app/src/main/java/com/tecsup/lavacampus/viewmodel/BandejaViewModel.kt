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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class BandejaViewModel(private val repo: LavaRepository) : ViewModel() {

    private val hoy = LocalDate.now()

    private val _filtro = MutableStateFlow<EstadoPedido?>(null)
    val filtro: StateFlow<EstadoPedido?> = _filtro

    private val _soloAtrasados = MutableStateFlow(false)
    val soloAtrasados: StateFlow<Boolean> = _soloAtrasados

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    private val idsAtrasados = repo.observarPedidosAtrasados(hoy.toString()).map { it.toSet() }

    val atrasados: StateFlow<Set<Int>> = idsAtrasados
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val pedidos: StateFlow<List<PedidoConDetalle>> = combine(
        repo.observarPedidos(),
        _filtro,
        _busqueda,
        _soloAtrasados,
        idsAtrasados
    ) { lista, filtro, texto, soloAtrasados, ids ->
        lista.filter { p ->
            val coincideEstado = filtro == null || p.pedido.estado == filtro.name
            val coincideAtraso = !soloAtrasados || p.pedido.id in ids
            val coincideTexto = texto.isBlank() ||
                p.usuario.nombre.contains(texto.trim(), ignoreCase = true) ||
                p.pedido.id.toString() == texto.trim().removePrefix("#")
            coincideEstado && coincideAtraso && coincideTexto
        }.sortedByDescending { it.pedido.id in ids }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirFiltro(estado: EstadoPedido?) {
        _filtro.value = estado
        _soloAtrasados.value = false
    }

    fun verAtrasados() {
        _filtro.value = null
        _soloAtrasados.value = true
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun diasAtraso(p: PedidoConDetalle): Long =
        ChronoUnit.DAYS.between(LocalDate.parse(p.pedido.fechaEntrega), hoy)

    fun iniciar(p: PedidoConDetalle) {
        if (p.pedido.estado != EstadoPedido.RECIBIDO.name) return
        viewModelScope.launch {
            repo.cambiarEstado(p.pedido.id, EstadoPedido.EN_PROCESO, null)
        }
    }
}
