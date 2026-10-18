package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.PedidoCard
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.ui.theme.EstadoListo
import com.tecsup.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    usuario: Usuario,
    vm: InicioViewModel,
    onNuevoPedido: () -> Unit,
    onMisPedidos: () -> Unit,
    onTarifas: () -> Unit,
    onPedido: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val activo by vm.pedidoActivo.collectAsState()
    val recientes by vm.recientes.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Hola, ${usuario.nombre}", style = MaterialTheme.typography.headlineSmall)
            Text(
                usuario.codigo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            PedidoActivoCard(activo, onPedido)
        }

        item {
            LavaButton("Nuevo pedido", onClick = onNuevoPedido)
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onMisPedidos, modifier = Modifier.weight(1f)) {
                    Text("Mis pedidos")
                }
                OutlinedButton(onClick = onTarifas, modifier = Modifier.weight(1f)) {
                    Text("Tarifas")
                }
            }
        }

        item {
            Text("Últimos pedidos", style = MaterialTheme.typography.titleMedium)
        }

        if (recientes.isEmpty()) {
            item {
                Text(
                    "Aún no tienes pedidos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(recientes, key = { it.pedido.id }) { p ->
            PedidoCard(
                numero = p.pedido.id,
                fecha = p.pedido.fechaRegistro,
                resumen = p.detalles.joinToString { it.servicio.nombre },
                total = p.pedido.total,
                estado = EstadoPedido.valueOf(p.pedido.estado),
                onClick = { onPedido(p.pedido.id) }
            )
        }
    }
}

@Composable
private fun PedidoActivoCard(activo: PedidoConDetalle?, onPedido: (Int) -> Unit) {
    Card(
        onClick = { activo?.let { onPedido(it.pedido.id) } },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp)) {
            if (activo == null) {
                Text("No tienes pedidos activos", style = MaterialTheme.typography.titleMedium)
                Text("Crea uno con el botón Nuevo pedido", style = MaterialTheme.typography.bodyMedium)
            } else {
                ContenidoActivo(activo)
            }
        }
    }
}

@Composable
private fun ContenidoActivo(activo: PedidoConDetalle) {
    Column {
        val estado = EstadoPedido.valueOf(activo.pedido.estado)
        Row {
            Text(
                "Pedido #${activo.pedido.id}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            EstadoPedidoChip(estado)
        }
        Spacer(Modifier.height(16.dp))
        StepperEstado(estado)
        Spacer(Modifier.height(16.dp))
        Text("Entrega estimada: ${activo.pedido.fechaEntrega}", style = MaterialTheme.typography.bodyMedium)
        if (estado == EstadoPedido.LISTO && activo.pedido.casillero != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                "Recógelo en el casillero ${activo.pedido.casillero}",
                color = EstadoListo,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EstadoListo.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            )
        }
    }
}
