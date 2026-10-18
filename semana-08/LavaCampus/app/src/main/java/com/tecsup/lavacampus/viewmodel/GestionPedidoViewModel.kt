package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Casilleros
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestionPedidoViewModel(
    private val repo: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val casillerosOcupados: StateFlow<List<String>> = combine(
        repo.observarCasillerosOcupados(),
        pedido
    ) { ocupados, actual ->
        ocupados.filter { it != actual?.pedido?.casillero }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _casillero = MutableStateFlow<String?>(null)
    val casillero: StateFlow<String?> = _casillero

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    fun elegirCasillero(c: String) {
        if (c in casillerosOcupados.value) return
        _casillero.value = c
        _mensaje.value = null
    }

    fun marcarPagado(pagado: Boolean) {
        viewModelScope.launch { repo.marcarPagado(pedidoId, pagado) }
    }

    fun avanzar() {
        val p = pedido.value?.pedido ?: return
        val siguiente = EstadoPedido.valueOf(p.estado).siguiente()
        if (siguiente == null) {
            _mensaje.value = "Este pedido ya no puede avanzar"
            return
        }
        val casilleroFinal = when (siguiente) {
            EstadoPedido.LISTO -> {
                val elegido = _casillero.value ?: p.casillero
                if (elegido == null || !Casilleros.esValido(elegido)) {
                    _mensaje.value = "Elige un casillero para marcarlo como listo"
                    return
                }
                if (elegido in casillerosOcupados.value) {
                    _mensaje.value = "El casillero $elegido está ocupado"
                    return
                }
                elegido
            }
            EstadoPedido.ENTREGADO -> {
                if (!p.pagado) {
                    _mensaje.value = "Primero marca el pedido como cobrado"
                    return
                }
                p.casillero
            }
            else -> p.casillero
        }
        _mensaje.value = null
        viewModelScope.launch { repo.cambiarEstado(pedidoId, siguiente, casilleroFinal) }
    }

    fun cancelar() {
        val p = pedido.value?.pedido ?: return
        val estado = EstadoPedido.valueOf(p.estado)
        if (estado != EstadoPedido.RECIBIDO && estado != EstadoPedido.EN_PROCESO) {
            _mensaje.value = "Solo se cancela un pedido recibido o en proceso"
            return
        }
        _mensaje.value = null
        viewModelScope.launch { repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null) }
    }
}
