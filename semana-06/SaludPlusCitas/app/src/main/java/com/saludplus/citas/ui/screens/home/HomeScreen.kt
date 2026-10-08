package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.saludplus.citas.data.repository.Repositorio
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import com.saludplus.citas.ui.components.TarjetaEspecialidad


@Composable
fun HomeScreen (
    onAgendarCita: () -> Unit,
    onEspecialidadClick: (Int) -> Unit,
    onNotificaciones: () -> Unit,
    onMisCitas: () -> Unit,
    onResultados: () -> Unit,
    onPerfil: () -> Unit
) {
    val nombre = Repositorio.usuarioActual?.nombres ?: "Paciente"

    Scaffold (
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onMisCitas,
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Citas") },
                    label = { Text("Citas") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onResultados,
                    icon = { Icon(Icons.Default.List, contentDescription = "Resultados") },
                    label = { Text("Resultados") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onPerfil,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hola, $nombre", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("¿Cómo te sientes hoy?", color = Color.Gray)
                }
                IconButton(onClick = onNotificaciones) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAgendarCita() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                    Column {
                        Text("Agendar cita", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Reserva con un especialista", color = Color.White)
                    }
                }
            }

            Text("Especialidades destacadas", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(Repositorio.especialidadesDestacadas()) { especialidad ->
                    TarjetaEspecialidad(
                        especialidad = especialidad,
                        onClick = { onEspecialidadClick(especialidad.id) },
                        modifier = Modifier.width(120.dp)
                    )
                }
            }

            val proxima = Repositorio.citasDelUsuario().firstOrNull()

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Próxima cita", fontWeight = FontWeight.Bold)
                    if (proxima == null) {
                        Text("No tienes citas próximas", color = Color.Gray)
                    } else {
                        Text(Repositorio.obtenerMedico(proxima.medicoId)?.nombre ?: "-")
                        Text(" ${proxima.fecha}    ${proxima.hora}", color = Color.Gray)
                    }
                }
            }
        }
    }
}
