package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

object Repositorio {

    val usuarios = mutableStateListOf(
        Usuario(
            nombres = "Paciente",
            apellidos = "Demo",
            dni = "12345678",
            correo = "demo@saludplus.com",
            celular = "999999999",
            password = "123456"
        )
    )

    val especialidades = mutableStateListOf(
        Especialidad(1, "Medicina General", "Consulta general y chequeos", "🩺"),
        Especialidad(2, "Cardiología", "Corazón y sistema circulatorio", "❤️"),
        Especialidad(3, "Pediatría", "Atención a niños", "🧸"),
        Especialidad(4, "Dermatología", "Piel, cabello y uñas", "🧴"),
        Especialidad(5, "Oftalmología", "Salud visual", "👁️"),
        Especialidad(6, "Traumatología", "Huesos y articulaciones", "🦴")
    )

    val medicos = mutableStateListOf(
        Medico(1, "Dr. Carlos Mendoza", 1, 4.8, 12),
        Medico(2, "Dra. Lucía Torres", 1, 4.5, 8),
        Medico(3, "Dr. Jorge Salazar", 2, 4.9, 20),
        Medico(4, "Dra. Ana Quispe", 2, 4.6, 10),
        Medico(5, "Dra. María Flores", 3, 4.7, 15),
        Medico(6, "Dr. Luis Paredes", 3, 4.3, 6),
        Medico(7, "Dra. Sofía Ramírez", 4, 4.8, 9),
        Medico(8, "Dr. Pedro Castillo", 5, 4.4, 11),
        Medico(9, "Dr. Miguel Rojas", 6, 4.6, 14)
    )

    val citas = mutableStateListOf<Cita>()

    val horariosBase = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "14:00", "14:30", "15:00", "15:30"
    )

    var usuarioActual by mutableStateOf<Usuario?>(null)

    fun registrarUsuario(usuario: Usuario): Boolean {
        if (usuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) }) return false
        usuarios.add(usuario)
        usuarioActual = usuario
        return true
    }

    fun iniciarSesion(correo: String, password: String): Boolean {
        usuarioActual = usuarios.find { it.correo.equals(correo, ignoreCase = true) && it.password == password }
        return usuarioActual != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> {
        if (texto.isBlank()) return especialidades
        return especialidades.filter { it.nombre.contains(texto, ignoreCase = true) }
    }

    fun especialidadesDestacadas(cantidad: Int = 4): List<Especialidad> {
        return especialidades.take(cantidad)
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        return medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(texto, ignoreCase = true) }
    }

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horariosBase.filter { it !in ocupados }
    }

    fun agendarCita(medicoId: Int, fecha: String, hora: String): Cita? {
        val ocupado = citas.any { it.medicoId == medicoId && it.fecha == fecha && it.hora == hora }
        if (ocupado) return null

        val medico = obtenerMedico(medicoId) ?: return null

        val cita = Cita(
            id = (citas.maxOfOrNull { it.id } ?: 0) + 1,
            correoUsuario = usuarioActual?.correo ?: "",
            especialidadId = medico.especialidadId,
            medicoId = medicoId,
            fecha = fecha,
            hora = hora
        )
        citas.add(cita)
        return cita
    }

    fun obtenerCita(id: Int): Cita? {
        return citas.find { it.id == id }
    }

    fun citasDelUsuario(): List<Cita> {
        return citas
            .filter { it.correoUsuario == usuarioActual?.correo }
            .sortedWith(compareBy({ it.fecha }, { it.hora }))
    }

    fun cancelarCita(id: Int): Boolean {
        return citas.removeIf { it.id == id }
    }
}
