package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconoServicio(
    icono: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 44.dp
) {
    val fondo = MaterialTheme.colorScheme.primaryContainer
    val color = MaterialTheme.colorScheme.primary
    Canvas(modifier.size(tamano)) {
        val s = size.minDimension
        val trazo = Stroke(width = s * 0.05f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawCircle(fondo)
        when (icono) {
            "iron" -> {
                val base = Path().apply {
                    moveTo(s * 0.22f, s * 0.66f)
                    lineTo(s * 0.78f, s * 0.66f)
                    lineTo(s * 0.72f, s * 0.44f)
                    lineTo(s * 0.40f, s * 0.44f)
                    quadraticTo(s * 0.26f, s * 0.48f, s * 0.22f, s * 0.66f)
                    close()
                }
                val asa = Path().apply {
                    moveTo(s * 0.44f, s * 0.44f)
                    lineTo(s * 0.48f, s * 0.32f)
                    lineTo(s * 0.68f, s * 0.32f)
                    lineTo(s * 0.70f, s * 0.44f)
                }
                drawPath(base, color, style = trazo)
                drawPath(asa, color, style = trazo)
            }
            "shirt" -> {
                val camisa = Path().apply {
                    moveTo(s * 0.38f, s * 0.26f)
                    lineTo(s * 0.24f, s * 0.34f)
                    lineTo(s * 0.30f, s * 0.46f)
                    lineTo(s * 0.36f, s * 0.43f)
                    lineTo(s * 0.36f, s * 0.74f)
                    lineTo(s * 0.64f, s * 0.74f)
                    lineTo(s * 0.64f, s * 0.43f)
                    lineTo(s * 0.70f, s * 0.46f)
                    lineTo(s * 0.76f, s * 0.34f)
                    lineTo(s * 0.62f, s * 0.26f)
                    quadraticTo(s * 0.50f, s * 0.36f, s * 0.38f, s * 0.26f)
                    close()
                }
                drawPath(camisa, color, style = trazo)
            }
            else -> {
                drawRoundRect(
                    color,
                    topLeft = Offset(s * 0.28f, s * 0.24f),
                    size = Size(s * 0.44f, s * 0.52f),
                    cornerRadius = CornerRadius(s * 0.06f),
                    style = trazo
                )
                drawLine(color, Offset(s * 0.28f, s * 0.36f), Offset(s * 0.72f, s * 0.36f), strokeWidth = s * 0.05f)
                drawCircle(color, radius = s * 0.025f, center = Offset(s * 0.36f, s * 0.30f))
                drawCircle(color, radius = s * 0.12f, center = Offset(s * 0.50f, s * 0.56f), style = trazo)
            }
        }
    }
}
