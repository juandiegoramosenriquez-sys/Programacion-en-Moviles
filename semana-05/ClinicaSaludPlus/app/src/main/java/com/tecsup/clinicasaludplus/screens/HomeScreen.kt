package com.tecsup.clinicasaludplus.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.clinicasaludplus.model.DataSource
import com.tecsup.clinicasaludplus.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Clínica Salud+") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { navController.navigate(Screen.MisCitas.route) }) {
                    Text("Mis citas")
                }
                OutlinedButton(onClick = { navController.navigate(Screen.Historial.route) }) {
                    Text("Historial")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Médicos", style = MaterialTheme.typography.titleMedium)

            LazyColumn {
                items(DataSource.doctores) { doctor ->
                    ListItem(
                        headlineContent = { Text(doctor.nombre) },
                        supportingContent = { Text(doctor.especialidad) },
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Doctor.createRoute(doctor.id))
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
