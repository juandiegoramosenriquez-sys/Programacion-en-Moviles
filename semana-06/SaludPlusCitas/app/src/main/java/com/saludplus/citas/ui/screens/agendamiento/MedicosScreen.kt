package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.Avatar
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.theme.Amarillo
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.Verde
import com.saludplus.citas.ui.theme.VerdeClaro
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onMedicoClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val lista = Repositorio.medicosPorEspecialidad(especialidadId)
    val hoy = LocalDate.now()
    val hoyEsHabil = hoy.dayOfWeek != DayOfWeek.SATURDAY && hoy.dayOfWeek != DayOfWeek.SUNDAY

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = if (especialidad != null) "Médicos de ${especialidad.nombre}" else "Médicos",
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (lista.isEmpty()) {
                Text("No hay médicos disponibles", color = TextoSecundario)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(lista) { medico ->
                        val disponibleHoy = hoyEsHabil &&
                                Repositorio.horariosDisponibles(medico.id, hoy.toString()).isNotEmpty()

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onMedicoClick(medico.id) },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Avatar(medico.nombre)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(medico.nombre, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                    Text(especialidad?.nombre ?: "", color = TextoSecundario, fontSize = 14.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("★ ", color = Amarillo)
                                        Text(
                                            "${medico.calificacion}  ·  ${medico.aniosExperiencia} años de exp.",
                                            color = TextoSecundario,
                                            fontSize = 13.sp
                                        )
                                    }
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        text = if (disponibleHoy) "Disponible hoy" else "Disponible esta semana",
                                        color = Verde,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .background(VerdeClaro)
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
