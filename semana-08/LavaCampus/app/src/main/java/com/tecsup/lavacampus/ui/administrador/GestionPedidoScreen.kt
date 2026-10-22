package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.Casilleros
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.ui.components.formatoSoles
import com.tecsup.lavacampus.ui.components.textoCantidad
import com.tecsup.lavacampus.ui.theme.EstadoListo
import com.tecsup.lavacampus.viewmodel.GestionPedidoViewModel

@Composable
fun GestionPedidoScreen(vm: GestionPedidoViewModel, modifier: Modifier = Modifier) {
    val detalle by vm.pedido.collectAsState()
    val ocupados by vm.casillerosOcupados.collectAsState()
    val elegido by vm.casillero.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    val actual = detalle ?: return
    val pedido = actual.pedido
    val estado = EstadoPedido.valueOf(pedido.estado)
    val siguiente = estado.siguiente()
    val casilleroMostrado = elegido ?: pedido.casillero

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Tarjeta {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Pedido #${pedido.id}", style = MaterialTheme.typography.titleLarge)
                    Text(actual.usuario.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${actual.usuario.codigo} · ${actual.usuario.correo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                EstadoPedidoChip(estado)
            }
        }

        Tarjeta {
            Text("Seguimiento", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            if (estado == EstadoPedido.CANCELADO) {
                Text("Pedido cancelado", color = MaterialTheme.colorScheme.error)
            } else {
                StepperEstado(estado)
            }
        }

        if (estado == EstadoPedido.EN_PROCESO || estado == EstadoPedido.LISTO) {
            Tarjeta {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Casillero", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    Text(
                        casilleroMostrado ?: "Sin elegir",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Casilleros.TODOS.chunked(5).forEach { fila ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        fila.forEach { c ->
                            CeldaCasillero(
                                codigo = c,
                                elegido = c == casilleroMostrado,
                                ocupado = c in ocupados,
                                habilitado = estado == EstadoPedido.EN_PROCESO,
                                onClick = { vm.elegirCasillero(c) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Leyenda(MaterialTheme.colorScheme.primary, "Elegido")
                    Leyenda(MaterialTheme.colorScheme.surfaceVariant, "Ocupado")
                    Leyenda(MaterialTheme.colorScheme.outlineVariant, "Libre")
                }
            }
        }

        Tarjeta {
            actual.detalles.forEach { d ->
                Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text(
                        "${d.servicio.nombre} × ${textoCantidad(d.servicio.unidad, d.detalle.cantidad)}",
                        modifier = Modifier.weight(1f)
                    )
                    Text(formatoSoles(d.detalle.subtotal))
                }
            }
            Spacer(Modifier.height(8.dp))
            if (pedido.descuento > 0.0) {
                Text(
                    "Descuento por volumen: - ${formatoSoles(pedido.descuento)}",
                    color = EstadoListo,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Total ${formatoSoles(pedido.total)}", fontWeight = FontWeight.Bold)
                    Text("Cobrado", style = MaterialTheme.typography.bodySmall)
                }
                Switch(
                    checked = pedido.pagado,
                    onCheckedChange = vm::marcarPagado,
                    enabled = estado != EstadoPedido.CANCELADO && estado != EstadoPedido.ENTREGADO
                )
            }
        }

        Text(
            "Regla: para pasar a Listo elige un casillero libre; para entregar el pedido debe estar cobrado.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        mensaje?.let {
            Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
        }

        if (siguiente != null) {
            LavaButton("Marcar como ${siguiente.etiqueta}", onClick = vm::avanzar)
        }

        if (estado == EstadoPedido.RECIBIDO || estado == EstadoPedido.EN_PROCESO) {
            OutlinedButton(
                onClick = vm::cancelar,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar pedido")
            }
        }
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
private fun CeldaCasillero(
    codigo: String,
    elegido: Boolean,
    ocupado: Boolean,
    habilitado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val colores = MaterialTheme.colorScheme
    val fondo = when {
        elegido -> colores.primary
        ocupado -> colores.surfaceVariant
        else -> colores.surface
    }
    val texto = when {
        elegido -> colores.onPrimary
        ocupado -> colores.outline
        else -> colores.onSurface
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(40.dp)
            .background(fondo, RoundedCornerShape(10.dp))
            .border(1.dp, if (elegido) colores.primary else colores.outlineVariant, RoundedCornerShape(10.dp))
            .clickable(enabled = habilitado && !ocupado, onClick = onClick)
    ) {
        Text(
            codigo.removePrefix("C-"),
            style = MaterialTheme.typography.labelLarge,
            color = texto,
            textDecoration = if (ocupado) TextDecoration.LineThrough else null
        )
    }
}

@Composable
private fun Leyenda(color: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(12.dp).background(color, RoundedCornerShape(3.dp)))
        Spacer(Modifier.width(6.dp))
        Text(texto, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
