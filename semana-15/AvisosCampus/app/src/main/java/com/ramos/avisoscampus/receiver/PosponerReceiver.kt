package com.ramos.avisoscampus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class PosponerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(EXTRA_ID, 0)
        NotificationManagerCompat.from(context).cancel(id)
        SolicitudesPosponer.emitir()
    }

    companion object {
        const val EXTRA_ID = "id_aviso"
    }
}

object SolicitudesPosponer {
    private val _solicitudes = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val solicitudes: SharedFlow<Unit> = _solicitudes.asSharedFlow()

    fun emitir() {
        _solicitudes.tryEmit(Unit)
    }
}
