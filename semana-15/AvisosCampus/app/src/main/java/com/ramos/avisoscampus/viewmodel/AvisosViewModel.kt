package com.ramos.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramos.avisoscampus.data.AvisosRepository
import com.ramos.avisoscampus.model.Aviso
import com.ramos.avisoscampus.model.Origen
import com.ramos.avisoscampus.model.PermisoUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class AvisosViewModel(
    private val repo: AvisosRepository,
    posponer: Flow<Unit> = emptyFlow()
) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    init {
        viewModelScope.launch {
            posponer.collect { recordarEn(10) }
        }
    }

    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
        if (concedido) _mensaje.value = null
    }

    fun noSePudoMostrar() {
        _mensaje.value = "Sin permiso: el aviso se guardó en el historial, pero no se mostró"
    }

    fun avisarAhora() {
        viewModelScope.launch {
            val aviso = repo.registrar("Tarea lista", "Tu entrega fue registrada", Origen.BOTON)
            _eventos.emit(aviso)
        }
    }

    private var trabajo: Job? = null
    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()

    fun recordarEn(segundos: Int = 10) {
        if (trabajo?.isActive == true) return
        trabajo = viewModelScope.launch {
            try {
                for (s in segundos downTo 1) {
                    _faltan.value = s
                    delay(1000)
                }
                val aviso = repo.registrar("Recordatorio", "Revisa tu laboratorio pendiente", Origen.RECORDATORIO)
                _eventos.emit(aviso.copy(urgente = true))
            } finally {
                _faltan.value = null
            }
        }
    }

    fun cancelarRecordatorio() {
        trabajo?.cancel()
    }

    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion: StateFlow<Boolean> = _escuchandoAvion.asStateFlow()

    fun cambiarEscucha(valor: Boolean) {
        _escuchandoAvion.value = valor
    }

    fun alCambiarModoAvion(activo: Boolean) {
        viewModelScope.launch {
            val texto = if (activo) "Activado" else "Desactivado"
            val aviso = repo.registrar("Modo avión", texto, Origen.MODO_AVION)
            _eventos.emit(aviso)
        }
    }

    private val _escuchandoCargador = MutableStateFlow(false)
    val escuchandoCargador: StateFlow<Boolean> = _escuchandoCargador.asStateFlow()

    fun cambiarEscuchaCargador(valor: Boolean) {
        _escuchandoCargador.value = valor
    }

    fun alCambiarCargador(conectado: Boolean) {
        viewModelScope.launch {
            val texto = if (conectado) "Conectado" else "Desconectado"
            val aviso = repo.registrar("Cargador", texto, Origen.CARGADOR)
            _eventos.emit(aviso)
        }
    }
}
