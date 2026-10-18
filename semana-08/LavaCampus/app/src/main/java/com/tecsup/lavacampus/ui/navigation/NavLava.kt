package com.tecsup.lavacampus.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.administrador.ServiciosScreen
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.estudiante.LoginScreen
import com.tecsup.lavacampus.ui.estudiante.SplashScreen
import com.tecsup.lavacampus.ui.theme.LavaTheme
import com.tecsup.lavacampus.viewmodel.LavaViewModelFactory
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel
import com.tecsup.lavacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.launch

@Composable
fun NavLava(repo: LavaRepository, prefs: PreferenciasStore) {
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()
    val sesion: SesionViewModel = viewModel(factory = LavaViewModelFactory { SesionViewModel(repo, prefs) })
    val oscuro by sesion.modoOscuro.collectAsState()

    val cerrarSesion = {
        sesion.cerrarSesion()
        nav.navigate(Rutas.LOGIN) {
            popUpTo(nav.graph.id) { inclusive = true }
        }
    }

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(onTerminar = {
                scope.launch {
                    val usuario = sesion.restaurarSesion()
                    val destino = if (usuario == null) Rutas.LOGIN else principalDe(usuario.rol)
                    irA(nav, destino, Rutas.SPLASH)
                }
            })
        }

        composable(Rutas.LOGIN) {
            LoginScreen(sesion, onIngresar = { usuario ->
                irA(nav, principalDe(usuario.rol), Rutas.LOGIN)
            })
        }

        composable(Rutas.INICIO) {
            val usuario by sesion.usuarioActual.collectAsState()
            LavaTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                Scaffold(topBar = {
                    AppTopBar("Inicio", acciones = {
                        IconButton(onClick = cerrarSesion) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                        }
                    })
                }) { padding ->
                    Text(
                        "Hola, ${usuario?.nombre.orEmpty()}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(padding).padding(16.dp)
                    )
                }
            }
        }

        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = LavaViewModelFactory { ServiciosViewModel(repo) })
            LavaTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                Scaffold(topBar = {
                    AppTopBar("Servicios", acciones = {
                        IconButton(onClick = cerrarSesion) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                        }
                    })
                }) { padding ->
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

private fun principalDe(rol: String): String =
    if (rol == Rol.ADMIN.name) Rutas.SERVICIOS else Rutas.INICIO

private fun irA(nav: NavHostController, destino: String, desde: String) {
    nav.navigate(destino) {
        popUpTo(desde) { inclusive = true }
    }
}
