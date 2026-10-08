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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.CirculoEmoji
import com.saludplus.citas.ui.components.colorDeFondo
import com.saludplus.citas.ui.theme.TextoSecundario

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
                placeholder = { Text("Buscar especialidad...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            if (lista.isEmpty()) {
                Text("No se encontraron especialidades", color = TextoSecundario)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(lista) { especialidad ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onEspecialidadClick(especialidad.id) },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                CirculoEmoji(especialidad.icono, colorDeFondo(especialidad.id))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(especialidad.nombre, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                    Text(especialidad.descripcion, color = TextoSecundario, fontSize = 14.sp)
                                }
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = TextoSecundario
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
