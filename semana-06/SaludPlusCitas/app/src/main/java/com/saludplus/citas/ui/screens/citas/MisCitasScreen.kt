package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior

@Composable
fun MisCitasScreen(
    onCitaClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val lista = Repositorio.citasDelUsuario()

    Scaffold(
        topBar = { BarraSuperior(titulo = "Mis citas", onBack = onBack) }
    ) { padding ->
        if (lista.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes citas", color = Color.Gray, fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista) { cita ->
                    val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
                    val medico = Repositorio.obtenerMedico(cita.medicoId)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCitaClick(cita.id) }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(especialidad?.nombre ?: "-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(medico?.nombre ?: "-")
                            Text("📅 ${cita.fecha}   🕐 ${cita.hora}", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}