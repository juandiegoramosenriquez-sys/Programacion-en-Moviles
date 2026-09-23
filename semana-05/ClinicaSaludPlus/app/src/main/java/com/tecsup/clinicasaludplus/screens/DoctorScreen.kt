package com.tecsup.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.clinicasaludplus.model.DataSource
import com.tecsup.clinicasaludplus.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorScreen(navController: NavController, doctorId: Int) {
    val doctor = DataSource.doctores.first { it.id == doctorId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico") },
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
            Text(doctor.nombre, style = MaterialTheme.typography.headlineSmall)
            Text(doctor.especialidad)
            Text(doctor.horario)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate(Screen.Agendar.createRoute(doctor.id)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agendar cita")
            }
        }
    }
}
