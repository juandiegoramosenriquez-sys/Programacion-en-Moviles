package com.ramos.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ramos.campusgps.model.UbicacionUi
import com.ramos.campusgps.util.formatearDistancia
import com.ramos.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugaresVisibles.collectAsState()
    val categoria by vm.categoria.collectAsState()
    val radio by vm.radio.collectAsState()
    val dentroDelRadio by vm.dentroDelRadio.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val siguiendo by vm.siguiendo.collectAsState()
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FiltroCategorias(
                categorias = vm.categorias,
                seleccionada = categoria,
                onElegir = { vm.filtrar(it) },
                modifier = Modifier.fillMaxWidth()
            )

            SelectorRadio(
                radios = vm.radios,
                seleccionado = radio,
                onElegir = { vm.elegirRadio(it) },
                modifier = Modifier.fillMaxWidth()
            )

            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                radio = radio,
                permisoConcedido = permisoConcedido,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { alPulsar() },
                    enabled = estado !is UbicacionUi.Buscando && !siguiendo
                ) { Text("Obtener mi ubicación") }

                FilledTonalButton(
                    onClick = {
                        when {
                            siguiendo -> vm.detenerSeguimiento()
                            tienePermiso() -> {
                                permisoConcedido = true
                                vm.iniciarSeguimiento()
                            }
                            else -> alPulsar()
                        }
                    }
                ) { Text(if (siguiendo) "Detener" else "Iniciar") }
            }

            if (siguiendo) Text("Siguiendo tu ubicación en vivo")

            OutlinedButton(
                onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                enabled = masCercano != null
            ) { Text("Ir al más cercano") }

            masCercano?.let { lugar ->
                val metros = lugares.firstOrNull { it.lugar.id == lugar.id }?.metros
                val distancia = metros?.let { formatearDistancia(it) } ?: ""
                Text("Más cercano: ${lugar.nombre} ($distancia)")
            }

            when (val e = estado) {
                UbicacionUi.Inicial -> Text("Pulsa el botón para ver tu ubicación")
                UbicacionUi.Buscando -> CircularProgressIndicator()
                is UbicacionUi.Lista -> {
                    Text("Latitud: ${e.posicion.latitud}")
                    Text("Longitud: ${e.posicion.longitud}")
                }
                is UbicacionUi.Error -> MensajeError(error = e, onReintentar = { alPulsar() })
            }

            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                dentroDelRadio = dentroDelRadio,
                onClick = { vm.seleccionar(it) },
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )
        }
    }
}
