package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.R
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FilaInfo
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.components.fechaEnTexto
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Fondo
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.Verde
import com.saludplus.citas.ui.theme.VerdeClaro

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onVerMisCitas: () -> Unit,
    onIrAlInicio: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = Repositorio.obtenerMedico(cita?.medicoId ?: 0)
    val especialidad = Repositorio.obtenerEspecialidad(medico?.especialidadId ?: 0)
    val sede = Repositorio.obtenerSede(cita?.sedeId ?: 0)
    val codigo = "SP-" + (cita?.id ?: 0).toString().padStart(4, '0')

    var mostrar by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { mostrar = true }
    val escala by animateFloatAsState(
        targetValue = if (mostrar) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "check"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_saludplus),
                contentDescription = "Logo",
                modifier = Modifier.size(32.dp)
            )
            Text("Clínica SaludPlus", color = AzulPrimario, fontWeight = FontWeight.Bold)
        }

        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .size(110.dp)
                .scale(escala)
                .clip(CircleShape)
                .background(VerdeClaro),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Verde),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
            }
        }

        Text("¡Cita agendada!", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Código de cita: $codigo", color = Naranja, fontWeight = FontWeight.SemiBold)

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
                FilaInfo("📍", sede?.nombre ?: "Sede", sede?.direccion ?: "-")
            }
        }

        Text("Llega 15 minutos antes con tu DNI", color = TextoSecundario, fontSize = 13.sp)

        BotonPrincipal(texto = "Ver mi agenda", onClick = onVerMisCitas)

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