package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.Unidad
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

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

    fun textoRecibo(): String {
        val actual = pedido.value ?: return ""
        val p = actual.pedido
        val lineas = actual.detalles.joinToString("\n") { d ->
            val cantidad = if (d.servicio.unidad == Unidad.KG.name) {
                String.format(Locale.US, "%.1f kg", d.detalle.cantidad)
            } else {
                "${d.detalle.cantidad.toInt()} ${d.servicio.unidad.lowercase()}"
            }
            "• ${d.servicio.nombre}: $cantidad x ${soles(d.detalle.precioUnitario)} = ${soles(d.detalle.subtotal)}"
        }
        return buildString {
            appendLine("LavaCampus · Recibo del pedido #${p.id}")
            appendLine("Estudiante: ${actual.usuario.nombre} (${actual.usuario.codigo})")
            appendLine("Fecha: ${p.fechaRegistro} · Entrega: ${p.fechaEntrega}")
            appendLine("Estado: ${EstadoPedido.valueOf(p.estado).etiqueta}")
            appendLine()
            appendLine(lineas)
            if (p.descuento > 0.0) appendLine("Descuento por volumen: - ${soles(p.descuento)}")
            appendLine("Total: ${soles(p.total)}")
            appendLine(if (p.pagado) "Pago: pagado" else "Pago: pendiente, se paga al recoger")
            p.casillero?.let { appendLine("Casillero: $it") }
        }.trim()
    }

    private fun soles(monto: Double) = String.format(Locale.US, "S/ %.2f", monto)
}
