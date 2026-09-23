package com.tecsup.semanaapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object StudentList : Screen("student_list")
    object StudentDetail : Screen("student_detail/{id}") {
        fun createRoute(id: Int): String = "student_detail/$id"
    }
    object Profile : Screen("profile")
}
