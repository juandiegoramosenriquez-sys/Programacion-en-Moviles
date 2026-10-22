package com.tecsup.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"

    const val INICIO = "inicio"
    const val NUEVO_PEDIDO = "nuevo_pedido?repetir={repetir}"
    const val DETALLE_PEDIDO = "detalle_pedido/{id}"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val TARIFAS = "tarifas"
    const val PERFIL = "perfil"

    const val PANEL = "panel"
    const val BANDEJA = "bandeja"
    const val GESTION_PEDIDO = "gestion_pedido/{id}"
    const val SERVICIOS = "servicios"
    const val FORMULARIO_SERVICIO = "formulario_servicio/{id}"
    const val CLIENTES = "clientes"
    const val AJUSTES = "ajustes"

    fun nuevoPedido(repetir: Int = 0) = "nuevo_pedido?repetir=$repetir"
    fun detallePedido(id: Int) = "detalle_pedido/$id"
    fun gestionPedido(id: Int) = "gestion_pedido/$id"
    fun formularioServicio(id: Int) = "formulario_servicio/$id"
}
