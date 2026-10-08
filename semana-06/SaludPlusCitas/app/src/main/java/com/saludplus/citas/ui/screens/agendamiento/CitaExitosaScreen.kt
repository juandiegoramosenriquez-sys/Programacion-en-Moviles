package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.fechaEnTexto

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onVerMisCitas: () -> Unit,
    onIrAlInicio: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = Repositorio.obtenerMedico(cita?.medicoId ?: 0)
    val especialidad = Repositorio.obtenerEspecialidad(medico?.especialidadId ?: 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF2E7D32),
            modifier = Modifier.size(100.dp)
        )

        Text("¡Cita agendada!", fontSize = 26.sp, fontWeight = FontWeight.Bold)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilaExito("Especialidad", especialidad?.nombre ?: "-")
                FilaExito("Médico", medico?.nombre ?: "-")
                FilaExito("Fecha", cita?.fecha?.let { fechaEnTexto(it) } ?: "-")
                FilaExito("Hora", cita?.hora ?: "-")
            }
        }

        BotonPrincipal(texto = "Ver mis citas", onClick = onVerMisCitas)

        OutlinedButton(
            onClick = onIrAlInicio,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ir al inicio")
        }
    }
}

@Composable
private fun FilaExito(etiqueta: String, valor: String) {
    Row {
        Text("$etiqueta: ", fontWeight = FontWeight.Bold)
        Text(valor)
    }
}