package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun SelectorCantidad(
    valor: Double,
    paso: Double,
    minimo: Double,
    maximo: Double,
    onCambio: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val entero = paso % 1.0 == 0.0
    val texto = if (entero) valor.toInt().toString() else String.format(Locale.US, "%.1f", valor)
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        FilledTonalIconButton(
            onClick = { onCambio(if (valor - paso < minimo) 0.0 else valor - paso) },
            enabled = valor > 0.0
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Menos")
        }
        Text(
            texto,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(min = 44.dp)
        )
        FilledTonalIconButton(
            onClick = { onCambio(if (valor < minimo) minimo else minOf(valor + paso, maximo)) },
            enabled = valor < maximo
        ) {
            Icon(Icons.Default.Add, contentDescription = "Más")
        }
    }
}
