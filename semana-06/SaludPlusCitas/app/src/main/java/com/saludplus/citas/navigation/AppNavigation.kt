package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen
import com.saludplus.citas.ui.screens.citas.DetalleCitaScreen
import com.saludplus.citas.ui.screens.citas.MisCitasScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.Splash.ruta) {

        composable(Rutas.Splash.ruta) {
            SplashScreen(
                onIniciarSesion = { navController.navigate(Rutas.Login.ruta) },
                onRegistrarse = { navController.navigate(Rutas.Registro.ruta) }
            )
        }

        composable(Rutas.Registro.ruta) {
            RegistroScreen(
                onRegistroExitoso = { navController.navigate(Rutas.Login.ruta) },
                onVerTerminos = { navController.navigate(Rutas.Terminos.ruta) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Rutas.Login.ruta) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.Registro.ruta) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Rutas.Terminos.ruta) {
            TerminosScreen(onBack = { navController.popBackStack() })
        }

        composable(Rutas.Home.ruta) {
            HomeScreen(
                onAgendarCita = { navController.navigate(Rutas.Especialidades.ruta) },
                onEspecialidadClick = { id -> navController.navigate(Rutas.Medicos.crearRuta(id)) },
                onNotificaciones = { navController.navigate(Rutas.Notificaciones.ruta) },
                onMisCitas = { navController.navigate(Rutas.MisCitas.ruta) },
                onResultados = { navController.navigate(Rutas.Resultados.ruta) },
                onPerfil = { navController.navigate(Rutas.Perfil.ruta) }
            )
        }

        composable(Rutas.Especialidades.ruta) {
            EspecialidadesScreen(
                onEspecialidadClick = { id -> navController.navigate(Rutas.Medicos.crearRuta(id)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.Medicos.ruta,
            arguments = listOf(navArgument("especialidadId") { type = NavType.IntType })
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getInt("especialidadId") ?: 0
            MedicosScreen(
                especialidadId = especialidadId,
                onMedicoClick = { id -> navController.navigate(Rutas.FechaHora.crearRuta(id)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.FechaHora.ruta,
            arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            FechaHoraScreen(
                medicoId = medicoId,
                onContinuar = { fecha, hora ->
                    navController.navigate(Rutas.ConfirmarCita.crearRuta(medicoId, fecha, hora))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.ConfirmarCita.ruta,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            ConfirmarCitaScreen(
                medicoId = medicoId,
                fecha = fecha,
                hora = hora,
                onCitaConfirmada = { citaId ->
                    navController.navigate(Rutas.CitaExitosa.crearRuta(citaId)) {
                        popUpTo(Rutas.Home.ruta)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.CitaExitosa.ruta,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            CitaExitosaScreen(
                citaId = citaId,
                onVerMisCitas = {
                    navController.navigate(Rutas.MisCitas.ruta) {
                        popUpTo(Rutas.Home.ruta)
                    }
                },
                onIrAlInicio = { navController.popBackStack(Rutas.Home.ruta, inclusive = false) }
            )
        }

        composable(Rutas.MisCitas.ruta) {
            MisCitasScreen(
                onCitaClick = { id -> navController.navigate(Rutas.DetalleCita.crearRuta(id)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.DetalleCita.ruta,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            DetalleCitaScreen(
                citaId = citaId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Rutas.Perfil.ruta) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.Splash.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Rutas.Resultados.ruta) {
            ResultadosScreen(onBack = { navController.popBackStack() })
        }

        composable(Rutas.Notificaciones.ruta) {
            NotificacionesScreen(onBack = { navController.popBackStack() })
        }
    }
}
