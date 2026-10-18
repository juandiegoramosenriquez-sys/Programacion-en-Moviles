package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    tamano: Dp = 140.dp,
    grosor: Dp = 22.dp
) {
    val vacio = MaterialTheme.colorScheme.surfaceVariant
    val total = datos.sumOf { it.valor }
    Canvas(modifier.size(tamano)) {
        val g = grosor.toPx()
        val area = Size(size.width - g, size.height - g)
        val origen = Offset(g / 2, g / 2)
        if (total <= 0.0) {
            drawArc(vacio, 0f, 360f, false, origen, area, style = Stroke(g))
            return@Canvas
        }
        var inicio = -90f
        datos.forEach { d ->
            val angulo = (d.valor / total * 360).toFloat()
            drawArc(d.color, inicio, angulo, false, origen, area, style = Stroke(g))
            inicio += angulo
        }
    }
}
