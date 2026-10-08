package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FilaInfo
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.components.fechaEnTexto
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Fondo
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.Verde

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
            .background(Fondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Verde,
            modifier = Modifier.size(100.dp)
        )

        Text("¡Cita agendada!", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Te esperamos en la clínica", color = TextoSecundario)

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
            }
        }

        BotonPrincipal(texto = "Ver mis citas", onClick = onVerMisCitas)

        OutlinedButton(
            onClick = onIrAlInicio,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Ir al inicio", color = AzulPrimario, fontWeight = FontWeight.Bold)
        }
    }
}
