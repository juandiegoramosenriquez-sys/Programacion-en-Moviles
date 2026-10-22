package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.ui.components.formatoSoles
import com.tecsup.lavacampus.ui.components.textoCantidad
import com.tecsup.lavacampus.ui.theme.EstadoListo
import com.tecsup.lavacampus.ui.theme.EstadoRecibido
import com.tecsup.lavacampus.viewmodel.DetallePedidoViewModel

@Composable
fun DetallePedidoScreen(vm: DetallePedidoViewModel, onVerRecibo: () -> Unit, modifier: Modifier = Modifier) {
    val detalle by vm.pedido.collectAsState()
    val puedeCancelar by vm.puedeCancelar.collectAsState()
    val puedeCalificar by vm.puedeCalificar.collectAsState()
    var confirmar by remember { mutableStateOf(false) }

    val actual = detalle ?: return
    val pedido = actual.pedido
    val estado = EstadoPedido.valueOf(pedido.estado)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row {
            Column(Modifier.weight(1f)) {
                Text("Pedido #${pedido.id}", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Registrado el ${pedido.fechaRegistro} · Entrega ${pedido.fechaEntrega}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            EstadoPedidoChip(estado)
        }

        Tarjeta {
            Text("Seguimiento", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            if (estado == EstadoPedido.CANCELADO) {
                Text("Este pedido fue cancelado", color = MaterialTheme.colorScheme.error)
            } else {
                StepperEstado(estado, vertical = true)
            }
        }

        Tarjeta {
            Text("Servicios", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            actual.detalles.forEach { d ->
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(d.servicio.nombre)
                        Text(
                            "${textoCantidad(d.servicio.unidad, d.detalle.cantidad)} × ${formatoSoles(d.detalle.precioUnitario)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(formatoSoles(d.detalle.subtotal))
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            if (pedido.descuento > 0.0) {
                Row(Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                    Text("Descuento por volumen", color = EstadoListo, modifier = Modifier.weight(1f))
                    Text("- ${formatoSoles(pedido.descuento)}", color = EstadoListo)
                }
            }
            Row(Modifier.fillMaxWidth()) {
                Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(formatoSoles(pedido.total), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            if (pedido.nota.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text("Nota: ${pedido.nota}", style = MaterialTheme.typography.bodySmall)
            }
        }

        Aviso(
            texto = if (pedido.pagado) "Pagado" else "Pago pendiente, se paga al recoger",
            color = if (pedido.pagado) EstadoListo else EstadoRecibido
        )

        when (estado) {
            EstadoPedido.LISTO -> Aviso("Recógelo en el casillero ${pedido.casillero.orEmpty()}", EstadoListo)
            EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO -> Aviso("Casillero: se asignará al estar listo", MaterialTheme.colorScheme.onSurfaceVariant)
            else -> Unit
        }

        if (puedeCalificar) {
            Tarjeta {
                Text(
                    if (pedido.calificacion == 0) "¿Qué tal tu pedido?" else "Tu calificación",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Toca las estrellas para calificar de 1 a 5",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Row {
                    (1..5).forEach { n ->
                        IconButton(onClick = { vm.calificar(n) }) {
                            Icon(
                                if (n <= pedido.calificacion) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                contentDescription = "$n estrellas",
                                tint = if (n <= pedido.calificacion) EstadoRecibido else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }

        OutlinedButton(onClick = onVerRecibo, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.AutoMirrored.Outlined.ReceiptLong, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Ver recibo")
        }

        if (estado != EstadoPedido.CANCELADO && estado != EstadoPedido.ENTREGADO) {
            OutlinedButton(
                onClick = { confirmar = true },
                enabled = puedeCancelar,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar pedido")
            }
            if (!puedeCancelar) {
                Text(
                    "Solo se cancela antes de empezar el lavado",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("Cancelar pedido") },
            text = { Text("¿Seguro que quieres cancelar el pedido #${pedido.id}?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar()
                    confirmar = false
                }) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
private fun Tarjeta(contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            contenido()
        }
    }
}

@Composable
private fun Aviso(texto: String, color: Color) {
    Text(
        texto,
        color = color,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    )
}
