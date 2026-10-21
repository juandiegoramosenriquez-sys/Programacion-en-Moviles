package com.tecsup.lavacampus.ui.theme

import androidx.compose.ui.graphics.Color
import com.tecsup.lavacampus.model.EstadoPedido

val Turquesa = Color(0xFF0891B2)
val TurquesaContenedor = Color(0xFFCFFAFE)
val TurquesaFondo = Color(0xFFF0FCFF)
val TurquesaOscuro = Color(0xFF164E63)

val Violeta = Color(0xFF6D28D9)
val VioletaContenedor = Color(0xFFEDE9FE)
val VioletaFondo = Color(0xFFFAF8FF)
val VioletaOscuro = Color(0xFF4C1D95)

val FondoOscuro = Color(0xFF0F172A)
val SuperficieOscura = Color(0xFF1E293B)

val EstadoRecibido = Color(0xFFD97706)
val EstadoEnProceso = Color(0xFF2563EB)
val EstadoListo = Color(0xFF16A34A)
val EstadoEntregado = Color(0xFF475569)
val EstadoCancelado = Color(0xFFDC2626)

fun colorEstado(estado: EstadoPedido): Color = when (estado) {
    EstadoPedido.RECIBIDO -> EstadoRecibido
    EstadoPedido.EN_PROCESO -> EstadoEnProceso
    EstadoPedido.LISTO -> EstadoListo
    EstadoPedido.ENTREGADO -> EstadoEntregado
    EstadoPedido.CANCELADO -> EstadoCancelado
}
