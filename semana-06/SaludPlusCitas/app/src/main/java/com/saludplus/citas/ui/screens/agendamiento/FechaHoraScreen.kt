package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.ChipSeleccion
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.components.diaCorto
import com.saludplus.citas.ui.components.diasHabiles
import com.saludplus.citas.ui.components.mesYAnio
import com.saludplus.citas.ui.theme.TextoSecundario
import java.time.LocalDate

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = Repositorio.obtenerEspecialidad(medico?.especialidadId ?: 0)
    val hoy = remember { LocalDate.now() }

    var semana by remember { mutableIntStateOf(0) }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    val dias = diasHabiles(hoy.plusWeeks(semana.toLong()), 5)

    Scaffold(
        topBar = { BarraSuperior(titulo = "Seleccionar fecha y hora", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaMedico(medico, especialidad?.nombre ?: "")

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        semana--
                        fecha = ""
                        hora = ""
                    },
                    enabled = semana > 0
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Semana anterior")
                }
                Text(
                    text = mesYAnio(dias.first()),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                IconButton(
                    onClick = {
                        semana++
                        fecha = ""
                        hora = ""
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Semana siguiente")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dias.forEach { dia ->
                    val valor = dia.toString()
                    ChipSeleccion(
                        texto = "${diaCorto(dia)}\n${dia.dayOfMonth}",
                        seleccionado = valor == fecha,
                        onClick = {
                            fecha = valor
                            hora = ""
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Text("Elige una hora", fontWeight = FontWeight.Bold)
            if (fecha.isEmpty()) {
                Text("Primero selecciona un día", color = TextoSecundario)
                Spacer(Modifier.weight(1f))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(Repositorio.horariosDisponibles(medicoId, fecha)) { h ->
                        ChipSeleccion(
                            texto = h,
                            seleccionado = h == hora,
                            onClick = { hora = h },
                            modifier = Modifier.fillMaxWidth()
                        )
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
