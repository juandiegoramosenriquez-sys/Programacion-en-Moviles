package com.tecsup.lavacampus.ui.estudiante

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.formatoSoles
import com.tecsup.lavacampus.ui.components.textoCantidad
import com.tecsup.lavacampus.ui.theme.EstadoListo
import com.tecsup.lavacampus.ui.theme.EstadoRecibido
import com.tecsup.lavacampus.viewmodel.DetallePedidoViewModel

@Composable
fun ReciboScreen(vm: DetallePedidoViewModel, modifier: Modifier = Modifier) {
    val detalle by vm.pedido.collectAsState()
    val contexto = LocalContext.current

    val actual = detalle ?: return
    val pedido = actual.pedido

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    IconoServicio("wash", tamano = 48.dp)
                    Spacer(Modifier.height(8.dp))
                    Text("LavaCampus", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Recibo del pedido #${pedido.id}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    EstadoPedidoChip(EstadoPedido.valueOf(pedido.estado))
                }

                HorizontalDivider(Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

                Dato("Estudiante", actual.usuario.nombre)
                Dato("Código", actual.usuario.codigo)
                Dato("Fecha", pedido.fechaRegistro)
                Dato("Entrega", pedido.fechaEntrega)
                pedido.casillero?.let { Dato("Casillero", it) }

                HorizontalDivider(Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

                actual.detalles.forEach { d ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text(d.servicio.nombre, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "${textoCantidad(d.servicio.unidad, d.detalle.cantidad)} × ${formatoSoles(d.detalle.precioUnitario)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(formatoSoles(d.detalle.subtotal), style = MaterialTheme.typography.bodyLarge)
                    }
                }

                HorizontalDivider(Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

                if (pedido.descuento > 0.0) {
                    Row(Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
                        Text("Descuento por volumen", color = EstadoListo, modifier = Modifier.weight(1f))
                        Text("- ${formatoSoles(pedido.descuento)}", color = EstadoListo)
                    }
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Total", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    Text(
                        formatoSoles(pedido.total),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    if (pedido.pagado) "Pagado" else "Pago pendiente, se paga al recoger",
                    color = if (pedido.pagado) EstadoListo else EstadoRecibido,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        LavaButton("Compartir recibo", onClick = {
            val envio = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Recibo LavaCampus #${pedido.id}")
                putExtra(Intent.EXTRA_TEXT, vm.textoRecibo())
            }
            contexto.startActivity(Intent.createChooser(envio, "Compartir recibo"))
        })
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(
            etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(valor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
