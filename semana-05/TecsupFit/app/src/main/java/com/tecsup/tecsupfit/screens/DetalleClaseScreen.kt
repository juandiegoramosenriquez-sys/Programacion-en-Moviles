package com.tecsup.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.tecsupfit.model.DataSource
import com.tecsup.tecsupfit.model.Reserva
import com.tecsup.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleClaseScreen(
    navController: NavController,
    claseId: Int,
    reservas: List<Reserva>,
    onReservar: (Reserva) -> Unit
) {
    val clase = DataSource.clases.first { it.id == claseId }

    // Selección única: solo un horario a la vez (como un RadioButton)
    var horarioSeleccionado by remember { mutableStateOf<String?>(null) }

    // MEJORA CON IA: los cupos se calculan con las reservas confirmadas de esta clase.
    // Al reservar bajan y al cancelar (estado "Cancelada") vuelven a subir.
    val reservasActivas = reservas.filter { it.clase.id == clase.id && it.estado == "Confirmada" }
    val cuposDisponibles = clase.cupos - reservasActivas.size
    val yaReservado = reservasActivas.any { it.horario == horarioSeleccionado }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(clase.nombre, style = MaterialTheme.typography.headlineSmall)
            Text("Instructor: ${clase.instructor}")
            Text(
                text = "Cupos disponibles: $cuposDisponibles de ${clase.cupos}",
                color = if (cuposDisponibles > 0) MaterialTheme.colorScheme.onSurface else Color(0xFFB3261E)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Elige un horario", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(clase.horarios) { horario ->
                    val reservadoEnEsteHorario = reservasActivas.any { it.horario == horario }
                    FilterChip(
                        selected = horario == horarioSeleccionado,
                        onClick = { horarioSeleccionado = horario },
                        // Marca con ✓ los horarios que ya reservaste
                        label = { Text(if (reservadoEnEsteHorario) "✓ $horario" else horario) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val horario = horarioSeleccionado ?: return@Button
                    onReservar(Reserva(clase, horario, "Confirmada"))
                    navController.navigate(Screen.Confirmacion.createRoute(clase.id, horario))
                },
                // No deja reservar sin horario, sin cupos o el mismo horario dos veces
                enabled = horarioSeleccionado != null && !yaReservado && cuposDisponibles > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    when {
                        cuposDisponibles <= 0 -> "Sin cupos disponibles"
                        yaReservado -> "Ya reservaste este horario"
                        else -> "Reservar cupo"
                    }
                )
            }
        }
    }
}
