package com.tecsup.authfirebaseramos

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

object Destinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AuthApp() {
    val navController = rememberNavController()
    AuthNavGraph(navController)
}

@Composable
fun AuthNavGraph(navController: NavHostController) {
    val inicio = if (FirebaseAuth.getInstance().currentUser != null) Destinations.HOME else Destinations.LOGIN

    NavHost(
        navController = navController,
        startDestination = inicio
    ) {
        composable(Destinations.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Destinations.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Destinations.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
