package com.tecsup.clinicasaludplus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.clinicasaludplus.model.Cita
import com.tecsup.clinicasaludplus.model.DataSource
import com.tecsup.clinicasaludplus.model.EstadoCita
import com.tecsup.clinicasaludplus.screens.AgendarScreen
import com.tecsup.clinicasaludplus.screens.ConfirmacionScreen
import com.tecsup.clinicasaludplus.screens.DoctorScreen
import com.tecsup.clinicasaludplus.screens.HistorialScreen
import com.tecsup.clinicasaludplus.screens.HomeScreen
import com.tecsup.clinicasaludplus.screens.MisCitasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Lista de citas compartida entre pantallas (sin ViewModel: solo remember)
    val citas = remember { mutableStateListOf<Cita>().apply { addAll(DataSource.citasIniciales) } }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.MisCitas.route) {
            MisCitasScreen(navController)
        }

        composable(Screen.Historial.route) {
            HistorialScreen(navController)
        }

        composable(
            route = Screen.Doctor.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            DoctorScreen(navController, doctorId)
        }

        composable(
            route = Screen.Agendar.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            AgendarScreen(
                navController = navController,
                doctorId = doctorId,
                onConfirmar = { doctor, fecha, hora ->
                    val nuevoId = (citas.maxOfOrNull { it.id } ?: 0) + 1
                    citas.add(Cita(nuevoId, doctor, fecha, hora, EstadoCita.CONFIRMADA))
                }
            )
        }

        // Confirmación recibe 3 parámetros: médico, fecha y hora
        composable(
            route = Screen.Confirmacion.route,
            arguments = listOf(
                navArgument("doctorId") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            ConfirmacionScreen(navController, doctorId, fecha, hora)
        }
    }
}
