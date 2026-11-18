package com.ramos.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramos.campusgps.data.LugaresRepository
import com.ramos.campusgps.data.UbicacionRepository
import com.ramos.campusgps.model.Lugar
import com.ramos.campusgps.model.LugarConDistancia
import com.ramos.campusgps.model.Posicion
import com.ramos.campusgps.model.TipoError
import com.ramos.campusgps.model.UbicacionUi
import com.ramos.campusgps.util.distanciaMetros
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    val categorias: List<String> = lugaresRepo.obtenerLugares().map { it.categoria }.distinct()

    private val _categoria = MutableStateFlow<String?>(null)
    val categoria: StateFlow<String?> = _categoria.asStateFlow()

    val lugaresVisibles: StateFlow<List<LugarConDistancia>> = combine(_lugares, _categoria) { lista, cat ->
        if (cat == null) lista else lista.filter { it.lugar.categoria == cat }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _lugares.value)

    private var seguimiento: Job? = null

    private val _siguiendo = MutableStateFlow(false)
    val siguiendo: StateFlow<Boolean> = _siguiendo.asStateFlow()

    val radios: List<Int> = listOf(100, 200, 500)

    private val _radio = MutableStateFlow(200)
    val radio: StateFlow<Int> = _radio.asStateFlow()

    val dentroDelRadio: StateFlow<Set<Int>> = combine(_lugares, _radio) { lista, r ->
        lista.filter { (it.metros ?: Float.MAX_VALUE) <= r }.map { it.lugar.id }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val masCercano: StateFlow<Lugar?> = lugaresVisibles
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun buscar() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    fun iniciarSeguimiento() {
        if (seguimiento != null) return
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        _siguiendo.value = true
        seguimiento = viewModelScope.launch {
            ubicacionRepo.seguirPosicion()
                .catch {
                    _estado.value = errorDe(it)
                    detenerSeguimiento()
                }
                .collect {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
        }
    }

    fun detenerSeguimiento() {
        seguimiento?.cancel()
        seguimiento = null
        _siguiendo.value = false
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    fun elegirRadio(metros: Int) {
        _radio.value = metros
    }

    fun filtrar(cat: String?) {
        _categoria.value = cat
        val visible = _lugares.value.any { it.lugar.id == _seleccionado.value && (cat == null || it.lugar.categoria == cat) }
        if (!visible) _seleccionado.value = null
    }

    private fun actualizarDistancias(pos: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }
            .sortedBy { it.metros }
    }

    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error("Falta el permiso de ubicación", TipoError.SIN_PERMISO)
        is IllegalStateException -> UbicacionUi.Error("Activa el GPS e intenta de nuevo", TipoError.GPS_APAGADO)
        else -> UbicacionUi.Error(e.message ?: "No se pudo obtener la ubicación", TipoError.OTRO)
    }
}
