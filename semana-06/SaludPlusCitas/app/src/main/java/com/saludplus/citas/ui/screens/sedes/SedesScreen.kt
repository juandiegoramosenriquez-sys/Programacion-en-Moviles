package com.saludplus.citas.ui.screens.sedes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.CirculoEmoji
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun SedesScreen(
    onSedeElegida: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Elige tu sede", onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("¿Dónde quieres atenderte?", color = TextoSecundario)
            }

            items(Repositorio.sedes) { sede ->
                val medicos = Repositorio.medicosDeSede(sede.id)
                val totalEspecialidades = medicos.map { it.especialidadId }.distinct().size

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Repositorio.elegirSede(sede.id)
                            onSedeElegida()
                        },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        CirculoEmoji("🏥", AzulClaro, 52.dp)
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(sede.nombre, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("📍 ${sede.direccion}", color = TextoSecundario, fontSize = 14.sp)
                            Text("🕐 ${sede.horario}", color = TextoSecundario, fontSize = 14.sp)
                            Text(
                                "$totalEspecialidades especialidades · ${medicos.size} médicos",
                                color = Naranja,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = TextoSecundario
                        )
                    }
                }
            }
        }
    }
}