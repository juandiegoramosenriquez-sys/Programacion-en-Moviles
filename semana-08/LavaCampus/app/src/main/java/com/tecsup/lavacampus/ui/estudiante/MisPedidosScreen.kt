package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.PedidoCard
import com.tecsup.lavacampus.ui.theme.EstadoListo
import com.tecsup.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun MisPedidosScreen(
    vm: MisPedidosViewModel,
    onPedido: (Int) -> Unit,
    onNuevoPedido: () -> Unit,
    onRepetir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val activos by vm.activos.collectAsState()
    val historial by vm.historial.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(0) }
    var porCancelar by remember { mutableStateOf<PedidoConDetalle?>(null) }

    val lista = if (pestana == 0) activos else historial

    Box(modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            PrimaryTabRow(selectedTabIndex = pestana) {
                Tab(selected = pestana == 0, onClick = { pestana = 0 }, text = { Text("Activos (${activos.size})") })
                Tab(selected = pestana == 1, onClick = { pestana = 1 }, text = { Text("Historial (${historial.size})") })
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (lista.isEmpty()) {
                    item {
                        Text(
                            if (pestana == 0) "No tienes pedidos activos" else "Aún no hay pedidos en tu historial",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                items(lista, key = { it.pedido.id }) { p ->
                    val estado = EstadoPedido.valueOf(p.pedido.estado)
                    PedidoCard(
                        numero = p.pedido.id,
                        fecha = "Entrega ${p.pedido.fechaEntrega}",
                        resumen = p.detalles.joinToString { it.servicio.nombre },
                        total = p.pedido.total,
                        estado = estado,
                        onClick = { onPedido(p.pedido.id) }
                    ) {
                        if (estado == EstadoPedido.LISTO && p.pedido.casillero != null) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Recógelo en el casillero ${p.pedido.casillero}",
                                color = EstadoListo,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(EstadoListo.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            )
                        }
                        if (vm.puedeCancelar(p)) {
                            TextButton(onClick = { porCancelar = p }) {
                                Text("Cancelar", color = MaterialTheme.colorScheme.error)
                            }
                        }
                        if (pestana == 1) {
                            Spacer(Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = { onRepetir(p.pedido.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Replay, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Pedir de nuevo")
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onNuevoPedido,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo pedido")
        }
    }

    porCancelar?.let { p ->
        AlertDialog(
            onDismissRequest = { porCancelar = null },
            title = { Text("Cancelar pedido") },
            text = { Text("¿Seguro que quieres cancelar el pedido #${p.pedido.id}?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar(p)
                    porCancelar = null
                }) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { porCancelar = null }) {
                    Text("No")
                }
            }
        )
    }
}
