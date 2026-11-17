package com.ramos.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramos.centrotareas.data.HistorialRepository
import com.ramos.centrotareas.data.TareasRepository
import com.ramos.centrotareas.model.CargaUi
import com.ramos.centrotareas.model.ComparacionTiempos
import com.ramos.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TareasViewModel(
    private val repo: TareasRepository,
    private val historialRepo: HistorialRepository
) : ViewModel() {
    val historial: StateFlow<List<String>> = historialRepo.historial
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private suspend fun guardar(resultado: String) {
        val hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        historialRepo.agregar("$hora  $resultado")
    }

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private var trabajo: Job? = null

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    fun tareaLarga() {
        if (_corriendo.value) return
        _corriendo.value = true
        trabajo = viewModelScope.launch {
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                repo.tareaConProgreso().collect { _progreso.value = it }
                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        if (hasta < 2 || _primos.value is PrimosUi.Calculando) return
        _primos.value = PrimosUi.Calculando()
        trabajoPrimos = viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            try {
                repo.contarPrimosConProgreso(hasta).collect { avance ->
                    if (avance.terminado) {
                        val ms = System.currentTimeMillis() - inicio
                        _primos.value = PrimosUi.Listo(avance.cantidad, ms)
                        guardar("Primos hasta $hasta: ${avance.cantidad} en $ms ms")
                    } else {
                        _primos.value = PrimosUi.Calculando(avance.porcentaje, avance.cantidad)
                    }
                }
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _comparacion = MutableStateFlow(ComparacionTiempos())
    val comparacion: StateFlow<ComparacionTiempos> = _comparacion.asStateFlow()

    fun limpiarComparacion() {
        _comparacion.value = ComparacionTiempos()
    }

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    private val _limiteTiempo = MutableStateFlow(false)
    val limiteTiempo: StateFlow<Boolean> = _limiteTiempo.asStateFlow()

    fun cambiarLimiteTiempo(valor: Boolean) {
        _limiteTiempo.value = valor
    }

    private val _intento = MutableStateFlow(0)
    val intento: StateFlow<Int> = _intento.asStateFlow()

    private suspend fun <T> conReintento(bloque: suspend () -> T): T {
        var espera = ESPERA_INICIAL_MS
        repeat(INTENTOS - 1) {
            _intento.value = it + 1
            try {
                return bloque()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                delay(espera)
                espera *= 2
            }
        }
        _intento.value = INTENTOS
        return bloque()
    }

    private suspend fun <T> conLimite(bloque: suspend () -> T): T =
        if (_limiteTiempo.value) withTimeout(LIMITE_MS) { bloque() } else bloque()

    fun cargarUnaPorUna() {
        if (_carga.value is CargaUi.Cargando) return
        _carga.value = CargaUi.Cargando
        _intento.value = 0
        viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = conLimite {
                    val perfil = repo.cargarPerfil()
                    val pedidos = conReintento { repo.cargarPedidos(_simularError.value) }
                    Pair(perfil, pedidos)
                }
                val ms = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("Una por una", perfil, pedidos.size, ms)
                _comparacion.update { it.copy(unaPorUna = it.unaPorUna + ms) }
                guardar("Una por una: $ms ms")
            } catch (e: TimeoutCancellationException) {
                _carga.value = CargaUi.Error("Tardó demasiado: más de ${LIMITE_MS / 1000} s")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: " + e.message)
            }
        }
    }

    fun cargarALaVez() {
        if (_carga.value is CargaUi.Cargando) return
        _carga.value = CargaUi.Cargando
        _intento.value = 0
        viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = conLimite {
                    coroutineScope {
                        val a = async { repo.cargarPerfil() }
                        val b = async { conReintento { repo.cargarPedidos(_simularError.value) } }
                        Pair(a.await(), b.await())
                    }
                }
                val ms = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("A la vez", perfil, pedidos.size, ms)
                _comparacion.update { it.copy(aLaVez = it.aLaVez + ms) }
                guardar("A la vez: $ms ms")
            } catch (e: TimeoutCancellationException) {
                _carga.value = CargaUi.Error("Tardó demasiado: más de ${LIMITE_MS / 1000} s")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: " + e.message)
            }
        }
    }

    companion object {
        private const val LIMITE_MS = 4000L
        private const val INTENTOS = 3
        private const val ESPERA_INICIAL_MS = 500L
    }
}
