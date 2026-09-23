package com.tecsup.tecsupfit.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.tecsupfit.model.DataSource
import com.tecsup.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(navController: NavController) {
    var filtro by remember { mutableStateOf("Hoy") }
    val clasesFiltradas = DataSource.clases.filter { it.dia == filtro }

    Scaffold(
        topBar = { TopAppBar(title = { Text("TECSUP Fit") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(DataSource.filtros) { opcion ->
                    FilterChip(
                        selected = filtro == opcion,
                        onClick = { filtro = opcion },
                        label = { Text(opcion) }
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(clasesFiltradas) { clase ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Screen.Detalle.createRoute(clase.id)) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(clase.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = clase.horarios.joinToString(" · "),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
