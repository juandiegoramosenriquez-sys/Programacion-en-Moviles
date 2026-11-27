package com.ramos.avisoscampus.data

import com.ramos.avisoscampus.data.local.AvisoDao
import com.ramos.avisoscampus.data.local.AvisoEntity
import com.ramos.avisoscampus.model.Aviso
import com.ramos.avisoscampus.model.Origen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface AvisosRepository {
    val avisos: Flow<List<Aviso>>
    suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso
}

private fun horaActual(): String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

class AvisosRepositoryMemoria : AvisosRepository {
    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    override val avisos: Flow<List<Aviso>> = _avisos.asStateFlow()
    private var siguiente = 1

    override suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        val aviso = Aviso(siguiente++, titulo, texto, origen, horaActual())
        _avisos.value = listOf(aviso) + _avisos.value
        return aviso
    }
}

class AvisosRepositoryRoom(private val dao: AvisoDao) : AvisosRepository {

    override val avisos: Flow<List<Aviso>> = dao.todos().map { lista -> lista.map { it.aAviso() } }

    override suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        val entidad = AvisoEntity(titulo = titulo, texto = texto, origen = origen.name, hora = horaActual())
        val id = dao.insertar(entidad).toInt()
        return entidad.copy(id = id).aAviso()
    }
}
