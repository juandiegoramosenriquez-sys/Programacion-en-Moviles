package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.LineaPedido
import com.tecsup.lavacampus.model.Unidad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class NuevoPedidoViewModel(
    private val repo: LavaRepository,
    private val usuarioId: Int,
    prefs: PreferenciasStore,
    val repetirId: Int = 0
) : ViewModel() {

    val recibiendo: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val limite: StateFlow<Int> = prefs.limitePedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, 3)

    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _cantidades = MutableStateFlow<Map<Int, Double>>(emptyMap())
    val cantidades: StateFlow<Map<Int, Double>> = _cantidades

    private val _nota = MutableStateFlow("")
    val nota: StateFlow<String> = _nota

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _creado = MutableStateFlow(false)
    val creado: StateFlow<Boolean> = _creado

    val subtotal: StateFlow<Double> = combine(servicios, _cantidades) { lista, cantidades ->
        lista.sumOf { (cantidades[it.id] ?: 0.0) * it.precio }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val kilosLavado: StateFlow<Double> = combine(servicios, _cantidades) { lista, cantidades ->
        lista.filter { it.unidad == Unidad.KG.name }.sumOf { cantidades[it.id] ?: 0.0 }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val descuento: StateFlow<Double> = combine(servicios, _cantidades) { lista, cantidades ->
        calcularDescuento(lista, cantidades)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val total: StateFlow<Double> = combine(subtotal, descuento) { sub, desc -> sub - desc }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val fechaEntrega: StateFlow<String> = combine(servicios, _cantidades) { lista, cantidades ->
        calcularFecha(lista.filter { (cantidades[it.id] ?: 0.0) > 0.0 })
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    init {
        if (repetirId != 0) cargarPedidoAnterior()
    }

    private fun cargarPedidoAnterior() {
        viewModelScope.launch {
            val anterior = repo.observarPedido(repetirId).first() ?: return@launch
            val activos = repo.observarServiciosActivos().first()
            val disponibles = anterior.detalles.filter { d -> activos.any { it.id == d.servicio.id } }
            _cantidades.value = disponibles.associate { d ->
                val servicio = activos.first { it.id == d.servicio.id }
                servicio.id to minOf(d.detalle.cantidad, maximo(servicio))
            }
            _mensaje.value = if (disponibles.size < anterior.detalles.size) {
                "Algunos servicios del pedido #$repetirId ya no están disponibles"
            } else {
                null
            }
        }
    }

    fun paso(s: Servicio): Double = if (s.unidad == Unidad.KG.name) 0.5 else 1.0

    fun maximo(s: Servicio): Double = if (s.unidad == Unidad.KG.name) 10.0 else 30.0

    fun cambiarCantidad(servicioId: Int, delta: Double) {
        val servicio = servicios.value.find { it.id == servicioId } ?: return
        val actual = _cantidades.value[servicioId] ?: 0.0
        val nueva = when {
            actual + delta < 1.0 && delta < 0 -> 0.0
            actual + delta < 1.0 -> 1.0
            else -> minOf(actual + delta, maximo(servicio))
        }
        _cantidades.value = _cantidades.value + (servicioId to nueva)
        _mensaje.value = null
    }

    fun cambiarNota(texto: String) {
        _nota.value = texto
    }

    fun confirmar() {
        viewModelScope.launch {
            val elegidos = servicios.value.filter { (_cantidades.value[it.id] ?: 0.0) > 0.0 }
            if (elegidos.isEmpty()) return@launch
            if (!recibiendo.value) {
                _mensaje.value = "La lavandería no está recibiendo pedidos"
                return@launch
            }
            if (repo.contarActivos(usuarioId) >= limite.value) {
                _mensaje.value = if (limite.value == 1) "Ya tienes 1 pedido activo" else "Ya tienes ${limite.value} pedidos activos"
                return@launch
            }
            val lineas = elegidos.map { LineaPedido(it.id, _cantidades.value.getValue(it.id)) }
            repo.crearPedido(usuarioId, lineas, calcularFecha(elegidos), _nota.value.trim(), calcularDescuento(elegidos, _cantidades.value))
            _creado.value = true
        }
    }

    private fun calcularDescuento(lista: List<Servicio>, cantidades: Map<Int, Double>): Double {
        val lavado = lista.filter { it.unidad == Unidad.KG.name }
        val kilos = lavado.sumOf { cantidades[it.id] ?: 0.0 }
        if (kilos <= KILOS_PARA_DESCUENTO) return 0.0
        return lavado.sumOf { (cantidades[it.id] ?: 0.0) * it.precio } * PORCENTAJE_DESCUENTO
    }

    private fun calcularFecha(elegidos: List<Servicio>): String {
        if (elegidos.isEmpty()) return ""
        val dias = elegidos.maxOf { it.horasEntrega } / 24
        var fecha = LocalDate.now().plusDays(dias.toLong())
        if (fecha.dayOfWeek == DayOfWeek.SUNDAY) fecha = fecha.plusDays(1)
        return fecha.toString()
    }

    companion object {
        const val KILOS_PARA_DESCUENTO = 5.0
        const val PORCENTAJE_DESCUENTO = 0.10
    }
}
