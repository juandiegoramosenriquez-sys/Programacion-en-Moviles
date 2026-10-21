package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.Unidad
import com.tecsup.lavacampus.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class MisPedidosViewModel(
    private val repo: LavaRepository,
    usuarioId: Int
) : ViewModel() {

    private val estadosActivos = setOf(EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO)

    private val pedidos = repo.observarPedidosDeUsuario(usuarioId)

    val activos: StateFlow<List<PedidoConDetalle>> = pedidos
        .map { lista -> lista.filter { EstadoPedido.valueOf(it.pedido.estado) in estadosActivos } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val historial: StateFlow<List<PedidoConDetalle>> = pedidos
        .map { lista -> lista.filter { EstadoPedido.valueOf(it.pedido.estado) !in estadosActivos } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPedidos: StateFlow<Int> = pedidos
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val kilosLavados: StateFlow<Double> = pedidos
        .map { lista ->
            lista.filter { it.pedido.estado != EstadoPedido.CANCELADO.name }
                .flatMap { it.detalles }
                .filter { it.servicio.unidad == Unidad.KG.name }
                .sumOf { it.detalle.cantidad }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val gastoMensual: StateFlow<List<DatoGrafico>> = repo.observarGastoMensual(usuarioId)
        .map { lista -> ultimosMeses(lista.associate { it.mes to it.total }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ultimosMeses(emptyMap()))

    private fun ultimosMeses(gastos: Map<String, Double>): List<DatoGrafico> {
        val actual = YearMonth.now()
        val espanol = Locale.forLanguageTag("es")
        return (5 downTo 0).map { atras ->
            val mes = actual.minusMonths(atras.toLong())
            DatoGrafico(
                etiqueta = mes.month.getDisplayName(TextStyle.SHORT, espanol).replace(".", ""),
                valor = gastos[mes.toString()] ?: 0.0
            )
        }
    }

    fun puedeCancelar(p: PedidoConDetalle): Boolean = p.pedido.estado == EstadoPedido.RECIBIDO.name

    fun cancelar(p: PedidoConDetalle) {
        if (!puedeCancelar(p)) return
        viewModelScope.launch {
            repo.cambiarEstado(p.pedido.id, EstadoPedido.CANCELADO, null)
        }
    }
}
