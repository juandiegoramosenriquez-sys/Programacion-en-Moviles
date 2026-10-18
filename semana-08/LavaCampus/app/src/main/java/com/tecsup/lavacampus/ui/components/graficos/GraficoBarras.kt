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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoBarras(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    alto: Dp = 140.dp
) {
    val colorBase = MaterialTheme.colorScheme.primary
    val maximo = datos.maxOfOrNull { it.valor }?.takeIf { it > 0.0 } ?: 1.0
    Column(modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(alto)) {
            if (datos.isEmpty()) return@Canvas
            val ancho = size.width / datos.size
            val barra = ancho * 0.6f
            datos.forEachIndexed { i, d ->
                val h = (d.valor / maximo).toFloat() * size.height
                drawRoundRect(
                    if (d.color.isSpecified) d.color else colorBase,
                    topLeft = Offset(ancho * i + (ancho - barra) / 2, size.height - h),
                    size = Size(barra, h),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )
            }
        }
        Row(Modifier.fillMaxWidth()) {
            datos.forEach {
                Text(
                    it.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
