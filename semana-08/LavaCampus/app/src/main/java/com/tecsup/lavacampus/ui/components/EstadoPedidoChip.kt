package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.theme.colorEstado
import com.tecsup.lavacampus.ui.theme.esModoOscuro
import com.tecsup.lavacampus.ui.theme.fondoEstadoNoche
import com.tecsup.lavacampus.ui.theme.textoEstadoNoche

@Composable
fun EstadoPedidoChip(estado: EstadoPedido, modifier: Modifier = Modifier) {
    val oscuro = esModoOscuro()
    val fondo = if (oscuro) fondoEstadoNoche(estado) else colorEstado(estado).copy(alpha = 0.12f)
    val texto = if (oscuro) textoEstadoNoche(estado) else colorEstado(estado)
    Text(
        text = estado.etiqueta,
        color = texto,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .background(fondo, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
