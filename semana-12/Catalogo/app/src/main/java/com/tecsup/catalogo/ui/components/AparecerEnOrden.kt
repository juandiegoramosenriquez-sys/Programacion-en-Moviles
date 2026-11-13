package com.tecsup.catalogo.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

@Composable
fun AparecerEnOrden(orden: Int, contenido: @Composable () -> Unit) {
    val progreso = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(orden * 90L)
        progreso.animateTo(1f, tween(durationMillis = 400))
    }
    Box(
        modifier = Modifier.graphicsLayer {
            alpha = progreso.value
            translationY = (1f - progreso.value) * 40f
        }
    ) {
        contenido()
    }
}
