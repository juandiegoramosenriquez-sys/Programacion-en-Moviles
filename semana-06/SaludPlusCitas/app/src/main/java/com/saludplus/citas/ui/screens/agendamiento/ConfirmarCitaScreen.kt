package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FilaInfo
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.components.fechaEnTexto
import com.saludplus.citas.ui.theme.Rojo
import java.time.LocalTime

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
    val usuario = Repositorio.usuarioActual
    val horaFin = runCatching { LocalTime.parse(hora).plusMinutes(30).toString() }.getOrNull()
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
            TarjetaMedico(medico, especialidad?.nombre ?: "")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FilaInfo("📅", "Fecha", fechaEnTexto(fecha))
                    FilaInfo("🕐", "Hora", if (horaFin != null) "$hora a $horaFin" else hora)
                    FilaInfo("🏥", "Tipo de atención", "Consulta presencial")
                    FilaInfo("📍", "Dirección", "Av. Los Olivos 123, Lima")
                    FilaInfo("👤", "Paciente", "${usuario?.nombres ?: ""} ${usuario?.apellidos ?: ""}")
                }
            }

            if (error.isNotEmpty()) {
                Text(error, color = Rojo)
            }

            Spacer(Modifier.weight(1f))

            BotonPrincipal(
                texto = "Agendar cita",
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
