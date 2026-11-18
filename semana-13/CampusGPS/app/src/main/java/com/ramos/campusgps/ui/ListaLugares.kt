package com.ramos.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ramos.campusgps.model.LugarConDistancia
import com.ramos.campusgps.ui.theme.Verde
import com.ramos.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    dentroDelRadio: Set<Int>,
    masCercano: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estadoLista = rememberLazyListState()

    LaunchedEffect(seleccionado, lugares) {
        val indice = lugares.indexOfFirst { it.lugar.id == seleccionado }
        if (indice >= 0) estadoLista.animateScrollToItem(indice)
    }

    LazyRow(
        state = estadoLista,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            val esSeleccionado = item.lugar.id == seleccionado
            val tono = if (item.lugar.id == masCercano) Verde else MaterialTheme.colorScheme.primary
            Card(
                onClick = { onClick(item.lugar.id) },
                modifier = Modifier.width(232.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = if (esSeleccionado) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                border = BorderStroke(
                    if (esSeleccionado) 1.5.dp else 1.dp,
                    if (esSeleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(tono.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            iconoCategoria(item.lugar.categoria),
                            contentDescription = null,
                            tint = tono,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            item.lugar.nombre,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                item.metros?.let { formatearDistancia(it) } ?: "Sin calcular",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (item.metros == null) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                            Text(
                                " · ${item.lugar.categoria}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (item.lugar.id in dentroDelRadio) {
                            Text(
                                "Dentro del radio",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
