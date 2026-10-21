package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.model.Unidad
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioServicioScreen(
    vm: ServiciosViewModel,
    servicioId: Int,
    onListo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val servicios by vm.servicios.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    DisposableEffect(Unit) {
        onDispose { vm.limpiarMensaje() }
    }

    val original = servicios.find { it.id == servicioId }
    if (servicioId != 0 && original == null) return

    var nombre by rememberSaveable(original) { mutableStateOf(original?.nombre ?: "") }
    var unidad by rememberSaveable(original) { mutableStateOf(original?.unidad ?: Unidad.KG.name) }
    var precio by rememberSaveable(original) { mutableStateOf(original?.precio?.toString() ?: "") }
    var horas by rememberSaveable(original) { mutableStateOf(original?.horasEntrega ?: 24) }
    var descripcion by rememberSaveable(original) { mutableStateOf(original?.descripcion ?: "") }
    var icono by rememberSaveable(original) { mutableStateOf(original?.icono ?: "wash") }
    var activo by rememberSaveable(original) { mutableStateOf(original?.activo ?: true) }

    val unidades = listOf(Unidad.KG.name to "Kilo", Unidad.PRENDA.name to "Prenda", Unidad.UNIDAD.name to "Unidad")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LavaTextField(nombre, { nombre = it }, "Nombre")

        Text("Se cobra por", style = MaterialTheme.typography.titleSmall)
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            unidades.forEachIndexed { i, (valor, texto) ->
                SegmentedButton(
                    selected = unidad == valor,
                    onClick = { unidad = valor },
                    shape = SegmentedButtonDefaults.itemShape(i, unidades.size)
                ) {
                    Text(texto)
                }
            }
        }

        LavaTextField(precio, { precio = it }, "Precio (S/)", teclado = KeyboardType.Decimal)

        Text("Tiempo de entrega", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(24, 48, 72).forEach { h ->
                FilterChip(selected = horas == h, onClick = { horas = h }, label = { Text("$h h") })
            }
        }

        LavaTextField(descripcion, { descripcion = it }, "Descripción")

        Text("Ícono", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("wash" to "Lavadora", "iron" to "Plancha", "shirt" to "Camisa").forEach { (valor, texto) ->
                FilterChip(
                    selected = icono == valor,
                    onClick = { icono = valor },
                    label = { Text(texto) },
                    leadingIcon = { IconoServicio(valor, tamano = 24.dp) }
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Servicio activo", modifier = Modifier.weight(1f))
            Switch(checked = activo, onCheckedChange = { activo = it })
        }

        mensaje?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        LavaButton("Guardar", onClick = {
            vm.guardar(
                Servicio(
                    id = servicioId,
                    nombre = nombre,
                    descripcion = descripcion,
                    unidad = unidad,
                    precio = precio.replace(',', '.').toDoubleOrNull() ?: 0.0,
                    horasEntrega = horas,
                    activo = activo,
                    icono = icono
                ),
                onListo
            )
        })

        if (original != null) {
            OutlinedButton(
                onClick = { vm.eliminar(original, onListo) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar servicio")
            }
        }
    }
}
