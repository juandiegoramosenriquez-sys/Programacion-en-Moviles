package com.ramos.avisoscampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ramos.avisoscampus.model.Origen
import com.ramos.avisoscampus.ui.theme.LocalPaleta
import com.ramos.avisoscampus.ui.theme.Pastel

@Composable
fun Tarjeta(
    titulo: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    accion: @Composable (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val p = LocalPaleta.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(p.tarjeta, RoundedCornerShape(16.dp))
            .border(1.dp, p.borde, RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (icono != null) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(p.arena, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icono, contentDescription = null, tint = p.texto, modifier = Modifier.size(18.dp))
                }
            }
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = p.texto,
                modifier = Modifier.weight(1f)
            )
            accion?.invoke()
        }
        contenido()
    }
}

@Composable
fun Pildora(texto: String, colores: Pastel) {
    Text(
        text = texto,
        color = colores.texto,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .background(colores.fondo, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
fun colorOrigen(origen: Origen): Pastel {
    val p = LocalPaleta.current
    return when (origen) {
        Origen.BOTON -> p.etiquetaBoton
        Origen.RECORDATORIO -> p.etiquetaRecordatorio
        Origen.MODO_AVION -> p.etiquetaAvion
        Origen.CARGADOR -> p.etiquetaCargador
    }
}

@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icono: ImageVector? = null,
    fondo: Color = LocalPaleta.current.boton,
    contenidoColor: Color = LocalPaleta.current.textoBoton
) {
    val p = LocalPaleta.current
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = fondo,
            contentColor = contenidoColor,
            disabledContainerColor = p.borde,
            disabledContentColor = p.textoSecundario
        ),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        if (icono != null) {
            Icon(icono, contentDescription = null, modifier = Modifier.size(18.dp))
            Text("  ")
        }
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun BotonBorde(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val p = LocalPaleta.current
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (enabled) p.texto.copy(alpha = 0.25f) else p.borde),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = p.texto,
            disabledContentColor = p.textoSecundario.copy(alpha = 0.6f)
        )
    ) {
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun coloresSwitch() = LocalPaleta.current.let { p ->
    SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = p.verde,
        checkedBorderColor = p.verde,
        uncheckedThumbColor = p.textoSecundario,
        uncheckedTrackColor = p.fondo,
        uncheckedBorderColor = p.borde
    )
}

@Composable
fun AnilloCuenta(faltan: Int?, total: Int = 10) {
    val p = LocalPaleta.current
    val progreso = (faltan ?: total).toFloat() / total
    Box(modifier = Modifier.size(84.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val grosor = 7.dp.toPx()
            drawArc(
                color = p.borde,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(grosor)
            )
            drawArc(
                color = if (faltan != null) p.verde else p.borde,
                startAngle = -90f,
                sweepAngle = 360f * progreso,
                useCenter = false,
                style = Stroke(grosor, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${faltan ?: total} s",
                style = MaterialTheme.typography.titleLarge,
                color = p.texto
            )
            Text(
                text = if (faltan != null) "faltan" else "listo",
                style = MaterialTheme.typography.bodySmall,
                color = p.textoSecundario
            )
        }
    }
}

@Composable
fun Monograma() {
    val p = LocalPaleta.current
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(p.boton, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("AC", style = MaterialTheme.typography.titleMedium, color = p.textoBoton)
    }
}
