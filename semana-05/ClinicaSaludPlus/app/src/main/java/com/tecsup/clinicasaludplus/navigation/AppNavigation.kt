package com.tecsup.clinicasaludplus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.clinicasaludplus.screens.AgendarScreen
import com.tecsup.clinicasaludplus.screens.ConfirmacionScreen
import com.tecsup.clinicasaludplus.screens.DoctorScreen
import com.tecsup.clinicasaludplus.screens.HistorialScreen
import com.tecsup.clinicasaludplus.screens.HomeScreen
import com.tecsup.clinicasaludplus.screens.MisCitasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

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
            AgendarScreen(navController, doctorId)
        }

        composable(
            route = Screen.Confirmacion.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            ConfirmacionScreen(navController, doctorId)
        }
    }
}
