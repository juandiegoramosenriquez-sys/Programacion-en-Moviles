package com.saludplus.citas.ui.screens.doctores

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
import com.saludplus.citas.ui.components.Avatar
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.CirculoEmoji
import com.saludplus.citas.ui.components.colorDeFondo
import com.saludplus.citas.ui.theme.Amarillo
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun DoctoresScreen(
    onMedicoClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    val grupos = Repositorio.buscarDoctores(texto).groupBy { it.especialidadId }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Nuestros doctores", onBack = onBack) }
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
                placeholder = { Text("Buscar doctor o especialidad...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            if (grupos.isEmpty()) {
                Text("No se encontraron doctores", color = TextoSecundario)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    grupos.forEach { (especialidadId, lista) ->
                        val especialidad = Repositorio.obtenerEspecialidad(especialidadId)

                        item(key = "titulo-$especialidadId") {
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CirculoEmoji(especialidad?.icono ?: "🩺", colorDeFondo(especialidadId), 36.dp)
                                Text(
                                    especialidad?.nombre ?: "-",
                                    color = AzulPrimario,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        items(lista, key = { it.id }) { medico ->
                            val sede = Repositorio.obtenerSede(medico.sedeId)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onMedicoClick(medico.id) },
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Avatar(medico.nombre, 48.dp)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(medico.nombre, fontWeight = FontWeight.Bold)
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text("★ ${medico.calificacion}", color = Amarillo, fontSize = 13.sp)
                                            Text("· ${medico.aniosExperiencia} años de exp.", color = TextoSecundario, fontSize = 13.sp)
                                        }
                                        Text("🏥 ${sede?.nombre ?: "-"}", color = Naranja, fontSize = 13.sp)
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
}