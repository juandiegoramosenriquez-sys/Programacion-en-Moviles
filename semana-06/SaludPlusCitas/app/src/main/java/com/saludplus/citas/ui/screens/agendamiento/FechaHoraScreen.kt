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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.diaCorto
import com.saludplus.citas.ui.components.diasHabiles
import com.saludplus.citas.ui.components.mesYAnio
import java.time.LocalDate

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val hoy = remember { LocalDate.now() }

    var semana by remember { mutableIntStateOf(0) }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    val dias = diasHabiles(hoy.plusWeeks(semana.toLong()), 5)

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
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Semana anterior")
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
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Semana siguiente")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                dias.forEach { dia ->
                    val valor = dia.toString()
                    val texto = "${diaCorto(dia)}\n${dia.dayOfMonth}"
                    if (valor == fecha) {
                        Button(onClick = { }, modifier = Modifier.weight(1f)) {
                            Text(texto, textAlign = TextAlign.Center)
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                fecha = valor
                                hora = ""
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(texto, textAlign = TextAlign.Center)
                        }
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
