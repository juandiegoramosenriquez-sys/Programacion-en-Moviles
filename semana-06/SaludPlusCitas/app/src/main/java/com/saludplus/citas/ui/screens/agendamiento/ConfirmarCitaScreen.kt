package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onCitaConfirmada: (Int) -> Unit,
    onBack: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = Repositorio.obtenerEspecialidad(medico?.especialidadId ?: 0)
    var error by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Confirmar cita", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Revisa los datos de tu cita", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilaResumen("Especialidad", especialidad?.nombre ?: "-")
                    FilaResumen("Médico", medico?.nombre ?: "-")
                    FilaResumen("Fecha", fecha)
                    FilaResumen("Hora", hora)
                    FilaResumen("Paciente", Repositorio.usuarioActual?.nombres ?: "-")
                }
            }

            if (error.isNotEmpty()) {
                Text(error, color = Color.Red)
            }

            Spacer(Modifier.weight(1f))

            BotonPrincipal(
                texto = "Confirmar cita",
                onClick = {
                    val cita = Repositorio.agendarCita(medicoId, fecha, hora)
                    if (cita != null) {
                        onCitaConfirmada(cita.id)
                    } else {
                        error = "Ese horario ya no está disponible"
                    }
                }
            )
        }
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: String) {
    Row {
        Text("$etiqueta: ", fontWeight = FontWeight.Bold)
        Text(valor)
    }
}