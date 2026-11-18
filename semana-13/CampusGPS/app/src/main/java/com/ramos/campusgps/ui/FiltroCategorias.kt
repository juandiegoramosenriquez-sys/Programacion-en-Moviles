package com.ramos.campusgps.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FiltroCategorias(
    categorias: List<String>,
    seleccionada: String?,
    onElegir: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = FilterChipDefaults.filterChipColors(
        containerColor = MaterialTheme.colorScheme.surface,
        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        selectedLabelColor = Color.White,
        selectedLeadingIconColor = Color.White
    )

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = seleccionada == null,
            onClick = { onElegir(null) },
            label = { Text("Todas", fontWeight = FontWeight.SemiBold) },
            colors = colores,
            shape = MaterialTheme.shapes.medium
        )
        categorias.forEach { cat ->
            FilterChip(
                selected = seleccionada == cat,
                onClick = { onElegir(if (seleccionada == cat) null else cat) },
                label = { Text(cat, fontWeight = FontWeight.SemiBold) },
                leadingIcon = {
                    Icon(iconoCategoria(cat), contentDescription = null, modifier = Modifier.size(18.dp))
                },
                colors = colores,
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}
