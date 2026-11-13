package com.tecsup.catalogo.ui.lista

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.catalogo.R
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.BotonFavorito
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.Encabezado
import com.tecsup.catalogo.ui.components.ErrorView
import com.tecsup.catalogo.ui.components.EstadoVacio
import com.tecsup.catalogo.ui.components.Etiqueta
import com.tecsup.catalogo.ui.components.TituloEncabezado
import com.tecsup.catalogo.ui.theme.AmbarOscuro
import com.tecsup.catalogo.ui.theme.AmbarSuave
import com.tecsup.catalogo.ui.theme.MedianocheClaro
import com.tecsup.catalogo.ui.theme.TextoEncabezado
import com.tecsup.catalogo.ui.theme.TextoEncabezadoSuave

private val marcas = listOf("Apple", "Samsung", "Oppo", "Realme", "Vivo")

@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    nombre: String,
    onItemClick: (Int) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    val queryBusqueda by viewModel.queryBusqueda.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Encabezado {
            Text(
                text = stringResource(R.string.lista_hola, nombre),
                style = MaterialTheme.typography.bodyMedium,
                color = TextoEncabezadoSuave
            )
            TituloEncabezado(stringResource(R.string.lista_titulo))
            Spacer(modifier = Modifier.height(12.dp))
            Buscador(texto = queryBusqueda, onCambio = viewModel::onBusquedaChanged)
        }
        ChipsDeMarca(seleccion = queryBusqueda, onSeleccionar = viewModel::onBusquedaChanged)

        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = viewModel::refrescar)
            is UiState.Exito -> {
                aviso?.let { texto ->
                    AvisoSinConexion(texto = texto, onReintentar = viewModel::refrescar)
                }
                when {
                    e.datos.isNotEmpty() -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(e.datos, key = { it.id }) { item ->
                            ItemFila(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onFavorito = { viewModel.alternarFavorito(item.id) }
                            )
                        }
                    }
                    queryBusqueda.isNotBlank() -> EstadoVacio(
                        icono = Icons.Outlined.SearchOff,
                        titulo = stringResource(R.string.buscar_sin_resultados_titulo),
                        texto = stringResource(R.string.buscar_sin_resultados_texto),
                        textoBoton = stringResource(R.string.buscar_ver_todos),
                        onBoton = { viewModel.onBusquedaChanged("") }
                    )
                    else -> EstadoVacio(
                        icono = Icons.Outlined.Inventory2,
                        titulo = stringResource(R.string.lista_vacia_titulo),
                        texto = stringResource(R.string.lista_vacia_texto),
                        textoBoton = stringResource(R.string.lista_actualizar),
                        onBoton = viewModel::refrescar
                    )
                }
            }
        }
    }
}

@Composable
fun Buscador(texto: String, onCambio: (String) -> Unit) {
    OutlinedTextField(
        value = texto,
        onValueChange = onCambio,
        placeholder = { Text(stringResource(R.string.buscar_ejemplo), color = TextoEncabezadoSuave) },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = TextoEncabezadoSuave) },
        trailingIcon = {
            if (texto.isNotBlank()) {
                IconButton(onClick = { onCambio("") }) {
                    Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.buscar_limpiar), tint = TextoEncabezado)
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MedianocheClaro,
            focusedContainerColor = MedianocheClaro,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedTextColor = TextoEncabezado,
            unfocusedTextColor = TextoEncabezado,
            cursorColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ChipsDeMarca(seleccion: String, onSeleccionar: (String) -> Unit) {
    val opciones = listOf(stringResource(R.string.buscar_todas) to "") + marcas.map { it to it }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.forEach { (texto, valor) ->
            val activo = seleccion.equals(valor, ignoreCase = true)
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                color = if (activo) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                    .border(1.dp, if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
                    .clickable { onSeleccionar(valor) }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            )
        }
    }
}

@Composable
fun AvisoSinConexion(texto: String, onReintentar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AmbarSuave)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.WifiOff, contentDescription = null, tint = AmbarOscuro, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            color = AmbarOscuro,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = onReintentar) {
            Text(stringResource(R.string.reintentar), color = AmbarOscuro, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ItemFila(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modoQuitar: Boolean = false
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.imagenUrl,
                    contentDescription = item.titulo,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Etiqueta(
                    texto = item.marca,
                    fondo = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.precio,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            if (modoQuitar) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onFavorito)
                        .padding(8.dp)
                ) {
                    Icon(Icons.Outlined.HeartBroken, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Text(
                        text = stringResource(R.string.favorito_quitar).substringBefore(" "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            } else {
                BotonFavorito(esFavorito = item.esFavorito, onClick = onFavorito)
            }
        }
    }
}
