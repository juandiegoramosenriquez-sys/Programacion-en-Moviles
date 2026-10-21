package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: LavaRepository) : ViewModel() {
    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activos: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    fun guardar(s: Servicio, onListo: () -> Unit) {
        when {
            s.nombre.isBlank() -> _mensaje.value = "El nombre es obligatorio"
            s.precio <= 0.0 -> _mensaje.value = "El precio debe ser mayor que 0"
            else -> viewModelScope.launch {
                repo.guardarServicio(s.copy(nombre = s.nombre.trim(), descripcion = s.descripcion.trim()))
                _mensaje.value = null
                onListo()
            }
        }
    }

    fun eliminar(s: Servicio, onListo: () -> Unit) {
        viewModelScope.launch {
            if (repo.servicioTieneDetalles(s.id)) {
                _mensaje.value = "Este servicio ya está en pedidos: no se puede eliminar, solo desactivar"
            } else {
                repo.eliminarServicio(s)
                _mensaje.value = null
                onListo()
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
