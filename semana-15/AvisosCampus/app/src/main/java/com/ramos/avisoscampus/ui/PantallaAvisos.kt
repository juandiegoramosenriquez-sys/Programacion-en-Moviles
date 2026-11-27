package com.ramos.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.AirplanemodeActive
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ramos.avisoscampus.model.Aviso
import com.ramos.avisoscampus.model.PermisoUi
import com.ramos.avisoscampus.notificacion.Notificador
import com.ramos.avisoscampus.receiver.CargadorReceiver
import com.ramos.avisoscampus.receiver.ModoAvionReceiver
import com.ramos.avisoscampus.ui.theme.LocalPaleta
import com.ramos.avisoscampus.viewmodel.AvisosViewModel

private data class Pestana(val titulo: String, val icono: ImageVector)

private val pestanas = listOf(
    Pestana("Inicio", Icons.Outlined.Home),
    Pestana("Historial", Icons.Outlined.History),
    Pestana("Ajustes", Icons.Outlined.Settings)
)

@Composable
fun PantallaAvisos(vm: AvisosViewModel, oscuro: Boolean, alCambiarTema: (Boolean) -> Unit) {
    val contexto = LocalContext.current
    val p = LocalPaleta.current
    val permiso by vm.permiso.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()
    val escuchandoCargador by vm.escuchandoCargador.collectAsState()
    val avisos by vm.avisos.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(0) }

    DisposableEffect(escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }

    DisposableEffect(escuchandoCargador) {
        if (escuchandoCargador) {
            val receptor = CargadorReceiver { conectado -> vm.alCambiarCargador(conectado) }
            val filtro = IntentFilter().apply {
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
            }
            ContextCompat.registerReceiver(contexto, receptor, filtro, ContextCompat.RECEIVER_NOT_EXPORTED)
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            if (!mostrado) vm.noSePudoMostrar()
        }
    }

    val pedirPermiso = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            vm.permisoRespondido(true)
        }
    }
    val abrirAjustes = {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
        contexto.startActivity(intent)
    }

    Scaffold(
        containerColor = p.fondo,
        topBar = { Cabecera(cantidad = avisos.size, alTocarCampana = { pestana = 1 }) },
        bottomBar = {
            NavigationBar(containerColor = p.tarjeta, tonalElevation = 0.dp) {
                pestanas.forEachIndexed { i, item ->
                    NavigationBarItem(
                        selected = pestana == i,
                        onClick = { pestana = i },
                        icon = { Icon(item.icono, contentDescription = null) },
                        label = { Text(item.titulo) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = p.textoBoton,
                            selectedTextColor = p.texto,
                            indicatorColor = p.boton,
                            unselectedIconColor = p.textoSecundario,
                            unselectedTextColor = p.textoSecundario
                        )
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (pestana) {
                0 -> Inicio(
                    avisos = avisos,
                    permiso = permiso,
                    mensaje = mensaje,
                    faltan = faltan,
                    escuchando = escuchando,
                    escuchandoCargador = escuchandoCargador,
                    vm = vm,
                    pedirPermiso = pedirPermiso,
                    abrirAjustes = abrirAjustes,
                    verTodo = { pestana = 1 }
                )
                1 -> HistorialAvisos(avisos)
                else -> Ajustes(oscuro, alCambiarTema, abrirAjustes)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun Cabecera(cantidad: Int, alTocarCampana: () -> Unit) {
    val p = LocalPaleta.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(p.fondo)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Monograma()
        Column(modifier = Modifier.weight(1f)) {
            Text("AvisosCampus", style = MaterialTheme.typography.titleLarge, color = p.texto)
            Text("Tu centro de avisos", style = MaterialTheme.typography.bodySmall, color = p.textoSecundario)
        }
        IconButton(onClick = alTocarCampana) {
            BadgedBox(badge = {
                if (cantidad > 0) {
                    Badge(containerColor = p.morado, contentColor = p.tarjeta) {
                        Text(if (cantidad > 99) "99+" else cantidad.toString())
                    }
                }
            }) {
                Icon(Icons.Rounded.Notifications, contentDescription = "Ver historial", tint = p.texto)
            }
        }
    }
}

@Composable
private fun Inicio(
    avisos: List<Aviso>,
    permiso: PermisoUi,
    mensaje: String?,
    faltan: Int?,
    escuchando: Boolean,
    escuchandoCargador: Boolean,
    vm: AvisosViewModel,
    pedirPermiso: () -> Unit,
    abrirAjustes: () -> Unit,
    verTodo: () -> Unit
) {
    val p = LocalPaleta.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(p.portada, RoundedCornerShape(20.dp))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "AVISOS REGISTRADOS · ${avisos.size}",
            style = MaterialTheme.typography.labelSmall,
            color = p.textoPortada.copy(alpha = 0.6f)
        )
        Text(
            text = "Tu campus,\nsiempre al día.",
            style = MaterialTheme.typography.headlineSmall,
            color = p.textoPortada
        )
        Text(
            text = "Envía un aviso de prueba y míralo llegar al panel de notificaciones.",
            style = MaterialTheme.typography.bodyMedium,
            color = p.textoPortada.copy(alpha = 0.7f)
        )
        BotonPrincipal(
            texto = "Mostrar aviso",
            onClick = { vm.avisarAhora() },
            icono = Icons.Rounded.NotificationsActive,
            fondo = p.textoPortada,
            contenidoColor = p.portada
        )
    }

    Tarjeta("Permiso de notificaciones", icono = Icons.Rounded.Shield) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Estado", style = MaterialTheme.typography.bodyMedium, color = p.textoSecundario)
            when (permiso) {
                PermisoUi.SinPedir -> Pildora("Sin pedir", p.etiquetaNeutra)
                PermisoUi.Concedido -> Pildora("Permitido", p.etiquetaAvion)
                PermisoUi.Denegado -> Pildora("Denegado", p.etiquetaError)
            }
        }
        BotonBorde("Permitir notificaciones", onClick = pedirPermiso, modifier = Modifier.fillMaxWidth())
    }

    mensaje?.let { texto ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(p.etiquetaError.fondo, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = texto,
                color = p.etiquetaError.texto,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            BotonPrincipal(
                texto = "Abrir ajustes",
                onClick = abrirAjustes,
                fondo = p.rojo,
                contenidoColor = p.tarjeta
            )
        }
    }

    Tarjeta("Recordatorio", icono = Icons.Rounded.Timer) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AnilloCuenta(faltan)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BotonPrincipal(
                    texto = "Recordar en 10 s",
                    onClick = { vm.recordarEn(10) },
                    enabled = faltan == null,
                    modifier = Modifier.fillMaxWidth()
                )
                BotonBorde(
                    texto = "Cancelar",
                    onClick = { vm.cancelarRecordatorio() },
                    enabled = faltan != null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    Tarjeta("Receptor de anuncios", icono = Icons.Rounded.Tune) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Mosaico(
                icono = Icons.Rounded.AirplanemodeActive,
                titulo = "Escuchar el modo avión",
                detalle = "Activa o desactiva el modo avión",
                activo = escuchando,
                alCambiar = { vm.cambiarEscucha(it) },
                modifier = Modifier.weight(1f)
            )
            Mosaico(
                icono = Icons.Rounded.BatteryChargingFull,
                titulo = "Escuchar el cargador",
                detalle = "Conecta o desconecta el cargador",
                activo = escuchandoCargador,
                alCambiar = { vm.cambiarEscuchaCargador(it) },
                modifier = Modifier.weight(1f)
            )
        }
    }

    HistorialAvisos(
        avisos = avisos,
        titulo = "Últimos avisos",
        limite = 3,
        accion = {
            if (avisos.size > 3) {
                TextButton(onClick = verTodo) { Text("Ver todo", color = p.texto) }
            }
        }
    )
}

