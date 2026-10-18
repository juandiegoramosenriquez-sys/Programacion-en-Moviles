package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.EstadoPedido

private val etapas = listOf(
    EstadoPedido.RECIBIDO,
    EstadoPedido.EN_PROCESO,
    EstadoPedido.LISTO,
    EstadoPedido.ENTREGADO
)

@Composable
fun StepperEstado(
    estado: EstadoPedido,
    modifier: Modifier = Modifier,
    vertical: Boolean = false
) {
    val actual = etapas.indexOf(estado)
    val activo = MaterialTheme.colorScheme.primary
    val inactivo = MaterialTheme.colorScheme.outlineVariant
    if (vertical) {
        StepperVertical(actual, activo, inactivo, modifier)
    } else {
        StepperHorizontal(actual, activo, inactivo, modifier)
    }
}

@Composable
private fun StepperHorizontal(actual: Int, activo: Color, inactivo: Color, modifier: Modifier) {
    Column(modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(24.dp)) {
            val radio = 8.dp.toPx()
            val paso = size.width / etapas.size
            val y = size.height / 2
            for (i in 0 until etapas.lastIndex) {
                val x = paso * i + paso / 2
                drawLine(
                    color = if (i < actual) activo else inactivo,
                    start = Offset(x, y),
                    end = Offset(x + paso, y),
                    strokeWidth = 3.dp.toPx()
                )
            }
            for (i in etapas.indices) {
                val centro = Offset(paso * i + paso / 2, y)
                drawCircle(if (i <= actual) activo else inactivo, radio, centro)
                if (i == actual) drawCircle(Color.White, radio / 2.5f, centro)
            }
        }
        Row(Modifier.fillMaxWidth()) {
            etapas.forEachIndexed { i, etapa ->
                Text(
                    etapa.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = if (i <= actual) activo else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StepperVertical(actual: Int, activo: Color, inactivo: Color, modifier: Modifier) {
    Column(modifier) {
        etapas.forEachIndexed { i, etapa ->
            val ultima = i == etapas.lastIndex
            Row(verticalAlignment = Alignment.Top) {
                Canvas(Modifier.width(24.dp).height(if (ultima) 24.dp else 48.dp)) {
                    val radio = 8.dp.toPx()
                    val centro = Offset(size.width / 2, 12.dp.toPx())
                    if (!ultima) {
                        drawLine(
                            color = if (i < actual) activo else inactivo,
                            start = centro,
                            end = Offset(centro.x, size.height + 12.dp.toPx()),
                            strokeWidth = 3.dp.toPx()
                        )
                    }
                    drawCircle(if (i <= actual) activo else inactivo, radio, centro)
                    if (i == actual) drawCircle(Color.White, radio / 2.5f, centro)
                }
                Text(
                    etapa.etiqueta,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (i == actual) FontWeight.Bold else FontWeight.Normal,
                    color = if (i <= actual) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}
