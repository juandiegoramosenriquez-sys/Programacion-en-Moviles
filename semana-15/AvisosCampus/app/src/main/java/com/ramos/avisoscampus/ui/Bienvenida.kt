package com.ramos.avisoscampus.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.ramos.avisoscampus.ui.theme.LocalPaleta

@Composable
fun Bienvenida(alTerminar: () -> Unit) {
    val p = LocalPaleta.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(p.portada)
            .systemBarsPadding()
            .padding(24.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = alTerminar) {
                Text("Omitir", color = p.textoPortada.copy(alpha = 0.7f))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(280.dp)) {
                val centro = size.minDimension / 2
                listOf(1f, 0.78f, 0.58f).forEachIndexed { i, escala ->
                    drawCircle(
                        color = p.arena.copy(alpha = 0.10f + i * 0.08f),
                        radius = centro * escala,
                        style = Stroke(1.dp.toPx())
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(p.arena, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.NotificationsActive,
                    contentDescription = null,
                    tint = p.portada,
                    modifier = Modifier.size(52.dp)
                )
            }
        }
        Text(
            text = "Más que avisos.\nTu campus al día.",
            style = MaterialTheme.typography.displaySmall,
            color = p.textoPortada
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Recibe tus avisos, programa recordatorios y entérate cuando cambia tu celular, todo en un solo lugar.",
            style = MaterialTheme.typography.bodyLarge,
            color = p.textoPortada.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(28.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(Modifier.size(width = 22.dp, height = 6.dp).background(p.textoPortada, RoundedCornerShape(50)))
            Box(Modifier.size(6.dp).background(p.textoPortada.copy(alpha = 0.3f), CircleShape))
            Box(Modifier.size(6.dp).background(p.textoPortada.copy(alpha = 0.3f), CircleShape))
        }
        Spacer(Modifier.height(24.dp))
        BotonPrincipal(
            texto = "Comenzar",
            onClick = alTerminar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            fondo = p.textoPortada,
            contenidoColor = p.portada
        )
        Spacer(Modifier.width(1.dp))
    }
}
