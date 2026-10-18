package com.tecsup.lavacampus.model

enum class EstadoPedido(val etiqueta: String) {
    RECIBIDO("Recibido"),
    EN_PROCESO("En proceso"),
    LISTO("Listo"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado")
}
