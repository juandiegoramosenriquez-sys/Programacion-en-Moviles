package com.DiegoRamos.semana06.navigaton

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object Pedidos : Screen("pedidos")
    object Favoritos : Screen("favoritos")
    object Perfil : Screen("perfil")
}