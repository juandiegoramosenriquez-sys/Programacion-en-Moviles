package com.tecsup.catalogo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.catalogo.AppContainer
import com.tecsup.catalogo.ui.detalle.DetalleScreen
import com.tecsup.catalogo.ui.detalle.DetalleViewModel
import com.tecsup.catalogo.ui.favoritos.FavoritosScreen
import com.tecsup.catalogo.ui.favoritos.FavoritosViewModel
import com.tecsup.catalogo.ui.login.LoginScreen
import com.tecsup.catalogo.ui.login.LoginViewModel
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.lista.ListaViewModel

@Composable
fun AppNav(container: AppContainer) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.factory(container))

    NavHost(navController = navController, startDestination = "login") {

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
                onItemClick = { id -> navController.navigate("detalle/$id") },
                onVerFavoritos = { navController.navigate("favoritos") },
                onCerrarSesion = {
                    loginViewModel.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable("favoritos") {
            val vm: FavoritosViewModel = viewModel(factory = FavoritosViewModel.factory(container))
            FavoritosScreen(
                viewModel = vm,
                onItemClick = { id -> navController.navigate("detalle/$id") },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: DetalleViewModel = viewModel(factory = DetalleViewModel.factory(container, id))
            DetalleScreen(
                viewModel = vm,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
