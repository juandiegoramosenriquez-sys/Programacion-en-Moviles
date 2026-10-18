package com.tecsup.lavacampus.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.administrador.ServiciosScreen
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.estudiante.SplashScreen
import com.tecsup.lavacampus.ui.theme.LavaTheme
import com.tecsup.lavacampus.viewmodel.LavaViewModelFactory
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel

@Composable
fun NavLava(repo: LavaRepository) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(onTerminar = {
                nav.navigate(Rutas.SERVICIOS) {
                    popUpTo(Rutas.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = LavaViewModelFactory { ServiciosViewModel(repo) })
            LavaTheme(rol = Rol.ADMIN) {
                Scaffold(topBar = { AppTopBar("Servicios") }) { padding ->
                    ServiciosScreen(
                        vm = vm,
                        onEditar = { },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
