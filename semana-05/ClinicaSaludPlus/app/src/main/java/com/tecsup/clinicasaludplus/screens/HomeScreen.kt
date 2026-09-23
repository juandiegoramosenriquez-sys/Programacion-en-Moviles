package com.tecsup.clinicasaludplus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.clinicasaludplus.model.DataSource
import com.tecsup.clinicasaludplus.model.Doctor
import com.tecsup.clinicasaludplus.navigation.Screen
import kotlinx.coroutines.launch

val MoradoClinica = Color(0xFF5B2C83)
val LavandaClinica = Color(0xFFF3EEF8)
val DoradoEstrella = Color(0xFFC9A227)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var especialidadSeleccionada by remember { mutableStateOf("Todos") }

    val doctoresFiltrados = if (especialidadSeleccionada == "Todos") {
        DataSource.doctores
    } else {
        DataSource.doctores.filter { it.especialidad == especialidadSeleccionada }
    }

    // El Drawer ENVUELVE al Scaffold: se dibuja por encima de toda la pantalla,
    // incluida la topBar. Por eso no es un parámetro más del Scaffold.
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuLateral(
                onInicio = { scope.launch { drawerState.close() } },
                onMisCitas = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.MisCitas.route)
                },
                onHistorial = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Historial.route)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Clínica Salud+", fontWeight = FontWeight.Bold)
                            Text("Hola, Juan Diego", style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MoradoClinica,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Chips de especialidad: filtran la lista (selección única)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(DataSource.especialidades) { especialidad ->
                        FilterChip(
                            selected = especialidadSeleccionada == especialidad,
                            onClick = { especialidadSeleccionada = especialidad },
                            label = { Text(especialidad) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MoradoClinica,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Médicos disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(doctoresFiltrados) { doctor ->
                        TarjetaDoctor(
                            doctor = doctor,
                            onClick = { navController.navigate(Screen.Doctor.createRoute(doctor.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaDoctor(doctor: Doctor, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LavandaClinica)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoMedico(tamano = 44)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(doctor.nombre, fontWeight = FontWeight.Bold)
                Text(
                    doctor.especialidad,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = DoradoEstrella,
                modifier = Modifier.size(18.dp)
            )
            Text(" ${doctor.calificacion}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun IconoMedico(tamano: Int) {
    Box(
        modifier = Modifier
            .size(tamano.dp)
            .background(Color(0xFFE8DDF3), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = null,
            tint = MoradoClinica,
            modifier = Modifier.size((tamano * 0.7f).dp)
        )
    }
}

@Composable
fun MenuLateral(onInicio: () -> Unit, onMisCitas: () -> Unit, onHistorial: () -> Unit) {
    ModalDrawerSheet {
        // Encabezado con las iniciales del paciente
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFE8DDF3), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("JR", color = MoradoClinica, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Juan Diego Ramos", fontWeight = FontWeight.Bold)
                Text("Paciente", style = MaterialTheme.typography.bodySmall)
            }
        }
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            label = { Text("Inicio") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            selected = true,
            onClick = onInicio,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Mis citas") },
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            selected = false,
            onClick = onMisCitas,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Historial médico") },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
            selected = false,
            onClick = onHistorial,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
