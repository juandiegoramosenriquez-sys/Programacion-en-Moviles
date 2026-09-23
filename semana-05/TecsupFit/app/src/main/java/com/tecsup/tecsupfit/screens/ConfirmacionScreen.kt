package com.tecsup.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.tecsupfit.model.DataSource
import com.tecsup.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionScreen(navController: NavController, claseId: Int, horario: String) {
    val clase = DataSource.clases.first { it.id == claseId }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Confirmación") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Reserva confirmada!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Clase: ${clase.nombre}")
                    Text("Horario: $horario")
                    Text("Instructor: ${clase.instructor}")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.Reservas.route) {
                        popUpTo(Screen.Inicio.route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver reservas")
            }
        }
    }
}
