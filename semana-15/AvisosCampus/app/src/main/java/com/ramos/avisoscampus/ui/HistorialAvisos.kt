package com.ramos.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramos.avisoscampus.model.Aviso
import com.ramos.avisoscampus.ui.theme.LocalPaleta

@Composable
fun HistorialAvisos(
    avisos: List<Aviso>,
    titulo: String = "Historial",
    limite: Int = 20,
    accion: @Composable (() -> Unit)? = null
) {
    Tarjeta(titulo, icono = Icons.Rounded.History, accion = accion) {
        if (avisos.isEmpty()) {
            HistorialVacio()
        } else {
            val visibles = avisos.take(limite)
            visibles.forEachIndexed { i, aviso ->
                FilaAviso(aviso)
                if (i < visibles.lastIndex) {
                    HorizontalDivider(color = LocalPaleta.current.borde)
                }
            }
        }
    }
}

@Composable
fun HistorialVacio() {
    val p = LocalPaleta.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(p.arena, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.NotificationsNone,
                contentDescription = null,
                tint = p.texto,
                modifier = Modifier.size(40.dp)
            )
        }
        Text("Sin avisos todavía", style = MaterialTheme.typography.titleMedium, color = p.texto)
        Text(
            text = "Cuando muestres un aviso o llegue un recordatorio, lo verás aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = p.textoSecundario,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FilaAviso(aviso: Aviso) {
    val p = LocalPaleta.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Pildora(aviso.origen.texto, colorOrigen(aviso.origen))
            Text(aviso.titulo, style = MaterialTheme.typography.titleMedium, color = p.texto)
            Text(aviso.texto, style = MaterialTheme.typography.bodyMedium, color = p.textoSecundario)
        }
        Text(aviso.hora, style = MaterialTheme.typography.bodySmall, color = p.textoSecundario)
    }
}
