package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.R
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.CirculoEmoji
import com.saludplus.citas.ui.components.TarjetaEspecialidad
import com.saludplus.citas.ui.components.fechaEnTexto
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Morado
import com.saludplus.citas.ui.theme.MoradoClaro
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.NaranjaClaro
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.Verde
import com.saludplus.citas.ui.theme.VerdeClaro
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onAgendarCita: () -> Unit,
    onEspecialidadClick: (Int) -> Unit,
    onNotificaciones: () -> Unit,
    onMisCitas: () -> Unit,
    onResultados: () -> Unit,
    onPerfil: () -> Unit,
    onSedes: () -> Unit = {},          // Paso 3
    onDoctores: () -> Unit = {},       // Paso 4
    onCerrarSesion: () -> Unit = {}
) {
    val usuario = Repositorio.usuarioActual
    val nombre = usuario?.nombres?.split(" ")?.firstOrNull() ?: "Paciente"
    val proxima = Repositorio.citasDelUsuario().firstOrNull()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var mostrarDialogo by remember { mutableStateOf(false) }

    // Cierra el menú y luego ejecuta la acción
    fun irA(accion: () -> Unit) {
        scope.launch { drawerState.close() }
        accion()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Encabezado azul con logo y datos del usuario
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AzulPrimario)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo_saludplus),
                            contentDescription = "Logo",
                            modifier = Modifier.size(44.dp)
                        )
                    }
                    Text("Clínica SaludPlus", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "${usuario?.nombres ?: ""} ${usuario?.apellidos ?: ""}",
                        color = Color.White
                    )
                    Text(usuario?.correo ?: "", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                }

                Spacer(Modifier.height(12.dp))

                ItemMenu("Sede", Icons.Default.LocationOn) { irA(onSedes) }
                ItemMenu("Doctores", Icons.Default.Face) { irA(onDoctores) }
                ItemMenu("Agenda", Icons.Default.DateRange) { irA(onMisCitas) }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    label = { Text("Cerrar sesión", color = Rojo, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Rojo) },
                    selected = false,
                    onClick = { irA { mostrarDialogo = true } },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
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
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // ☰  Saludo  🔔
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("¡Hola, $nombre!", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        Text("¿Qué deseas hacer hoy?", color = TextoSecundario)
                    }
                    IconButton(onClick = onNotificaciones) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TarjetaAccion("Agendar cita", Icons.Default.DateRange, AzulClaro, AzulPrimario, onAgendarCita, Modifier.weight(1f))
                    TarjetaAccion("Mis citas", Icons.Default.CheckCircle, VerdeClaro, Verde, onMisCitas, Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TarjetaAccion("Mis datos", Icons.Default.Person, MoradoClaro, Morado, onPerfil, Modifier.weight(1f))
                    TarjetaAccion("Resultados", Icons.Default.List, NaranjaClaro, Naranja, onResultados, Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Especialidades destacadas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onAgendarCita) {
                        Text("Ver todas", color = AzulPrimario)
                    }
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(Repositorio.especialidadesDestacadas()) { especialidad ->
                        TarjetaEspecialidad(
                            especialidad = especialidad,
                            onClick = { onEspecialidadClick(especialidad.id) },
                            modifier = Modifier.width(110.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CirculoEmoji("📅", AzulClaro, 44.dp)
                        Column {
                            Text("Próxima cita", fontWeight = FontWeight.Bold)
                            if (proxima == null) {
                                Text("No tienes citas próximas", color = TextoSecundario)
                            } else {
                                Text(Repositorio.obtenerMedico(proxima.medicoId)?.nombre ?: "-", fontWeight = FontWeight.SemiBold)
                                Text("${fechaEnTexto(proxima.fecha)} - ${proxima.hora}", color = TextoSecundario)
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmación antes de cerrar sesión
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Cerrar sesión?") },
            text = { Text("Tendrás que volver a ingresar tu correo y contraseña.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    Repositorio.cerrarSesion()
                    onCerrarSesion()
                }) {
                    Text("Sí, salir", color = Rojo)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun ItemMenu(texto: String, icono: ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(texto, fontWeight = FontWeight.SemiBold) },
        icon = { Icon(icono, contentDescription = null, tint = AzulPrimario) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
private fun TarjetaAccion(
    texto: String,
    icono: ImageVector,
    fondo: Color,
    acento: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = fondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icono, contentDescription = null, tint = acento, modifier = Modifier.size(36.dp))
            Spacer(Modifier.height(8.dp))
            Text(texto, color = acento, fontWeight = FontWeight.Bold)
        }
    }
}