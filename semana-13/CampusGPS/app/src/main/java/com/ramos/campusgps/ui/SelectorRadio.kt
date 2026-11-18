package com.ramos.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectorRadio(
    radios: List<Int>,
    seleccionado: Int,
    onElegir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Radio:")
        radios.forEach { metros ->
            FilterChip(
                selected = seleccionado == metros,
                onClick = { onElegir(metros) },
                label = { Text("$metros m") }
            )
        }
    }
}
