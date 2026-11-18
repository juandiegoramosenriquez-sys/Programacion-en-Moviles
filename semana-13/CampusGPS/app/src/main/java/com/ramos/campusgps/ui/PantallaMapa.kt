package com.ramos.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ramos.campusgps.model.UbicacionUi
import com.ramos.campusgps.util.formatearDistancia
import com.ramos.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(vm: MapaViewModel, oscuro: Boolean) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugaresVisibles.collectAsState()
    val categoria by vm.categoria.collectAsState()
    val radio by vm.radio.collectAsState()
    val dentroDelRadio by vm.dentroDelRadio.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val siguiendo by vm.siguiendo.collectAsState()
    val ultimoVisitado by vm.ultimoVisitado.collectAsState()
    val contexto = LocalContext.current

    fun tienePermiso(): Boolean {
        val fine = ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    var permisoConcedido by remember { mutableStateOf(tienePermiso()) }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        permisoConcedido = resultado.values.any { it }
        if (permisoConcedido) vm.buscar() else vm.permisoDenegado()
    }

    fun alPulsar() {
        permisoConcedido = tienePermiso()
        if (permisoConcedido) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val distanciaCercano = masCercano?.let { lugar ->
        lugares.firstOrNull { it.lugar.id == lugar.id }?.metros?.let { formatearDistancia(it) }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
            CabeceraPanel(
                masCercano = distanciaCercano ?: "—",
                enRadio = "${lugares.count { it.lugar.id in dentroDelRadio }} de ${lugares.size}",
                ultimo = ultimoVisitado?.nombre ?: "—",
                siguiendo = siguiendo,
                radios = vm.radios,
                radio = radio,
                onRadio = { vm.elegirRadio(it) },
                oscuro = oscuro,
                onCambiarTema = { vm.cambiarTema(oscuro) }
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FiltroCategorias(
                    categorias = vm.categorias,
                    seleccionada = categoria,
                    onElegir = { vm.filtrar(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                MapaUbicacion(
                    estado = estado,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercano = masCercano,
                    radio = radio,
                    permisoConcedido = permisoConcedido,
                    modoOscuro = oscuro,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline), MaterialTheme.shapes.medium)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { alPulsar() },
                        enabled = estado !is UbicacionUi.Buscando && !siguiendo,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.weight(1.6f).height(48.dp)
                    ) {
                        Icon(Icons.Outlined.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Obtener mi ubicación", fontWeight = FontWeight.SemiBold, maxLines = 1)
                    }

                    OutlinedButton(
                        onClick = {
                            when {
                                siguiendo -> vm.detenerSeguimiento()
                                tienePermiso() -> {
                                    permisoConcedido = true
                                    vm.iniciarSeguimiento()
                                }
                                else -> alPulsar()
                            }
                        },
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Icon(
                            if (siguiendo) Icons.Outlined.Stop else Icons.Outlined.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(if (siguiendo) "Detener" else "Iniciar", fontWeight = FontWeight.SemiBold)
                    }
                }

                when (val e = estado) {
                    UbicacionUi.Buscando -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    is UbicacionUi.Error -> MensajeError(error = e, onReintentar = { alPulsar() })
                    else -> Unit
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            masCercano?.let { "Más cercano: ${it.nombre}" } ?: "Pulsa el botón para ver tu ubicación",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                        (estado as? UbicacionUi.Lista)?.let {
                            Text(
                                "Latitud: ${"%.4f".format(it.posicion.latitud)} · Longitud: ${"%.4f".format(it.posicion.longitud)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    TextButton(
                        onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                        enabled = masCercano != null
                    ) { Text("Ir al más cercano", fontWeight = FontWeight.SemiBold) }
                }

                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    dentroDelRadio = dentroDelRadio,
                    masCercano = masCercano?.id,
                    onClick = { vm.seleccionar(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
