package com.ramos.avisoscampus.notificacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.ramos.avisoscampus.MainActivity
import com.ramos.avisoscampus.R
import com.ramos.avisoscampus.model.Aviso
import com.ramos.avisoscampus.model.Origen
import com.ramos.avisoscampus.receiver.PosponerReceiver

object Notificador {

    const val CANAL = "avisos"
    const val CANAL_URGENTE = "urgentes"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
            val urgente = NotificationChannel(CANAL_URGENTE, "Urgentes", NotificationManager.IMPORTANCE_HIGH)
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannels(listOf(canal, urgente))
        }
    }

    fun tienePermiso(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false
        val intent = Intent(context, MainActivity::class.java)
        val pendiente = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val canal = if (aviso.urgente) CANAL_URGENTE else CANAL
        val prioridad = if (aviso.urgente) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT
        val constructor = NotificationCompat.Builder(context, canal)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(textoLargo(aviso)))
            .setPriority(prioridad)
            .setContentIntent(pendiente)
            .setAutoCancel(true)
        if (aviso.origen == Origen.RECORDATORIO) {
            val intentPosponer = Intent(context, PosponerReceiver::class.java)
                .putExtra(PosponerReceiver.EXTRA_ID, aviso.id)
            val posponer = PendingIntent.getBroadcast(
                context,
                aviso.id,
                intentPosponer,
                PendingIntent.FLAG_IMMUTABLE
            )
            constructor.addAction(R.drawable.ic_notificacion, "Posponer 10 s", posponer)
        }
        NotificationManagerCompat.from(context).notify(aviso.id, constructor.build())
        return true
    }

    private fun textoLargo(aviso: Aviso): String = when (aviso.origen) {
        Origen.BOTON -> "${aviso.texto}. Tu docente ya puede revisar tu trabajo; si necesitas corregir algo, vuelve a subir tus cambios antes del cierre."
        Origen.RECORDATORIO -> "${aviso.texto}: termina los pasos que faltan, toma las capturas en el emulador y sube tus commits a GitHub antes de la fecha de entrega."
        Origen.MODO_AVION -> "Modo avión ${aviso.texto.lowercase()}. Mientras esté activo no tendrás internet, pero los avisos locales de AvisosCampus seguirán llegando."
        Origen.CARGADOR -> "Cargador ${aviso.texto.lowercase()}. AvisosCampus registró el cambio en tu historial para que sepas cuándo conectaste o desconectaste el celular."
    }
}
