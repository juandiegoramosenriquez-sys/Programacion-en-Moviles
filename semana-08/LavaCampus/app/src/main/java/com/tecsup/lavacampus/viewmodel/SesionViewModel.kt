package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    suspend fun restaurarSesion(): Usuario? {
        val perfil = prefs.perfilActivo.first()
        val id = prefs.usuarioId.first()
        if (perfil.isEmpty() || id == 0) return null
        val usuario = repo.obtenerUsuario(id)
        _usuarioActual.value = usuario
        return usuario
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        viewModelScope.launch {
            if (correo.isBlank() || clave.isBlank()) {
                _error.value = "Completa el correo y la contraseña"
                return@launch
            }
            val usuario = repo.login(correo.trim(), clave)
            when {
                usuario == null -> _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name -> _error.value =
                    if (rolSeleccionado == Rol.ADMIN) "Esta cuenta no es de administrador"
                    else "Esta cuenta no es de estudiante"
                else -> {
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    _error.value = null
                    _usuarioActual.value = usuario
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
        viewModelScope.launch { prefs.cerrarSesion() }
    }
}
