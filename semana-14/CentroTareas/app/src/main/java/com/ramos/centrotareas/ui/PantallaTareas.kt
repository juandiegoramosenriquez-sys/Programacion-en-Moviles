package com.ramos.centrotareas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramos.centrotareas.model.CargaUi
import com.ramos.centrotareas.model.PrimosUi
import com.ramos.centrotareas.viewmodel.TareasViewModel

private val Esquina = RoundedCornerShape(8.dp)
private val EsquinaBoton = RoundedCornerShape(6.dp)

@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val limiteTiempo by vm.limiteTiempo.collectAsState()
    val intento by vm.intento.collectAsState()
    val historial by vm.historial.collectAsState()
    val comparacion by vm.comparacion.collectAsState()
    val tareasActivas by vm.tareasActivas.collectAsState()
    var toques by rememberSaveable { mutableIntStateOf(0) }
    var numero by rememberSaveable { mutableStateOf("500000") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Cabecera(toques = toques, tareasActivas = tareasActivas, onTocar = { toques++ })
        Column(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaTarea(
                titulo = "Tarea con progreso",
                icono = Icons.Outlined.Timelapse,
                estado = {
                    when {
                        corriendo -> Estado("Trabajando", Tono.Activo)
                        mensaje == "Cancelado" -> Estado("Cancelado", Tono.Error)
                        mensaje == "Terminó" -> Estado("Terminado", Tono.Exito)
                        else -> Estado("En espera", Tono.Neutro)
                    }
                }
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        "${(progreso * 100).toInt()}%",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        mensaje,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                Linea(progreso)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BotonNegro("Iniciar", !corriendo) { vm.tareaLarga() }
                    BotonCancelar(corriendo) { vm.cancelar() }
                }
            }

            TarjetaTarea(
                titulo = "Contar primos",
                icono = Icons.Outlined.Functions,
                estado = {
                    when (primos) {
                        is PrimosUi.Calculando -> Estado("Calculando", Tono.Activo)
                        is PrimosUi.Listo -> Estado("Listo", Tono.Exito)
                        PrimosUi.Cancelado -> Estado("Cancelado", Tono.Error)
                        PrimosUi.Inicial -> {}
                    }
                }
            ) {
                OutlinedTextField(
                    value = numero,
                    onValueChange = { numero = it.filter { c -> c.isDigit() } },
                    label = { Text("Contar primos hasta") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = Esquina,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BotonNegro(
                        "Contar",
                        primos !is PrimosUi.Calculando && (numero.toIntOrNull() ?: 0) >= 2
                    ) { numero.toIntOrNull()?.let { vm.contar(it) } }
                    BotonCancelar(primos is PrimosUi.Calculando) { vm.cancelarPrimos() }
                }
                when (val p = primos) {
                    PrimosUi.Inicial -> {}
                    is PrimosUi.Calculando -> Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { p.porcentaje },
                            modifier = Modifier.size(40.dp),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            strokeCap = StrokeCap.Butt
                        )
                        Column {
                            Text(
                                "Calculando ${(p.porcentaje * 100).toInt()}%",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "${p.parcial} primos hasta ahora",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    is PrimosUi.Listo -> Resultado {
                        Cifra("${p.cantidad}", "primos encontrados")
                        Text(
                            "Calculado en ${p.milisegundos} ms",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    PrimosUi.Cancelado -> Aviso("Cálculo cancelado")
                }
            }

            TarjetaTarea(
                titulo = "Dos cargas",
                icono = Icons.Outlined.CloudDownload,
                estado = {
                    when (carga) {
                        CargaUi.Cargando -> Estado("Cargando", Tono.Activo)
                        is CargaUi.Exito -> Estado("Listo", Tono.Exito)
                        is CargaUi.Error -> Estado("Error", Tono.Error)
                        CargaUi.Inicial -> {}
                    }
                }
            ) {
                ControlSegmentado(
                    habilitado = carga !is CargaUi.Cargando,
                    onUnaPorUna = { vm.cargarUnaPorUna() },
                    onALaVez = { vm.cargarALaVez() }
                )
                Column {
                    FilaInterruptor("Simular error", simularError) { vm.cambiarSimularError(it) }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    FilaInterruptor("Tiempo máximo 4 s", limiteTiempo) { vm.cambiarLimiteTiempo(it) }
                }
                when (val c = carga) {
                    CargaUi.Inicial -> {}
                    CargaUi.Cargando -> Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            if (intento > 1) "Reintentando: intento $intento de 3" else "Cargando datos...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    is CargaUi.Exito -> Resultado {
                        Dato("Modo", c.modo)
                        Dato("Perfil", c.perfil)
                        Dato("Pedidos", c.pedidos.toString())
                        Dato("Tiempo", "${c.milisegundos} ms")
                    }
                    is CargaUi.Error -> {
                        Aviso(c.mensaje)
                        Row { BotonNegro("Reintentar", true) { vm.cargarALaVez() } }
                    }
                }
            }

            TarjetaTarea(titulo = "Comparación de tiempos", icono = Icons.Outlined.BarChart) {
                val lento = comparacion.promedioUnaPorUna
                val rapido = comparacion.promedioALaVez
                if (lento == null && rapido == null) {
                    Text(
                        "Prueba Una por una y A la vez para comparar",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    val maximo = maxOf(lento ?: 0L, rapido ?: 0L).coerceAtLeast(1L)
                    BarraTiempo(
                        "Una por una",
                        lento,
                        comparacion.unaPorUna.size,
                        maximo,
                        MaterialTheme.colorScheme.outlineVariant
                    )
                    BarraTiempo(
                        "A la vez",
                        rapido,
                        comparacion.aLaVez.size,
                        maximo,
                        MaterialTheme.colorScheme.secondary
                    )
                    val ahorro = comparacion.ahorroMs
                    val porcentaje = comparacion.ahorroPorcentaje
                    if (ahorro != null && porcentaje != null) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                "$ahorro ms",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "ahorrados con async ($porcentaje %)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                    }
                    TextButton(onClick = { vm.limpiarComparacion() }) {
                        Text("Limpiar comparación", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            TarjetaTarea(titulo = "Historial", icono = Icons.Outlined.History) {
                if (historial.isEmpty()) {
                    Text(
                        "Tus resultados aparecerán aquí",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column {
                        historial.forEachIndexed { indice, linea ->
                            if (indice > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                            Text(
                                linea,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Cabecera(toques: Int, tareasActivas: Int, onTocar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column {
                Text(
                    "PANEL DE HILOS",
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    "CentroTareas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Indicador(
                    "Toques",
                    "$toques",
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    MaterialTheme.colorScheme.onSurface,
                    Modifier.weight(1f)
                )
                Indicador(
                    "Activas",
                    "$tareasActivas",
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = onTocar,
                    shape = EsquinaBoton,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Icon(Icons.Outlined.TouchApp, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Tócame")
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun Indicador(
    etiqueta: String,
    valor: String,
    fondo: Color,
    colorEtiqueta: Color,
    colorValor: Color,
    modifier: Modifier = Modifier
) {
    Surface(color = fondo, shape = Esquina, modifier = modifier) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(etiqueta, style = MaterialTheme.typography.labelMedium, color = colorEtiqueta)
            Text(
                valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = colorValor
            )
        }
    }
}

@Composable
private fun Cifra(valor: String, etiqueta: String) {
    Column {
        Text(
            valor,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            etiqueta,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TarjetaTarea(
    titulo: String,
    icono: ImageVector,
    estado: @Composable () -> Unit = {},
    contenido: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = Esquina,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icono,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                estado()
            }
            contenido()
        }
    }
}

private enum class Tono { Activo, Exito, Error, Neutro }

@Composable
private fun Estado(texto: String, tono: Tono) {
    val color = when (tono) {
        Tono.Activo -> MaterialTheme.colorScheme.secondary
        Tono.Exito -> MaterialTheme.colorScheme.tertiary
        Tono.Error -> MaterialTheme.colorScheme.error
        Tono.Neutro -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(7.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(6.dp))
        Text(texto, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Composable
private fun Resultado(contenido: @Composable ColumnScope.() -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = Esquina,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            content = contenido
        )
    }
}

@Composable
private fun Aviso(texto: String) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = Esquina,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            texto,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun Linea(valor: Float, color: Color = MaterialTheme.colorScheme.secondary) {
    LinearProgressIndicator(
        progress = { valor },
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp),
        color = color,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeCap = StrokeCap.Butt,
        gapSize = 0.dp,
        drawStopIndicator = {}
    )
}

@Composable
private fun RowScope.BotonNegro(texto: String, habilitado: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        shape = EsquinaBoton,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .weight(1f)
            .height(44.dp)
    ) { Text(texto, fontWeight = FontWeight.Medium) }
}

@Composable
private fun RowScope.BotonCancelar(habilitado: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        enabled = habilitado,
        shape = EsquinaBoton,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error,
            disabledContentColor = MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .weight(1f)
            .height(44.dp)
    ) { Text("Cancelar", fontWeight = FontWeight.Medium) }
}

@Composable
private fun ControlSegmentado(habilitado: Boolean, onUnaPorUna: () -> Unit, onALaVez: () -> Unit) {
    Surface(
        shape = EsquinaBoton,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
    ) {
        Row {
            Surface(
                onClick = onUnaPorUna,
                enabled = habilitado,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "Una por una",
                        fontWeight = FontWeight.Medium,
                        color = if (habilitado) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
            Surface(
                onClick = onALaVez,
                enabled = habilitado,
                color = if (habilitado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "A la vez",
                        fontWeight = FontWeight.Medium,
                        color = if (habilitado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaInterruptor(texto: String, activo: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = activo,
            onCheckedChange = onCambio,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedBorderColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
    }
}

@Composable
private fun BarraTiempo(modo: String, promedio: Long?, veces: Int, maximo: Long, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                if (veces > 0) "$modo  ·  $veces ${if (veces == 1) "prueba" else "pruebas"}" else modo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                if (promedio == null) "sin datos" else "$promedio ms",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Linea((promedio ?: 0L).toFloat() / maximo, color)
    }
}
