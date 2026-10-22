package com.tecsup.lavacampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.tecsup.lavacampus.model.EstadoPedido

val Turquesa = Color(0xFF0891B2)
val TurquesaElectrico = Color(0xFF00D4F5)
val TurquesaContenedor = Color(0xFFCFFAFE)
val TurquesaFondo = Color(0xFFF0FCFF)
val TurquesaOscuro = Color(0xFF164E63)
val TurquesaSuave = Color(0xFFE0F7FC)

val Violeta = Color(0xFF6D28D9)
val VioletaElectrico = Color(0xFFB84DFF)
val VioletaContenedor = Color(0xFFEDE9FE)
val VioletaFondo = Color(0xFFFAF8FF)
val VioletaOscuro = Color(0xFF4C1D95)
val VioletaSuave = Color(0xFFF1ECFE)

val Tinta = Color(0xFF0F172A)
val TintaSuave = Color(0xFF475569)
val Borde = Color(0xFF94A3B8)

val NocheTurquesaFondo = Color(0xFF060A0B)
val NocheTurquesaTarjeta = Color(0xFF14222A)
val NocheTurquesaBorde = Color(0xFF2A3F47)
val NocheTurquesaTexto = Color(0xFFF3F8F9)
val NocheTurquesaTextoSuave = Color(0xFFB2C6CC)
val NocheTurquesaApagado = Color(0xFF7F979D)
val NocheTurquesaAcento = Color(0xFF22E3FF)
val NocheTurquesaSobreAcento = Color(0xFF021316)
val NocheTurquesaContenedor = Color(0xFF0D3640)
val NocheTurquesaSobreContenedor = Color(0xFFA5F3FC)

val NocheVioletaFondo = Color(0xFF08070B)
val NocheVioletaTarjeta = Color(0xFF1C1828)
val NocheVioletaBorde = Color(0xFF362E48)
val NocheVioletaTexto = Color(0xFFF6F4F9)
val NocheVioletaTextoSuave = Color(0xFFC1B8D0)
val NocheVioletaApagado = Color(0xFF8F86A2)
val NocheVioletaAcento = Color(0xFFC08CFF)
val NocheVioletaSobreAcento = Color(0xFF12071F)
val NocheVioletaContenedor = Color(0xFF2E1752)
val NocheVioletaSobreContenedor = Color(0xFFE2CCFF)

private val RecibidoClaro = Color(0xFFD97706)
private val EnProcesoClaro = Color(0xFF2563EB)
private val ListoClaro = Color(0xFF16A34A)
private val EntregadoClaro = Color(0xFF475569)
val CanceladoClaro = Color(0xFFDC2626)

private val RecibidoNeon = Color(0xFFFFC233)
private val EnProcesoNeon = Color(0xFF4DA3FF)
private val ListoNeon = Color(0xFF2EE58A)
private val EntregadoNeon = Color(0xFFCBD5E1)
val CanceladoNeon = Color(0xFFFF4A5E)

fun colorEstado(estado: EstadoPedido, oscuro: Boolean = false): Color = when (estado) {
    EstadoPedido.RECIBIDO -> if (oscuro) RecibidoNeon else RecibidoClaro
    EstadoPedido.EN_PROCESO -> if (oscuro) EnProcesoNeon else EnProcesoClaro
    EstadoPedido.LISTO -> if (oscuro) ListoNeon else ListoClaro
    EstadoPedido.ENTREGADO -> if (oscuro) EntregadoNeon else EntregadoClaro
    EstadoPedido.CANCELADO -> if (oscuro) CanceladoNeon else CanceladoClaro
}

@Composable
@ReadOnlyComposable
fun esModoOscuro(): Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f

fun fondoEstadoNoche(estado: EstadoPedido): Color = when (estado) {
    EstadoPedido.RECIBIDO -> Color(0xFF2A1E05)
    EstadoPedido.EN_PROCESO -> Color(0xFF0A1A33)
    EstadoPedido.LISTO -> Color(0xFF0A2817)
    EstadoPedido.ENTREGADO -> Color(0xFF1A1F26)
    EstadoPedido.CANCELADO -> Color(0xFF330A10)
}

fun textoEstadoNoche(estado: EstadoPedido): Color = when (estado) {
    EstadoPedido.RECIBIDO -> Color(0xFFFFD98A)
    EstadoPedido.EN_PROCESO -> Color(0xFFA9CCFF)
    EstadoPedido.LISTO -> Color(0xFF9BF0C2)
    EstadoPedido.ENTREGADO -> Color(0xFFCBD5E1)
    EstadoPedido.CANCELADO -> Color(0xFFFFB8C2)
}

val EstadoRecibido: Color
    @Composable @ReadOnlyComposable get() = colorEstado(EstadoPedido.RECIBIDO, esModoOscuro())

val EstadoEnProceso: Color
    @Composable @ReadOnlyComposable get() = colorEstado(EstadoPedido.EN_PROCESO, esModoOscuro())

val EstadoListo: Color
    @Composable @ReadOnlyComposable get() = colorEstado(EstadoPedido.LISTO, esModoOscuro())

val EstadoEntregado: Color
    @Composable @ReadOnlyComposable get() = colorEstado(EstadoPedido.ENTREGADO, esModoOscuro())

val EstadoCancelado: Color
    @Composable @ReadOnlyComposable get() = colorEstado(EstadoPedido.CANCELADO, esModoOscuro())
