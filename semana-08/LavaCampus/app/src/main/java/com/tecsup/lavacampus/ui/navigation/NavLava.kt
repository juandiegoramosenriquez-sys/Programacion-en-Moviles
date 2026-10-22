package com.tecsup.lavacampus.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.administrador.AjustesScreen
import com.tecsup.lavacampus.ui.administrador.BandejaScreen
import com.tecsup.lavacampus.ui.administrador.ClientesScreen
import com.tecsup.lavacampus.ui.administrador.FormularioServicioScreen
import com.tecsup.lavacampus.ui.administrador.GestionPedidoScreen
import com.tecsup.lavacampus.ui.administrador.PanelScreen
import com.tecsup.lavacampus.ui.administrador.ServiciosScreen
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.estudiante.DetallePedidoScreen
import com.tecsup.lavacampus.ui.estudiante.InicioScreen
import com.tecsup.lavacampus.ui.estudiante.LoginScreen
import com.tecsup.lavacampus.ui.estudiante.MisPedidosScreen
import com.tecsup.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.tecsup.lavacampus.ui.estudiante.PerfilScreen
import com.tecsup.lavacampus.ui.estudiante.ReciboScreen
import com.tecsup.lavacampus.ui.estudiante.RegistroScreen
import com.tecsup.lavacampus.ui.estudiante.SplashScreen
import com.tecsup.lavacampus.ui.estudiante.TarifasScreen
import com.tecsup.lavacampus.ui.theme.LavaTheme
import com.tecsup.lavacampus.viewmodel.BandejaViewModel
import com.tecsup.lavacampus.viewmodel.ClientesViewModel
import com.tecsup.lavacampus.viewmodel.DetallePedidoViewModel
import com.tecsup.lavacampus.viewmodel.GestionPedidoViewModel
import com.tecsup.lavacampus.viewmodel.InicioViewModel
import com.tecsup.lavacampus.viewmodel.LavaViewModelFactory
import com.tecsup.lavacampus.viewmodel.MisPedidosViewModel
import com.tecsup.lavacampus.viewmodel.NuevoPedidoViewModel
import com.tecsup.lavacampus.viewmodel.PanelViewModel
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel
import com.tecsup.lavacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.launch

private data class ItemBarra(val ruta: String, val texto: String, val icono: ImageVector)

private val itemsEstudiante = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Default.Home),
    ItemBarra(Rutas.MIS_PEDIDOS, "Pedidos", Icons.Default.Receipt),
    ItemBarra(Rutas.TARIFAS, "Tarifas", Icons.Default.Sell),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)

private val itemsAdmin = listOf(
    ItemBarra(Rutas.PANEL, "Panel", Icons.Default.Dashboard),
    ItemBarra(Rutas.BANDEJA, "Pedidos", Icons.Default.Inbox),
    ItemBarra(Rutas.SERVICIOS, "Servicios", Icons.Default.LocalLaundryService),
    ItemBarra(Rutas.CLIENTES, "Clientes", Icons.Default.Groups),
    ItemBarra(Rutas.AJUSTES, "Ajustes", Icons.Default.Settings)
)

