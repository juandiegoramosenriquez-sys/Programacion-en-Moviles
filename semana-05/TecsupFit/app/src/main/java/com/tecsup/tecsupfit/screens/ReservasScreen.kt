package com.tecsup.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.tecsupfit.model.Reserva
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(reservas: List<Reserva>, onCancelar: (Reserva) -> Unit) {
    // Reserva que el usuario quiere cancelar; si es null, el diálogo está cerrado
    var reservaPorCancelar by remember { mutableStateOf<Reserva?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis reservas") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                            // Solo las reservas confirmadas se pueden cancelar
                            if (reserva.estado == "Confirmada") {
                                TextButton(
                                    onClick = { reservaPorCancelar = reserva },
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFB3261E))
                                ) {
                                    Text("Cancelar reserva")
                                }
                            }
                        }
                        EstadoEtiqueta(reserva.estado)
                    }
                }
            }
        }
    }

    // MEJORA CON IA: diálogo de confirmación antes de cancelar
    reservaPorCancelar?.let { reserva ->
        AlertDialog(
            onDismissRequest = { reservaPorCancelar = null },
            title = { Text("Cancelar reserva") },
            text = {
                Text("¿Seguro que deseas cancelar tu clase de ${reserva.clase.nombre} (${reserva.horario})? Liberarás tu cupo.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onCancelar(reserva)
                        reservaPorCancelar = null
                        scope.launch { snackbarHostState.showSnackbar("Reserva cancelada") }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFB3261E))
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { reservaPorCancelar = null }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun EstadoEtiqueta(estado: String) {
    // Cada estado con su propio color: Confirmada, Completada y Cancelada
    val (fondo, texto) = when (estado) {
        "Confirmada" -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        "Cancelada" -> Color(0xFFFCE4E4) to Color(0xFFB3261E)
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(color = fondo, shape = MaterialTheme.shapes.small) {
        Text(
            text = estado,
            color = texto,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
