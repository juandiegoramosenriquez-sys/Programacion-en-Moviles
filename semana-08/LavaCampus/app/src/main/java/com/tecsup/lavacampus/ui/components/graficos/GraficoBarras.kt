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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoBarras(
    etiquetas: List<String>,
    valores: List<Double>,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    alto: Dp = 160.dp
) {
    val maximo = valores.maxOrNull()?.takeIf { it > 0.0 } ?: 1.0
    Column(modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(alto)) {
            if (valores.isEmpty()) return@Canvas
            val ancho = size.width / valores.size
            val barra = ancho * 0.6f
            valores.forEachIndexed { i, v ->
                val h = (v / maximo).toFloat() * size.height
                drawRoundRect(
                    color,
                    topLeft = Offset(ancho * i + (ancho - barra) / 2, size.height - h),
                    size = Size(barra, h),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )
            }
        }
        Row(Modifier.fillMaxWidth()) {
            etiquetas.forEach {
                Text(
                    it,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