@Composable
fun NavLava(repo: LavaRepository, prefs: PreferenciasStore) {
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()
    val sesion: SesionViewModel = viewModel(factory = LavaViewModelFactory { SesionViewModel(repo, prefs) })
    val oscuro by sesion.modoOscuro.collectAsState()

    val cerrarSesion: () -> Unit = {
        sesion.cerrarSesion()
        nav.navigate(Rutas.LOGIN) {
            popUpTo(nav.graph.id) { inclusive = true }
        }
    }

    val cambiarPerfil: () -> Unit = {
        sesion.cambiarPerfil()
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
                    nav.navigate(destino) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            })
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                sesion,
                onIngresar = { usuario ->
                    nav.navigate(principalDe(usuario.rol)) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onRegistrarse = { nav.navigate(Rutas.REGISTRO) }
            )
        }

        composable(Rutas.REGISTRO) {
            Pantalla(nav, Rol.CLIENTE, oscuro, "Crear cuenta", conBarra = false) { modifier ->
                RegistroScreen(
                    vm = sesion,
                    onRegistrado = {
                        nav.navigate(Rutas.INICIO) {
                            popUpTo(nav.graph.id) { inclusive = true }
                        }
                    },
                    modifier = modifier
                )
            }
        }

        composable(Rutas.INICIO) {
            val usuario = sesion.usuarioActual.collectAsState().value ?: return@composable
            val vm: InicioViewModel = viewModel(factory = LavaViewModelFactory { InicioViewModel(repo, usuario.id) })
            Pantalla(nav, Rol.CLIENTE, oscuro, "Inicio", conBarra = true) { modifier ->
                InicioScreen(
                    usuario = usuario,
                    vm = vm,
                    onNuevoPedido = { nav.navigate(Rutas.nuevoPedido()) },
                    onMisPedidos = { irPestana(nav, Rutas.MIS_PEDIDOS, Rutas.INICIO) },
                    onTarifas = { irPestana(nav, Rutas.TARIFAS, Rutas.INICIO) },
                    onPedido = { nav.navigate(Rutas.detallePedido(it)) },
                    modifier = modifier
                )
            }
        }

        composable(
            Rutas.NUEVO_PEDIDO,
            arguments = listOf(navArgument("repetir") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) { entrada ->
            val usuario = sesion.usuarioActual.collectAsState().value ?: return@composable
            val repetir = entrada.arguments?.getInt("repetir") ?: 0
            val vm: NuevoPedidoViewModel = viewModel(factory = LavaViewModelFactory { NuevoPedidoViewModel(repo, usuario.id, prefs, repetir) })
            val titulo = if (repetir == 0) "Nuevo pedido" else "Pedir de nuevo"
            Pantalla(nav, Rol.CLIENTE, oscuro, titulo, conBarra = false) { modifier ->
                NuevoPedidoScreen(
                    vm = vm,
                    onCreado = {
                        nav.navigate(Rutas.MIS_PEDIDOS) {
                            popUpTo(Rutas.INICIO)
                            launchSingleTop = true
                        }
                    },
                    modifier = modifier
                )
            }
        }

        composable(
            Rutas.DETALLE_PEDIDO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: DetallePedidoViewModel = viewModel(factory = LavaViewModelFactory { DetallePedidoViewModel(repo, id) })
            Pantalla(nav, Rol.CLIENTE, oscuro, "Detalle del pedido", conBarra = false) { modifier ->
                DetallePedidoScreen(
                    vm = vm,
                    onVerRecibo = { nav.navigate(Rutas.recibo(id)) },
                    modifier = modifier
                )
            }
        }

        composable(
            Rutas.RECIBO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: DetallePedidoViewModel = viewModel(factory = LavaViewModelFactory { DetallePedidoViewModel(repo, id) })
            Pantalla(nav, Rol.CLIENTE, oscuro, "Recibo", conBarra = false) { modifier ->
                ReciboScreen(vm = vm, modifier = modifier)
            }
        }

        composable(Rutas.MIS_PEDIDOS) {
            val usuario = sesion.usuarioActual.collectAsState().value ?: return@composable
            val vm: MisPedidosViewModel = viewModel(factory = LavaViewModelFactory { MisPedidosViewModel(repo, usuario.id) })
            Pantalla(nav, Rol.CLIENTE, oscuro, "Mis pedidos", conBarra = true) { modifier ->
                MisPedidosScreen(
                    vm = vm,
                    onPedido = { nav.navigate(Rutas.detallePedido(it)) },
                    onNuevoPedido = { nav.navigate(Rutas.nuevoPedido()) },
                    onRepetir = { nav.navigate(Rutas.nuevoPedido(it)) },
                    modifier = modifier
                )
            }
        }

        composable(Rutas.TARIFAS) {
            val vm: ServiciosViewModel = viewModel(factory = LavaViewModelFactory { ServiciosViewModel(repo) })
            Pantalla(nav, Rol.CLIENTE, oscuro, "Tarifas", conBarra = true) { modifier ->
                val limite by sesion.limitePedidos.collectAsState()
                TarifasScreen(vm = vm, limite = limite, modifier = modifier)
            }
        }

        composable(Rutas.PERFIL) {
            val usuario = sesion.usuarioActual.collectAsState().value ?: return@composable
            val vm: MisPedidosViewModel = viewModel(factory = LavaViewModelFactory { MisPedidosViewModel(repo, usuario.id) })
            Pantalla(nav, Rol.CLIENTE, oscuro, "Perfil", conBarra = true) { modifier ->
                PerfilScreen(
                    usuario = usuario,
                    pedidosVm = vm,
                    sesion = sesion,
                    onCambiarPerfil = cambiarPerfil,
                    onCerrarSesion = cerrarSesion,
                    modifier = modifier
                )
            }
        }

        composable(Rutas.PANEL) {
            val vm: PanelViewModel = viewModel(factory = LavaViewModelFactory { PanelViewModel(repo) })
            Pantalla(nav, Rol.ADMIN, oscuro, "Panel", conBarra = true) { modifier ->
                PanelScreen(
                    vm = vm,
                    onVerPedidos = { irPestana(nav, Rutas.BANDEJA, Rutas.PANEL) },
                    modifier = modifier
                )
            }
        }

        composable(Rutas.BANDEJA) {
            val vm: BandejaViewModel = viewModel(factory = LavaViewModelFactory { BandejaViewModel(repo) })
            Pantalla(nav, Rol.ADMIN, oscuro, "Pedidos", conBarra = true) { modifier ->
                BandejaScreen(
                    vm = vm,
                    onAbrir = { nav.navigate(Rutas.gestionPedido(it)) },
                    modifier = modifier
                )
            }
        }

        composable(
            Rutas.GESTION_PEDIDO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: GestionPedidoViewModel = viewModel(factory = LavaViewModelFactory { GestionPedidoViewModel(repo, id) })
            Pantalla(nav, Rol.ADMIN, oscuro, "Gestionar pedido", conBarra = false) { modifier ->
                GestionPedidoScreen(vm = vm, modifier = modifier)
            }
        }

        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = LavaViewModelFactory { ServiciosViewModel(repo) })
            Pantalla(
                nav, Rol.ADMIN, oscuro, "Servicios", conBarra = true,
                botonFlotante = {
                    FloatingActionButton(
                        onClick = { nav.navigate(Rutas.formularioServicio(0)) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo servicio")
                    }
                }
            ) { modifier ->
                ServiciosScreen(
                    vm = vm,
                    onEditar = { nav.navigate(Rutas.formularioServicio(it)) },
                    modifier = modifier
                )
            }
        }

        composable(
            Rutas.FORMULARIO_SERVICIO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: ServiciosViewModel = viewModel(factory = LavaViewModelFactory { ServiciosViewModel(repo) })
            val titulo = if (id == 0) "Nuevo servicio" else "Editar servicio"
            Pantalla(nav, Rol.ADMIN, oscuro, titulo, conBarra = false) { modifier ->
                FormularioServicioScreen(
                    vm = vm,
                    servicioId = id,
                    onListo = { nav.popBackStack() },
                    modifier = modifier
                )
            }
        }

        composable(Rutas.CLIENTES) {
            val vm: ClientesViewModel = viewModel(factory = LavaViewModelFactory { ClientesViewModel(repo) })
            Pantalla(nav, Rol.ADMIN, oscuro, "Clientes", conBarra = true) { modifier ->
                ClientesScreen(vm = vm, modifier = modifier)
            }
        }

        composable(Rutas.AJUSTES) {
            val usuario = sesion.usuarioActual.collectAsState().value ?: return@composable
            Pantalla(nav, Rol.ADMIN, oscuro, "Ajustes", conBarra = true) { modifier ->
                AjustesScreen(
                    usuario = usuario,
                    sesion = sesion,
                    onCambiarPerfil = cambiarPerfil,
                    onCerrarSesion = cerrarSesion,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun Pantalla(
    nav: NavHostController,
    rol: Rol,
    oscuro: Boolean,
    titulo: String,
    conBarra: Boolean,
    botonFlotante: @Composable () -> Unit = {},
    contenido: @Composable (Modifier) -> Unit
) {
    val atras: (() -> Unit)? = if (conBarra) null else { { nav.popBackStack() } }
    LavaTheme(rol = rol, oscuro = oscuro) {
        Scaffold(
            topBar = { AppTopBar(titulo, onAtras = atras) },
            bottomBar = { if (conBarra) BarraInferior(nav, rol) },
            floatingActionButton = botonFlotante
        ) { padding ->
            contenido(Modifier.padding(padding))
        }
    }
}

@Composable
private fun BarraInferior(nav: NavHostController, rol: Rol) {
    val ruta = nav.currentBackStackEntryAsState().value?.destination?.route
    val items = if (rol == Rol.ADMIN) itemsAdmin else itemsEstudiante
    val raiz = if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = ruta == item.ruta,
                onClick = { irPestana(nav, item.ruta, raiz) },
                icon = { Icon(item.icono, contentDescription = item.texto) },
                label = { Text(item.texto) }
            )
        }
    }
}

private fun irPestana(nav: NavHostController, ruta: String, raiz: String) {
    if (ruta == raiz) {
        nav.popBackStack(raiz, inclusive = false)
        return
    }
    nav.navigate(ruta) {
        popUpTo(raiz)
        launchSingleTop = true
    }
}

private fun principalDe(rol: String): String =
    if (rol == Rol.ADMIN.name) Rutas.PANEL else Rutas.INICIO
