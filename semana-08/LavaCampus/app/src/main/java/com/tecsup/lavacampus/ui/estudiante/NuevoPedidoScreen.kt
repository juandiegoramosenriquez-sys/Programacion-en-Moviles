package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.ui.components.SelectorCantidad
import com.tecsup.lavacampus.ui.components.formatoSoles
import com.tecsup.lavacampus.ui.components.textoCantidad
import com.tecsup.lavacampus.ui.theme.EstadoListo
import com.tecsup.lavacampus.viewmodel.NuevoPedidoViewModel

@Composable
fun NuevoPedidoScreen(
    vm: NuevoPedidoViewModel,
    onCreado: () -> Unit,
    modifier: Modifier = Modifier
) {
    val servicios by vm.servicios.collectAsState()
    val cantidades by vm.cantidades.collectAsState()
    val nota by vm.nota.collectAsState()
    val subtotal by vm.subtotal.collectAsState()
    val descuento by vm.descuento.collectAsState()
    val kilos by vm.kilosLavado.collectAsState()
    val total by vm.total.collectAsState()
    val fechaEntrega by vm.fechaEntrega.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val creado by vm.creado.collectAsState()
    val recibiendo by vm.recibiendo.collectAsState()

    LaunchedEffect(creado) {
        if (creado) onCreado()
    }

    val elegidos = servicios.filter { (cantidades[it.id] ?: 0.0) > 0.0 }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!recibiendo) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "La lavandería no está recibiendo pedidos",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        if (vm.repetirId != 0) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Repitiendo el pedido #${vm.repetirId}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            "Cargamos las mismas cantidades con los precios actuales. Puedes cambiarlas antes de confirmar.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        item {
            Text("Elige tus servicios", style = MaterialTheme.typography.titleMedium)
        }

        items(servicios, key = { it.id }) { s ->
            val cantidad = cantidades[s.id] ?: 0.0
            ServicioCantidad(
                servicio = s,
                cantidad = cantidad,
                paso = vm.paso(s),
                maximo = vm.maximo(s),
                onCambio = { nuevo -> vm.cambiarCantidad(s.id, nuevo - cantidad) }
            )
        }

        item {
            LavaTextField(nota, vm::cambiarNota, "Nota (opcional)")
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Resumen", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    if (elegidos.isEmpty()) {
                        Text("Aún no elegiste servicios", style = MaterialTheme.typography.bodyMedium)
                    }
                    elegidos.forEach { s ->
                        val cantidad = cantidades[s.id] ?: 0.0
                        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                            Text("${s.nombre} × ${textoCantidad(s.unidad, cantidad)}", modifier = Modifier.weight(1f))
                            Text(formatoSoles(cantidad * s.precio))
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    if (descuento > 0.0) {
                        Row(Modifier.fillMaxWidth()) {
                            Text("Subtotal", modifier = Modifier.weight(1f))
                            Text(formatoSoles(subtotal))
                        }
                        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                            Text(
                                "Descuento por volumen (10 % del lavado)",
                                color = EstadoListo,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Text("- ${formatoSoles(descuento)}", color = EstadoListo, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Row(Modifier.fillMaxWidth()) {
                        Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(formatoSoles(total), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    if (descuento == 0.0 && kilos > 0.0) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Lava más de 5 kg y obtén 10 % de descuento en el lavado",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (fechaEntrega.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text("Entrega estimada: $fechaEntrega", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        mensaje?.let {
            item {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

        item {
            LavaButton("Confirmar pedido", onClick = vm::confirmar, enabled = elegidos.isNotEmpty() && recibiendo)
        }
    }
}

@Composable
private fun ServicioCantidad(
    servicio: Servicio,
    cantidad: Double,
    paso: Double,
    maximo: Double,
    onCambio: (Double) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconoServicio(servicio.icono)
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(servicio.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${formatoSoles(servicio.precio)} · ${servicio.unidad.lowercase()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            SelectorCantidad(
                valor = cantidad,
                paso = paso,
                minimo = 1.0,
                maximo = maximo,
                onCambio = onCambio
            )
        }
    }
}
