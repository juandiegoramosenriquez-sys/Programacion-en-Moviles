package com.tecsup.semanaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.semanaapp.screens.HomeScreen
import com.tecsup.semanaapp.screens.LoginScreen
import com.tecsup.semanaapp.screens.ProfileScreen
import com.tecsup.semanaapp.screens.StudentDetailScreen
import com.tecsup.semanaapp.screens.StudentListScreen
import com.tecsup.semanaapp.ui.AppLanguage
import com.tecsup.semanaapp.ui.EnglishStrings
import com.tecsup.semanaapp.ui.LocalAppStrings
import com.tecsup.semanaapp.ui.SpanishStrings

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Estado del idioma guardado con rememberSaveable
    var currentLanguage by rememberSaveable { mutableStateOf(AppLanguage.SPANISH) }
    val strings = when (currentLanguage) {
        AppLanguage.SPANISH -> SpanishStrings
        AppLanguage.ENGLISH -> EnglishStrings
    }

    // Proveedor de cadenas mediante CompositionLocal para toda la aplicación
    CompositionLocalProvider(LocalAppStrings provides strings) {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }

            composable(Screen.Home.route) {
                HomeScreen(navController)
            }

            composable(Screen.StudentList.route) {
                StudentListScreen(navController)
            }

            composable(
                route = Screen.StudentDetail.route,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->
                // Paso del argumento Int al expediente del alumno
                val studentId = backStackEntry.arguments?.getInt("id") ?: 0
                StudentDetailScreen(navController, studentId)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    navController = navController,
                    currentLanguage = currentLanguage,
                    onLanguageChanged = { newLanguage ->
                        currentLanguage = newLanguage
                    }
                )
            }
        }
    }
}
