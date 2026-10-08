package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onMedicoClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val lista = Repositorio.medicosPorEspecialidad(especialidadId)

    Scaffold(
        topBar = { BarraSuperior(titulo = especialidad?.nombre ?: "Médicos", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (lista.isEmpty()) {
                Text("No hay médicos disponibles", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(lista) { medico ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onMedicoClick(medico.id) }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(medico.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("⭐ ${medico.calificacion}")
                                Text("${medico.aniosExperiencia} años de experiencia", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}