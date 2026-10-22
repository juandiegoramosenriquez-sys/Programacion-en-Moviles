package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.model.Unidad
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.formatoSoles
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel

fun Servicio.textoPrecio(): String {
    val unidadTexto = when (unidad) {
        Unidad.KG.name -> "por kg"
        Unidad.PRENDA.name -> "por prenda"
        else -> "por unidad"
    }
    return "${formatoSoles(precio)} $unidadTexto"
}

@Composable
fun TarifasScreen(vm: ServiciosViewModel, limite: Int, modifier: Modifier = Modifier) {
    val servicios by vm.activos.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(servicios, key = { it.id }) { s ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconoServicio(s.icono)
                        Spacer(Modifier.width(12.dp))
                        Text(s.nombre, style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        s.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Entrega en ${s.horasEntrega} h",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            s.textoPrecio(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Horario de atención", style = MaterialTheme.typography.titleMedium)
                    Text("Lunes a sábado de 8:00 a 20:00. Los domingos no atendemos.")
                    Spacer(Modifier.height(12.dp))
                    Text("Reglas", style = MaterialTheme.typography.titleMedium)
                    Text("• Mínimo 1 kg en lavado por kilo")
                    Text(if (limite == 1) "• Máximo 1 pedido activo por estudiante" else "• Máximo $limite pedidos activos por estudiante")
                    Text("• Se paga al recoger en el casillero")
                }
            }
        }
    }
}
