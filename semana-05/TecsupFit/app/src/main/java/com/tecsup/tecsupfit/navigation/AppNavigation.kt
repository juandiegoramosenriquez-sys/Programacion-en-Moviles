package com.tecsup.tecsupfit.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.tecsupfit.model.DataSource
import com.tecsup.tecsupfit.model.Reserva
import com.tecsup.tecsupfit.screens.ConfirmacionScreen
import com.tecsup.tecsupfit.screens.DetalleClaseScreen
import com.tecsup.tecsupfit.screens.InicioScreen
import com.tecsup.tecsupfit.screens.PerfilScreen
import com.tecsup.tecsupfit.screens.ReservasScreen
import com.tecsup.tecsupfit.screens.RutinasScreen

data class Pestana(val titulo: String, val ruta: String, val icono: ImageVector)

val pestanas = listOf(
    Pestana("Inicio", Screen.Inicio.route, Icons.Filled.Home),
    Pestana("Reservas", Screen.Reservas.route, Icons.Filled.DateRange),
    Pestana("Rutinas", Screen.Rutinas.route, Icons.AutoMirrored.Filled.List),
    Pestana("Perfil", Screen.Perfil.route, Icons.Filled.Person)
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Lista de reservas con remember (sin ViewModel)
    val reservas = remember {
        mutableStateListOf<Reserva>().apply { addAll(DataSource.reservasIniciales) }
    }

    // Ruta en la que estamos ahora: sirve para resaltar la pestaña activa
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar {
                pestanas.forEach { pestana ->
                    NavigationBarItem(
                        selected = rutaActual == pestana.ruta,
                        onClick = {
                            navController.navigate(pestana.ruta) {
                                popUpTo(Screen.Inicio.route)
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(pestana.icono, contentDescription = pestana.titulo) },
                        label = { Text(pestana.titulo) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Inicio.route) {
                InicioScreen(navController)
            }

            composable(Screen.Reservas.route) {
                ReservasScreen(
                    reservas = reservas,
                    onCancelar = { reserva ->
                        // Se reemplaza la reserva por una copia con estado "Cancelada"
                        val indice = reservas.indexOf(reserva)
                        if (indice >= 0) reservas[indice] = reserva.copy(estado = "Cancelada")
                    }
                )
            }

            composable(Screen.Rutinas.route) {
                RutinasScreen()
            }

            composable(Screen.Perfil.route) {
                PerfilScreen(reservas)
            }

            composable(
                route = Screen.Detalle.route,
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0
                DetalleClaseScreen(navController, claseId, onReservar = { reservas.add(0, it) })
            }

            composable(
                route = Screen.Confirmacion.route,
                arguments = listOf(
                    navArgument("claseId") { type = NavType.IntType },
                    navArgument("horario") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0
                val horario = backStackEntry.arguments?.getString("horario") ?: ""
                ConfirmacionScreen(navController, claseId, horario)
            }
        }
    }
}