@Composable
private fun Mosaico(
    icono: ImageVector,
    titulo: String,
    detalle: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val p = LocalPaleta.current
    Column(
        modifier = modifier
            .background(if (activo) p.etiquetaAvion.fondo else p.fondo, RoundedCornerShape(14.dp))
            .border(1.dp, p.borde, RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(p.tarjeta, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = if (activo) p.verde else p.texto, modifier = Modifier.size(18.dp))
            }
            Switch(checked = activo, onCheckedChange = alCambiar, colors = coloresSwitch())
        }
        Text(titulo, style = MaterialTheme.typography.labelLarge, color = p.texto)
        Text(detalle, style = MaterialTheme.typography.bodySmall, color = p.textoSecundario)
    }
}

@Composable
private fun Ajustes(oscuro: Boolean, alCambiarTema: (Boolean) -> Unit, abrirAjustes: () -> Unit) {
    val p = LocalPaleta.current
    Tarjeta("Apariencia", icono = Icons.Rounded.DarkMode) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Modo oscuro", style = MaterialTheme.typography.bodyLarge, color = p.texto)
                Text("Fondo negro con texto claro", style = MaterialTheme.typography.bodySmall, color = p.textoSecundario)
            }
            Switch(checked = oscuro, onCheckedChange = alCambiarTema, colors = coloresSwitch())
        }
    }
    Tarjeta("Notificaciones", icono = Icons.Rounded.Notifications) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Canal Avisos", style = MaterialTheme.typography.bodyMedium, color = p.texto)
            Pildora("Normal", p.etiquetaCargador)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Canal Urgentes", style = MaterialTheme.typography.bodyMedium, color = p.texto)
            Pildora("Emergente", p.etiquetaBoton)
        }
        BotonBorde("Abrir ajustes de notificaciones", onClick = abrirAjustes, modifier = Modifier.fillMaxWidth())
    }
    Text(
        text = "AvisosCampus 1.0 · Laboratorio 15",
        style = MaterialTheme.typography.bodySmall,
        color = p.textoSecundario,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}
