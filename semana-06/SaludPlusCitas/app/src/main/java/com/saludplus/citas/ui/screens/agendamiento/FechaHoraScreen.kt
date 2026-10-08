package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val dias = listOf("2026-10-13", "2026-10-14", "2026-10-15", "2026-10-16", "2026-10-17")

    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Fecha y hora", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(medico?.nombre ?: "Médico", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Text("Elige un día", fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(dias) { dia ->
                    if (dia == fecha) {
                        Button(onClick = { }) { Text(dia) }
                    } else {
                        OutlinedButton(onClick = {
                            fecha = dia
                            hora = ""
                        }) { Text(dia) }
                    }
                }
            }

            Text("Elige una hora", fontWeight = FontWeight.Bold)
            if (fecha.isEmpty()) {
                Text("Primero selecciona un día", color = Color.Gray)
                Spacer(Modifier.weight(1f))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Repositorio.horariosDisponibles(medicoId, fecha)) { h ->
                        if (h == hora) {
                            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text(h) }
                        } else {
                            OutlinedButton(onClick = { hora = h }, modifier = Modifier.fillMaxWidth()) { Text(h) }
                        }
                    }
                }
            }

            BotonPrincipal(
                texto = "Continuar",
                onClick = { onContinuar(fecha, hora) },
                enabled = fecha.isNotEmpty() && hora.isNotEmpty()
            )
        }
    }
}