package com.ramos.avisoscampus.notificacion

import android.content.Context
import com.ramos.avisoscampus.model.Aviso

object Notificador {

    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
    }

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        return true
    }
}
