package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.formatoSoles
import com.tecsup.lavacampus.ui.components.graficos.GraficoBarras
import com.tecsup.lavacampus.ui.components.graficos.GraficoDona
import com.tecsup.lavacampus.ui.theme.EstadoCancelado
import com.tecsup.lavacampus.ui.theme.EstadoListo
import com.tecsup.lavacampus.ui.theme.EstadoRecibido
import com.tecsup.lavacampus.ui.theme.colorEstado
import com.tecsup.lavacampus.ui.theme.degradadoElectrico
import com.tecsup.lavacampus.ui.theme.esModoOscuro
import com.tecsup.lavacampus.ui.theme.textoSobreDestacado
import com.tecsup.lavacampus.viewmodel.PanelViewModel
import java.util.Locale

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerPedidos: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pedidosHoy by vm.pedidosHoy.collectAsState()
    val enProceso by vm.enProceso.collectAsState()
    val porRecoger by vm.porRecoger.collectAsState()
    val ingresos by vm.ingresosSemana.collectAsState()
    val porEstadoBase by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()
    val atrasados by vm.atrasados.collectAsState()
    val promedio by vm.promedioCalificacion.collectAsState()
    val calificados by vm.cantidadCalificados.collectAsState()
    val oscuro = esModoOscuro()
    val porEstado = porEstadoBase.map { d ->
        EstadoPedido.entries.find { it.etiqueta == d.etiqueta }
            ?.let { d.copy(color = colorEstado(it, oscuro)) } ?: d
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ResumenSemana(ingresos, pedidosHoy, enProceso, porRecoger)

        if (porRecoger > 0) {
            Aviso(
                icono = Icons.Default.Inventory2,
                texto = if (porRecoger == 1) "1 pedido listo espera recojo" else "$porRecoger pedidos listos esperan recojo",
                color = EstadoListo,
                onClick = onVerPedidos
            )
        }
        if (atrasados > 0) {
            Aviso(
                icono = Icons.Default.Schedule,
                texto = if (atrasados == 1) "1 pedido atrasado" else "$atrasados pedidos atrasados",
                color = EstadoCancelado,
                onClick = onVerPedidos
            )
        }

        Seccion(Icons.Default.Star, "Calificación de los clientes") {
            if (calificados == 0) {
                Text(
                    "Aún no hay pedidos calificados",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(String.format(Locale.US, "%.1f", promedio), style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Row {
                            (1..5).forEach { n ->
                                Icon(
                                    if (n <= promedio + 0.5) Icons.Default.Star else Icons.Outlined.StarOutline,
                                    contentDescription = null,
                                    tint = EstadoRecibido,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Text(
                            if (calificados == 1) "1 pedido calificado" else "$calificados pedidos calificados",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Seccion(Icons.Default.DonutLarge, "Pedidos por estado") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                GraficoDona(porEstado, tamano = 120.dp, grosor = 18.dp)
                Spacer(Modifier.width(20.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    porEstado.forEach { d ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(12.dp).background(d.color, CircleShape))
                            Spacer(Modifier.width(10.dp))
                            Text(d.etiqueta, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(84.dp))
                            Text("${d.valor.toInt()}", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }
        }

        Seccion(Icons.Default.BarChart, "Ingresos por servicio") {
            if (porServicio.isEmpty()) {
                Text(
                    "Aún no hay ingresos esta semana",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                GraficoBarras(porServicio)
                Spacer(Modifier.height(12.dp))
                porServicio.forEachIndexed { i, d ->
                    if (i > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(d.etiqueta, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        Text(formatoSoles(d.valor), style = MaterialTheme.typography.titleSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumenSemana(ingresos: Double, hoy: Int, enProceso: Int, porRecoger: Int) {
    val texto = textoSobreDestacado()
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent, contentColor = texto),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(degradadoElectrico())
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Ingresos de la semana", style = MaterialTheme.typography.bodyMedium, color = texto.copy(alpha = 0.85f))
                    Text(formatoSoles(ingresos), style = MaterialTheme.typography.headlineMedium)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(48.dp).background(texto.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(Icons.Default.Payments, contentDescription = null)
                }
            }
            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatoResumen("Hoy", hoy, Modifier.weight(1f))
                DatoResumen("En proceso", enProceso, Modifier.weight(1f))
                DatoResumen("Por recoger", porRecoger, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DatoResumen(titulo: String, valor: Int, modifier: Modifier) {
    val texto = textoSobreDestacado()
    Column(
        modifier
            .background(texto.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text("$valor", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(titulo, style = MaterialTheme.typography.labelMedium, color = texto.copy(alpha = 0.85f))
    }
}

@Composable
private fun Aviso(icono: ImageVector, texto: String, color: Color, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Icon(icono, contentDescription = null, tint = color)
        Spacer(Modifier.width(12.dp))
        Text(texto, color = color, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Ver", tint = color)
    }
}

@Composable
private fun Seccion(icono: ImageVector, titulo: String, contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(titulo, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(14.dp))
            contenido()
        }
    }
}
