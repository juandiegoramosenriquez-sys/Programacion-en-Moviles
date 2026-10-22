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
import java.time.LocalDate

class SesionViewModel(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val recibirPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val limitePedidos: StateFlow<Int> = prefs.limitePedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, 3)

    fun cambiarLimitePedidos(limite: Int) {
        if (limite !in 1..5) return
        viewModelScope.launch { prefs.guardarLimitePedidos(limite) }
    }

    fun cambiarModoOscuro(activo: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(activo) }
    }

    fun cambiarNotificaciones(activo: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(activo) }
    }

    fun cambiarRecibirPedidos(activo: Boolean) {
        viewModelScope.launch { prefs.guardarRecibirPedidos(activo) }
    }

    private val _rolLogin = MutableStateFlow(Rol.CLIENTE)
    val rolLogin: StateFlow<Rol> = _rolLogin

    fun cambiarPerfil() {
        val actual = _usuarioActual.value ?: return
        _rolLogin.value = if (actual.rol == Rol.ADMIN.name) Rol.CLIENTE else Rol.ADMIN
        _error.value = null
        cerrarSesion()
    }

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

    private val _errorRegistro = MutableStateFlow<String?>(null)
    val errorRegistro: StateFlow<String?> = _errorRegistro

    fun registrar(nombre: String, correo: String, clave: String, confirmar: String) {
        viewModelScope.launch {
            val correoLimpio = correo.trim().lowercase()
            _errorRegistro.value = when {
                nombre.isBlank() || correo.isBlank() || clave.isBlank() ->
                    "Completa todos los campos"
                nombre.trim().length < 3 -> "Escribe tu nombre completo"
                !Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""").matches(correoLimpio) -> "Escribe un correo válido"
                clave.length < 4 -> "La contraseña debe tener al menos 4 caracteres"
                clave != confirmar -> "Las contraseñas no coinciden"
                repo.existeCorreo(correoLimpio) -> "Ese correo ya está registrado"
                else -> null
            }
            if (_errorRegistro.value != null) return@launch
            val nuevo = Usuario(
                nombre = nombre.trim(),
                codigo = generarCodigo(),
                correo = correoLimpio,
                clave = clave,
                rol = Rol.CLIENTE.name
            )
            val id = repo.registrarUsuario(nuevo).toInt()
            prefs.guardarSesion(Rol.CLIENTE, id)
            _usuarioActual.value = nuevo.copy(id = id)
        }
    }

    private suspend fun generarCodigo(): String {
        val anio = LocalDate.now().year
        var codigo: String
        do {
            codigo = "U$anio" + (10000..99999).random()
        } while (repo.existeCodigo(codigo))
        return codigo
    }

    fun limpiarErrorRegistro() {
        _errorRegistro.value = null
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
        viewModelScope.launch { prefs.cerrarSesion() }
    }
}
