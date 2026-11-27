package com.ramos.avisoscampus

import android.app.Application
import com.ramos.avisoscampus.notificacion.Notificador

class AvisosApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanal(this)
    }
}
