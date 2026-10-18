package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoDona(
    valores: List<Double>,
    colores: List<Color>,
    modifier: Modifier = Modifier,
    tamano: Dp = 160.dp,
    grosor: Dp = 24.dp
) {
    val vacio = MaterialTheme.colorScheme.surfaceVariant
    val total = valores.sum()
    Canvas(modifier.size(tamano)) {
        val g = grosor.toPx()
        val area = Size(size.width - g, size.height - g)
        val origen = Offset(g / 2, g / 2)
        if (total <= 0.0 || colores.isEmpty()) {
            drawArc(vacio, 0f, 360f, false, origen, area, style = Stroke(g))
            return@Canvas
        }
        var inicio = -90f
        valores.forEachIndexed { i, v ->
            val angulo = (v / total * 360).toFloat()
            drawArc(colores[i % colores.size], inicio, angulo, false, origen, area, style = Stroke(g))
            inicio += angulo
        }
    }
}
