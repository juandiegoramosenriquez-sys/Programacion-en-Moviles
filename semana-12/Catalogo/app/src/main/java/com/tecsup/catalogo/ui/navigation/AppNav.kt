package com.tecsup.catalogo.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.catalogo.AppContainer
import com.tecsup.catalogo.R
import com.tecsup.catalogo.domain.model.Rol
import com.tecsup.catalogo.domain.model.Sesion
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.admin.AdminScreen
import com.tecsup.catalogo.ui.admin.AdminViewModel
import com.tecsup.catalogo.ui.detalle.DetalleScreen
import com.tecsup.catalogo.ui.detalle.DetalleViewModel
import com.tecsup.catalogo.ui.favoritos.FavoritosScreen
import com.tecsup.catalogo.ui.favoritos.FavoritosViewModel
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.lista.ListaViewModel
import com.tecsup.catalogo.ui.login.LoginScreen
import com.tecsup.catalogo.ui.login.LoginViewModel
import com.tecsup.catalogo.ui.perfil.PerfilScreen
import com.tecsup.catalogo.ui.perfil.PerfilViewModel

data class Pestana(val ruta: String, @StringRes val titulo: Int, val icono: ImageVector)

@Composable
fun AppNav(container: AppContainer) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.factory(container))
    val estadoLogin by loginViewModel.estado.collectAsStateWithLifecycle()
    val sesion = (estadoLogin as? UiState.Exito<Sesion?>)?.datos
    val esAdmin = sesion?.rol == Rol.ADMIN

    val pestanas = buildList {
        add(Pestana("lista", R.string.nav_inicio, Icons.Outlined.Home))
        add(Pestana("favoritos", R.string.nav_favoritos, Icons.Outlined.FavoriteBorder))
        add(Pestana("perfil", R.string.nav_perfil, Icons.Outlined.Person))
        if (esAdmin) add(Pestana("admin", R.string.nav_admin, Icons.Outlined.AdminPanelSettings))
    }
    val entrada by navController.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (pestanas.any { it.ruta == rutaActual }) {
                BarraInferior(pestanas, rutaActual) { ruta -> irAPestana(navController, ruta) }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {

            composable("login") {
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginExitoso = {
                        navController.navigate("lista") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            composable("lista") {
                val vm: ListaViewModel = viewModel(factory = ListaViewModel.factory(container))
                ListaScreen(
                    viewModel = vm,
                    nombre = stringResource(if (esAdmin) R.string.rol_admin else R.string.rol_usuario),
                    onItemClick = { id -> navController.navigate("detalle/$id") }
                )
            }

            composable("favoritos") {
                val vm: FavoritosViewModel = viewModel(factory = FavoritosViewModel.factory(container))
                FavoritosScreen(
                    viewModel = vm,
                    onItemClick = { id -> navController.navigate("detalle/$id") },
                    onExplorar = { irAPestana(navController, "lista") }
                )
            }

            composable("perfil") {
                val vm: PerfilViewModel = viewModel(factory = PerfilViewModel.factory(container))
                PerfilScreen(
                    viewModel = vm,
                    sesion = sesion,
                    onVerFavoritos = { irAPestana(navController, "favoritos") },
                    onCerrarSesion = {
                        loginViewModel.cerrarSesion()
                        navController.navigate("login") {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }

            composable("admin") {
                if (esAdmin) {
                    val vm: AdminViewModel = viewModel(factory = AdminViewModel.factory(container))
                    AdminScreen(viewModel = vm)
                }
            }

            composable(
                route = "detalle/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entradaDetalle ->
                val id = entradaDetalle.arguments?.getInt("id") ?: 0
                val vm: DetalleViewModel = viewModel(factory = DetalleViewModel.factory(container, id))
                DetalleScreen(
                    viewModel = vm,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
    }
}

private fun irAPestana(navController: NavHostController, ruta: String) {
    navController.navigate(ruta) {
        popUpTo("lista") { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun BarraInferior(pestanas: List<Pestana>, rutaActual: String?, onSeleccionar: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        pestanas.forEach { pestana ->
            NavigationBarItem(
                selected = rutaActual == pestana.ruta,
                onClick = { onSeleccionar(pestana.ruta) },
                icon = { Icon(pestana.icono, contentDescription = null) },
                label = { Text(stringResource(pestana.titulo)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
