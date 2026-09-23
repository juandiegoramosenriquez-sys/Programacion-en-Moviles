package com.tecsup.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.tecsupfit.model.Reserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(reservas: List<Reserva>) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis reservas") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reservas) { reserva ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(reserva.clase.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(reserva.horario)
                        }
                        EstadoEtiqueta(reserva.estado)
                    }
                }
            }
        }
    }
}

@Composable
fun EstadoEtiqueta(estado: String) {
    // Confirmada y Completada con colores distintos
    val fondo = if (estado == "Confirmada") {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    Surface(color = fondo, shape = MaterialTheme.shapes.small) {
        Text(
            text = estado,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
