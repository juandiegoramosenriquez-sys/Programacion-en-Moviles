package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior

@Composable
fun EspecialidadesScreen(
    onEspecialidadClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    val lista = Repositorio.buscarEspecialidades(texto)

    Scaffold(
        topBar = { BarraSuperior(titulo = "Especialidades", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Buscar especialidad") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (lista.isEmpty()) {
                Text("No se encontraron especialidades", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(lista) { especialidad ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onEspecialidadClick(especialidad.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(especialidad.icono, fontSize = 32.sp)
                                Column {
                                    Text(especialidad.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Text(especialidad.descripcion, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}