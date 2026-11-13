package com.tecsup.catalogo.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.catalogo.R
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.DatoEncabezado
import com.tecsup.catalogo.ui.components.Encabezado
import com.tecsup.catalogo.ui.components.ErrorView
import com.tecsup.catalogo.ui.components.EstadoVacio
import com.tecsup.catalogo.ui.components.TituloEncabezado
import com.tecsup.catalogo.ui.lista.ItemFila
import com.tecsup.catalogo.ui.theme.Cian
import java.util.Locale

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    onExplorar: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val favoritos = (estado as? UiState.Exito<List<Item>>)?.datos.orEmpty()
    val total = favoritos.sumOf { it.precioNumero }

    Column(modifier = Modifier.fillMaxSize()) {
        Encabezado {
            TituloEncabezado(stringResource(R.string.favoritos_titulo))
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DatoEncabezado(
                    valor = favoritos.size.toString(),
                    etiqueta = stringResource(R.string.favoritos_guardados),
                    modifier = Modifier.weight(1f)
                )
                DatoEncabezado(
                    valor = String.format(Locale.US, "S/ %.2f", total),
                    etiqueta = stringResource(R.string.favoritos_valor_total),
                    colorValor = Cian,
                    modifier = Modifier.weight(1.6f)
                )
            }
        }

        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = onExplorar)
            is UiState.Exito -> {
                if (e.datos.isEmpty()) {
                    EstadoVacio(
                        icono = Icons.Outlined.FavoriteBorder,
                        titulo = stringResource(R.string.favoritos_vacio_titulo),
                        texto = stringResource(R.string.favoritos_vacio_texto),
                        textoBoton = stringResource(R.string.favoritos_explorar),
                        onBoton = onExplorar
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(e.datos, key = { it.id }) { item ->
                            ItemFila(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onFavorito = { viewModel.alternarFavorito(item.id) },
                                modoQuitar = true
                            )
                        }
                    }
                }
            }
        }
    }
}
