package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoLineas(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    alto: Dp = 140.dp
) {
    val color = MaterialTheme.colorScheme.primary
    val guia = MaterialTheme.colorScheme.outlineVariant
    val fondo = MaterialTheme.colorScheme.surface
    val maximo = datos.maxOfOrNull { it.valor }?.takeIf { it > 0.0 } ?: 1.0

    Column(modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(alto)) {
            if (datos.isEmpty()) return@Canvas
            val margen = 8.dp.toPx()
            val alturaUtil = size.height - margen * 2
            val paso = size.width / datos.size
            val puntos = datos.mapIndexed { i, d ->
                Offset(
                    x = paso * i + paso / 2,
                    y = margen + alturaUtil - (d.valor / maximo).toFloat() * alturaUtil
                )
            }

            repeat(3) { i ->
                val y = margen + alturaUtil * i / 2
                drawLine(guia, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
            }

            val area = Path().apply {
                moveTo(puntos.first().x, margen + alturaUtil)
                puntos.forEach { lineTo(it.x, it.y) }
                lineTo(puntos.last().x, margen + alturaUtil)
                close()
            }
            drawPath(area, Brush.verticalGradient(listOf(color.copy(alpha = 0.25f), color.copy(alpha = 0f))))

            val linea = Path().apply {
                moveTo(puntos.first().x, puntos.first().y)
                puntos.drop(1).forEach { lineTo(it.x, it.y) }
            }
            drawPath(
                linea,
                color,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            puntos.forEach {
                drawCircle(color, radius = 5.dp.toPx(), center = it)
                drawCircle(fondo, radius = 2.5.dp.toPx(), center = it)
            }
        }
        Row(Modifier.fillMaxWidth()) {
            datos.forEach {
                Text(
                    it.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
