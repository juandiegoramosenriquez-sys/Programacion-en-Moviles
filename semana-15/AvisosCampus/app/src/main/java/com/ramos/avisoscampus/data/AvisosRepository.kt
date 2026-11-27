package com.ramos.avisoscampus.data

import com.ramos.avisoscampus.model.Aviso
import com.ramos.avisoscampus.model.Origen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface AvisosRepository {
    val avisos: StateFlow<List<Aviso>>
    fun registrar(titulo: String, texto: String, origen: Origen): Aviso
}

class AvisosRepositoryMemoria : AvisosRepository {
    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    override val avisos: StateFlow<List<Aviso>> = _avisos.asStateFlow()
    private var siguiente = 1

    override fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val aviso = Aviso(siguiente++, titulo, texto, origen, hora)
        _avisos.value = listOf(aviso) + _avisos.value
        return aviso
    }
}
