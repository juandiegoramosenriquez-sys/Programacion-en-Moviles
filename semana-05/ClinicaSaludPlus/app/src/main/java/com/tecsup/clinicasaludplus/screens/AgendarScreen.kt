package com.tecsup.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.clinicasaludplus.model.DataSource
import com.tecsup.clinicasaludplus.model.Doctor
import com.tecsup.clinicasaludplus.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarScreen(
    navController: NavController,
    doctorId: Int,
    onConfirmar: (Doctor, String, String) -> Unit
) {
    val doctor = DataSource.doctores.first { it.id == doctorId }

    // Selección única: solo se guarda UNA fecha y UNA hora (como un RadioButton)
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
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
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Cita con ${doctor.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(doctor.especialidad, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(24.dp))
            Text("Selecciona fecha", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(DataSource.fechas) { fecha ->
                    FilterChip(
                        selected = fechaSeleccionada == fecha,
                        onClick = { fechaSeleccionada = fecha },
                        label = { Text(fecha) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Selecciona hora", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(DataSource.horas) { hora ->
                    FilterChip(
                        selected = horaSeleccionada == hora,
                        onClick = { horaSeleccionada = hora },
                        label = { Text(hora) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                // Solo se puede confirmar cuando hay fecha y hora elegidas
                enabled = fechaSeleccionada != null && horaSeleccionada != null,
                onClick = {
                    val fecha = fechaSeleccionada ?: return@Button
                    val hora = horaSeleccionada ?: return@Button
                    onConfirmar(doctor, fecha, hora)
                    navController.navigate(Screen.Confirmacion.createRoute(doctor.id, fecha, hora))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar cita")
            }
        }
    }
}
