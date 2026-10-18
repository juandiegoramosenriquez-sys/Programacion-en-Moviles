package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.PedidoCard
import com.tecsup.lavacampus.viewmodel.BandejaViewModel

@Composable
fun BandejaScreen(
    vm: BandejaViewModel,
    onAbrir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pedidos by vm.pedidos.collectAsState()
    val filtro by vm.filtro.collectAsState()
    val busqueda by vm.busqueda.collectAsState()

    val opciones = listOf(
        null to "Todos",
        EstadoPedido.RECIBIDO to "Recibido",
        EstadoPedido.EN_PROCESO to "En proceso",
        EstadoPedido.LISTO to "Listo"
    )

    Column(modifier.fillMaxSize()) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = vm::buscar,
            placeholder = { Text("Buscar por estudiante o número") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            opciones.forEach { (estado, texto) ->
                FilterChip(
                    selected = filtro == estado,
                    onClick = { vm.elegirFiltro(estado) },
                    label = { Text(texto) }
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (pedidos.isEmpty()) {
                item {
                    Text("No hay pedidos", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            items(pedidos, key = { it.pedido.id }) { p ->
                val estado = EstadoPedido.valueOf(p.pedido.estado)
                PedidoCard(
                    numero = p.pedido.id,
                    fecha = p.usuario.nombre,
                    resumen = p.detalles.joinToString { it.servicio.nombre },
                    total = p.pedido.total,
                    estado = estado,
                    onClick = { onAbrir(p.pedido.id) }
                ) {
                    val accion = when (estado) {
                        EstadoPedido.RECIBIDO -> "Iniciar"
                        EstadoPedido.EN_PROCESO -> "Marcar listo"
                        EstadoPedido.LISTO -> "Entregar"
                        else -> null
                    }
                    if (accion != null) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                            OutlinedButton(onClick = {
                                if (estado == EstadoPedido.RECIBIDO) vm.iniciar(p) else onAbrir(p.pedido.id)
                            }) {
                                Text(accion)
                            }
                        }
                    }
                }
            }
        }
    }
}
