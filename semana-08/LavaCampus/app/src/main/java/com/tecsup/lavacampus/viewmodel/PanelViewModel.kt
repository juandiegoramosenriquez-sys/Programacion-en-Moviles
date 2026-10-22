package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.graficos.DatoGrafico
import com.tecsup.lavacampus.ui.theme.colorEstado
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class PanelViewModel(repo: LavaRepository) : ViewModel() {

    private val hoy = LocalDate.now().toString()
    private val lunes = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()

    val pedidosHoy: StateFlow<Int> = repo.observarPedidosDeFecha(hoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val enProceso: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val porRecoger: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.LISTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ingresosSemana: StateFlow<Double> = repo.observarIngresosDesde(lunes)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val promedioCalificacion: StateFlow<Double> = repo.observarPromedioCalificacion()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cantidadCalificados: StateFlow<Int> = repo.observarCantidadCalificados()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val atrasados: StateFlow<Int> = repo.observarPedidosAtrasados(hoy)
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO),
        repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO),
        repo.observarCantidadPorEstado(EstadoPedido.LISTO),
        repo.observarCantidadPorEstado(EstadoPedido.ENTREGADO)
    ) { recibido, proceso, listo, entregado ->
        listOf(
            dato(EstadoPedido.RECIBIDO, recibido),
            dato(EstadoPedido.EN_PROCESO, proceso),
            dato(EstadoPedido.LISTO, listo),
            dato(EstadoPedido.ENTREGADO, entregado)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarIngresosPorServicio(lunes)
        .map { lista -> lista.map { DatoGrafico(it.nombre, it.total) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun dato(estado: EstadoPedido, cantidad: Int) =
        DatoGrafico(estado.etiqueta, cantidad.toDouble(), colorEstado(estado))
}
