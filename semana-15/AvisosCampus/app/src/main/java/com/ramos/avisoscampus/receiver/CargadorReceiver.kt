package com.ramos.avisoscampus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CargadorReceiver(private val alCambiar: (Boolean) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> alCambiar(true)
            Intent.ACTION_POWER_DISCONNECTED -> alCambiar(false)
        }
    }
}
