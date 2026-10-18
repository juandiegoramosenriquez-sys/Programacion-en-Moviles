package com.tecsup.lavacampus.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.administrador.ServiciosScreen
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.estudiante.DetallePedidoScreen
import com.tecsup.lavacampus.ui.estudiante.InicioScreen
import com.tecsup.lavacampus.ui.estudiante.LoginScreen
import com.tecsup.lavacampus.ui.estudiante.MisPedidosScreen
import com.tecsup.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.tecsup.lavacampus.ui.estudiante.SplashScreen
import com.tecsup.lavacampus.ui.estudiante.TarifasScreen
import com.tecsup.lavacampus.ui.theme.LavaTheme
import com.tecsup.lavacampus.viewmodel.DetallePedidoViewModel
import com.tecsup.lavacampus.viewmodel.InicioViewModel
import com.tecsup.lavacampus.viewmodel.LavaViewModelFactory
import com.tecsup.lavacampus.viewmodel.MisPedidosViewModel
import com.tecsup.lavacampus.viewmodel.NuevoPedidoViewModel
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
            val usuario = sesion.usuarioActual.collectAsState().value ?: return@composable
            val vm: InicioViewModel = viewModel(factory = LavaViewModelFactory { InicioViewModel(repo, usuario.id) })
            LavaTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                Scaffold(topBar = {
                    AppTopBar("Inicio", acciones = {
                        IconButton(onClick = cerrarSesion) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                        }
                    })
                }) { padding ->
                    InicioScreen(
                        usuario = usuario,
                        vm = vm,
                        onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
                        onMisPedidos = { nav.navigate(Rutas.MIS_PEDIDOS) },
                        onTarifas = { nav.navigate(Rutas.TARIFAS) },
                        onPedido = { nav.navigate(Rutas.detallePedido(it)) },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        composable(Rutas.NUEVO_PEDIDO) {
            val usuario = sesion.usuarioActual.collectAsState().value ?: return@composable
            val vm: NuevoPedidoViewModel = viewModel(factory = LavaViewModelFactory { NuevoPedidoViewModel(repo, usuario.id) })
            LavaTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                Scaffold(topBar = {
                    AppTopBar("Nuevo pedido", onAtras = { nav.popBackStack() })
                }) { padding ->
                    NuevoPedidoScreen(
                        vm = vm,
                        onCreado = {
                            nav.navigate(Rutas.MIS_PEDIDOS) {
                                popUpTo(Rutas.INICIO)
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        composable(Rutas.MIS_PEDIDOS) {
            val usuario = sesion.usuarioActual.collectAsState().value ?: return@composable
            val vm: MisPedidosViewModel = viewModel(factory = LavaViewModelFactory { MisPedidosViewModel(repo, usuario.id) })
            LavaTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                Scaffold(topBar = {
                    AppTopBar("Mis pedidos", onAtras = { nav.popBackStack() })
                }) { padding ->
                    MisPedidosScreen(
                        vm = vm,
                        onPedido = { nav.navigate(Rutas.detallePedido(it)) },
                        onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        composable(Rutas.TARIFAS) {
            val vm: ServiciosViewModel = viewModel(factory = LavaViewModelFactory { ServiciosViewModel(repo) })
            LavaTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                Scaffold(topBar = {
                    AppTopBar("Tarifas", onAtras = { nav.popBackStack() })
                }) { padding ->
                    TarifasScreen(vm = vm, modifier = Modifier.padding(padding))
                }
            }
        }

        composable(
            Rutas.DETALLE_PEDIDO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: DetallePedidoViewModel = viewModel(factory = LavaViewModelFactory { DetallePedidoViewModel(repo, id) })
            LavaTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                Scaffold(topBar = {
                    AppTopBar("Detalle del pedido", onAtras = { nav.popBackStack() })
                }) { padding ->
                    DetallePedidoScreen(vm = vm, modifier = Modifier.padding(padding))
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
