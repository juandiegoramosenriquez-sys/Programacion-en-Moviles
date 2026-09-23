package com.tecsup.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.tecsupfit.model.Reserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(reservas: List<Reserva>) {
    val clasesTomadas = reservas.count { it.estado == "Completada" }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Perfil") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Diego Ramos", style = MaterialTheme.typography.headlineSmall)
            Text("diego.ramos@tecsup.edu.pe", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Estadistica("Clases tomadas", "$clasesTomadas", Modifier.weight(1f))
                Estadistica("Racha", "5 días", Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun Estadistica(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(valor, style = MaterialTheme.typography.headlineMedium)
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
