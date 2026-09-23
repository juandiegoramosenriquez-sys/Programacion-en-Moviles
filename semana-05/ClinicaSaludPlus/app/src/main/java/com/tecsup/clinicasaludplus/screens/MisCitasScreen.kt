package com.tecsup.clinicasaludplus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.clinicasaludplus.model.Cita
import com.tecsup.clinicasaludplus.model.EstadoCita
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    navController: NavController,
    citas: List<Cita>,
    onCancelar: (Cita) -> Unit
) {
    // Cita que el usuario quiere cancelar; si es null, el diálogo está cerrado
    var citaPorCancelar by remember { mutableStateOf<Cita?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (citas.isEmpty()) {
            Text(
                "Todavía no tienes citas agendadas.",
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Las más recientes primero
                items(citas.reversed(), key = { it.id }) { cita ->
                    TarjetaCita(
                        cita = cita,
                        onCancelarClick = { citaPorCancelar = cita }
                    )
                }
            }
        }
    }

    // MEJORA CON IA: diálogo de confirmación antes de cancelar
    citaPorCancelar?.let { cita ->
        AlertDialog(
            onDismissRequest = { citaPorCancelar = null },
            title = { Text("Cancelar cita") },
            text = {
                Text("¿Seguro que deseas cancelar tu cita con ${cita.doctor.nombre} el ${cita.fecha} a las ${cita.hora}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onCancelar(cita)
                        citaPorCancelar = null
                        scope.launch { snackbarHostState.showSnackbar("Cita cancelada") }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFB3261E))
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { citaPorCancelar = null }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun TarjetaCita(cita: Cita, onCancelarClick: () -> Unit) {
    // Cada estado se diferencia visualmente con su propio color
    val (texto, fondo, colorTexto) = when (cita.estado) {
        EstadoCita.CONFIRMADA -> Triple("Confirmada", Color(0xFFE3F4EA), Color(0xFF1E7B45))
        EstadoCita.COMPLETADA -> Triple("Completada", Color(0xFFE6E6E6), Color(0xFF555555))
        EstadoCita.CANCELADA -> Triple("Cancelada", Color(0xFFFCE4E4), Color(0xFFB3261E))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LavandaClinica)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Borde morado a la izquierda solo en las citas confirmadas (como en el Word)
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(if (cita.estado == EstadoCita.CONFIRMADA) MoradoClinica else Color.Transparent)
            )
            Column(modifier = Modifier.padding(14.dp)) {
                Text(cita.doctor.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "${cita.fecha}, ${cita.hora}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        texto,
                        color = colorTexto,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .background(fondo, RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                    // Solo las citas confirmadas se pueden cancelar
                    if (cita.estado == EstadoCita.CONFIRMADA) {
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = onCancelarClick,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFB3261E))
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}
