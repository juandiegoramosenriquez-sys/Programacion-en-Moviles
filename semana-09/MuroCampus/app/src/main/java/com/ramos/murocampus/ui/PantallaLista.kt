package com.ramos.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramos.murocampus.viewmodel.PostsViewModel
import com.ramos.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, onAbrir: (Int) -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val sinConexion by vm.sinConexion.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = { vm.buscar(it) },
            placeholder = { Text("Buscar por título") },
            singleLine = true,
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    TextButton(onClick = { vm.buscar("") }) { Text("Limpiar") }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp)
        )

        FiltroUsuarios(seleccionado = filtro, onElegir = { vm.filtrar(it) })

        if (sinConexion && estado is UiState.Exito) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp, end = 4.dp)
                ) {
                    Text(
                        text = "Sin conexión. Mostrando publicaciones guardadas.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { vm.cargar() }) { Text("Reintentar") }
                }
            }
        }

        val contenido = Modifier
            .fillMaxWidth()
            .weight(1f)

        when (val e = estado) {
            is UiState.Cargando -> Cargando("Cargando publicaciones...", contenido)
            is UiState.Error -> Box(modifier = contenido) {
                ErrorConReintento(e.mensaje, onReintentar = { vm.cargar() })
            }
            is UiState.Exito -> if (e.datos.isEmpty()) {
                Box(modifier = contenido, contentAlignment = Alignment.Center) {
                    Text(
                        if (busqueda.isNotBlank()) "Ninguna publicación coincide con «${busqueda.trim()}»"
                        else "Sin publicaciones"
                    )
                }
            } else {
                LazyColumn(
                    modifier = contenido,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(e.datos, key = { it.id }) { post ->
                        TarjetaPost(
                            userId = post.userId,
                            titulo = post.title,
                            cuerpo = post.body,
                            onClick = { onAbrir(post.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FiltroUsuarios(seleccionado: Int?, onElegir: (Int?) -> Unit) {
    val opciones: List<Int?> = listOf(null) + (1..10).toList()

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(opciones) { userId ->
            FilterChip(
                selected = seleccionado == userId,
                onClick = { onElegir(userId) },
                label = { Text(if (userId == null) "Todos" else "Usuario $userId") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}
