package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.ui.components.formatoSoles
import com.tecsup.lavacampus.ui.components.graficos.GraficoBarras
import com.tecsup.lavacampus.ui.components.graficos.GraficoDona
import com.tecsup.lavacampus.ui.theme.EstadoEnProceso
import com.tecsup.lavacampus.ui.theme.EstadoListo
import com.tecsup.lavacampus.ui.theme.EstadoRecibido
import com.tecsup.lavacampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerListos: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pedidosHoy by vm.pedidosHoy.collectAsState()
    val enProceso by vm.enProceso.collectAsState()
    val porRecoger by vm.porRecoger.collectAsState()
    val ingresos by vm.ingresosSemana.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Contador("Hoy", pedidosHoy, EstadoRecibido, Modifier.weight(1f))
            Contador("En proceso", enProceso, EstadoEnProceso, Modifier.weight(1f))
            Contador("Por recoger", porRecoger, EstadoListo, Modifier.weight(1f))
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Ingresos de la semana", style = MaterialTheme.typography.bodyMedium)
                Text(formatoSoles(ingresos), style = MaterialTheme.typography.headlineSmall)
                Text("Pedidos pagados desde el lunes", style = MaterialTheme.typography.bodySmall)
            }
        }

        if (porRecoger > 0) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EstadoListo.copy(alpha = 0.12f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Hay $porRecoger pedidos listos esperando recojo",
                        color = EstadoListo,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = onVerListos,
                        colors = ButtonDefaults.buttonColors(containerColor = EstadoListo)
                    ) {
                        Text("Ver")
                    }
                }
            }
        }

        Seccion("Pedidos por estado") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                GraficoDona(porEstado)
                Spacer(Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    porEstado.forEach { d ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(10.dp).background(d.color, CircleShape))
                            Spacer(Modifier.width(8.dp))
                            Text("${d.etiqueta}: ${d.valor.toInt()}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        Seccion("Ingresos por servicio (semana)") {
            if (porServicio.isEmpty()) {
                Text("Aún no hay ingresos esta semana", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                GraficoBarras(porServicio)
                Spacer(Modifier.height(8.dp))
                porServicio.forEach { d ->
                    Row(Modifier.fillMaxWidth()) {
                        Text(d.etiqueta, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        Text(formatoSoles(d.valor), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, color: Color, modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        modifier = modifier
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("$valor", style = MaterialTheme.typography.headlineSmall, color = color)
            Text(titulo, style = MaterialTheme.typography.bodySmall, color = color)
        }
    }
}

@Composable
private fun Seccion(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            contenido()
        }
    }
}
