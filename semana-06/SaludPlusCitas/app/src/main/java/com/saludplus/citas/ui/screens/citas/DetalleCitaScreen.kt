package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.FilaInfo
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.components.fechaEnTexto
import com.saludplus.citas.ui.theme.Rojo

@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onBack: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = Repositorio.obtenerMedico(cita?.medicoId ?: 0)
    val especialidad = Repositorio.obtenerEspecialidad(cita?.especialidadId ?: 0)
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Detalle de cita", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaMedico(medico, especialidad?.nombre ?: "")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FilaInfo("📅", "Fecha", cita?.fecha?.let { fechaEnTexto(it) } ?: "-")
                    FilaInfo("🕐", "Hora", cita?.hora ?: "-")
                    FilaInfo("🏥", "Tipo de atención", "Consulta presencial")
                    FilaInfo("📍", "Dirección", "Av. Los Olivos 123, Lima")
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { mostrarDialogo = true },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Rojo),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Cancelar cita", fontWeight = FontWeight.Bold)
            }
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("¿Cancelar cita?") },
                text = { Text("Esta acción no se puede deshacer") },
                confirmButton = {
                    TextButton(onClick = {
                        Repositorio.cancelarCita(citaId)
                        mostrarDialogo = false
                        onBack()
                    }) {
                        Text("Sí, cancelar", color = Rojo)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}
