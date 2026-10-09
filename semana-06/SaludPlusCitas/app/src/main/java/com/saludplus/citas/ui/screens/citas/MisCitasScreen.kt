package com.saludplus.citas.ui.screens.citas

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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.CirculoEmoji
import com.saludplus.citas.ui.components.colorDeFondo
import com.saludplus.citas.ui.components.fechaEnTexto
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun MisCitasScreen(
    onCitaClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val lista = Repositorio.citasDelUsuario()

    Scaffold(
        topBar = { BarraSuperior(titulo = "Mi agenda", onBack = onBack) }
    ) { padding ->
        if (lista.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                Text("📭", fontSize = 56.sp)
                Text("No tienes citas", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Agenda una desde el menú ☰ → Sede", color = TextoSecundario)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text("${lista.size} cita(s) programada(s)", color = TextoSecundario)
                }

                items(lista) { cita ->
                    val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val sede = Repositorio.obtenerSede(cita.sedeId)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCitaClick(cita.id) },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            CirculoEmoji(especialidad?.icono ?: "🩺", colorDeFondo(cita.especialidadId))
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(especialidad?.nombre ?: "-", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                Text(medico?.nombre ?: "-")
                                Text("📅 ${fechaEnTexto(cita.fecha)}", color = TextoSecundario, fontSize = 14.sp)
                                Text("🕐 ${cita.hora}", color = TextoSecundario, fontSize = 14.sp)
                                if (sede != null) {
                                    Text("🏥 ${sede.nombre}", color = Naranja, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
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