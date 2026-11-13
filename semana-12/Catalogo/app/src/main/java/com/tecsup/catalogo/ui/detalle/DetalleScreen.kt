package com.tecsup.catalogo.ui.detalle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.catalogo.R
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.AparecerEnOrden
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView
import com.tecsup.catalogo.ui.components.Etiqueta
import com.tecsup.catalogo.ui.theme.Ambar
import com.tecsup.catalogo.ui.theme.AmbarOscuro
import com.tecsup.catalogo.ui.theme.AmbarSuave
import com.tecsup.catalogo.ui.theme.Borde
import com.tecsup.catalogo.ui.theme.Medianoche
import com.tecsup.catalogo.ui.theme.Rojo
import com.tecsup.catalogo.ui.theme.RojoSuave
import com.tecsup.catalogo.ui.theme.TextoEncabezado
import com.tecsup.catalogo.ui.theme.VerdeOscuro
import com.tecsup.catalogo.ui.theme.VerdeSuave
import kotlin.math.roundToInt

@Composable
fun DetalleScreen(viewModel: DetalleViewModel, onVolver: () -> Unit) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Medianoche)
                .statusBarsPadding()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = stringResource(R.string.volver), tint = TextoEncabezado)
            }
            Text(
                text = stringResource(R.string.detalle_titulo),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextoEncabezado
            )
        }
        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = viewModel::cargar)
            is UiState.Exito -> DetalleContenido(item = e.datos, onFavorito = viewModel::alternarFavorito)
        }
    }
}

@Composable
fun DetalleContenido(item: Item, onFavorito: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            AparecerEnOrden(0) {
                AsyncImage(
                    model = item.imagenUrl,
                    contentDescription = item.titulo,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(Medianoche)
                        .padding(20.dp)
                )
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AparecerEnOrden(1) {
                    Seccion {
                        Etiqueta(
                            texto = item.marca,
                            fondo = MaterialTheme.colorScheme.primaryContainer,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(item.titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Estrellas(item.rating)
                    }
                }
                AparecerEnOrden(2) {
                    Seccion(titulo = stringResource(R.string.detalle_precio)) {
                        Text(
                            text = item.precio,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.precio_referencial),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                AparecerEnOrden(3) {
                    Seccion(titulo = stringResource(R.string.detalle_datos)) {
                        val hayStock = item.stock > 0
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Dato(Icons.Outlined.StarOutline, item.rating.toString(), stringResource(R.string.detalle_calificacion), AmbarSuave, AmbarOscuro, Modifier.weight(1f))
                            Dato(
                                Icons.Outlined.Inventory2,
                                item.stock.toString(),
                                stringResource(if (hayStock) R.string.detalle_stock else R.string.detalle_agotado),
                                if (hayStock) VerdeSuave else RojoSuave,
                                if (hayStock) VerdeOscuro else Rojo,
                                Modifier.weight(1f)
                            )
                            Dato(
                                Icons.Outlined.Sell,
                                item.marca,
                                stringResource(R.string.detalle_marca),
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.onPrimaryContainer,
                                Modifier.weight(1f)
                            )
                        }
                    }
                }
                AparecerEnOrden(4) {
                    Seccion(titulo = stringResource(R.string.detalle_descripcion)) {
                        Text(item.descripcion, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        AparecerEnOrden(5) {
            Button(
                onClick = onFavorito,
                shape = RoundedCornerShape(8.dp),
                colors = if (item.esFavorito) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                } else {
                    ButtonDefaults.buttonColors()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .height(52.dp)
            ) {
                Icon(if (item.esFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    stringResource(if (item.esFavorito) R.string.detalle_en_favoritos else R.string.detalle_guardar),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun Seccion(titulo: String? = null, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            titulo?.let {
                Text(it, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(6.dp))
            }
            contenido()
        }
    }
}

@Composable
private fun Estrellas(rating: Double) {
    val llenas = rating.roundToInt().coerceIn(0, 5)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("★".repeat(llenas), color = Ambar, style = MaterialTheme.typography.titleMedium)
        Text("★".repeat(5 - llenas), color = Borde, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.size(6.dp))
        Text(rating.toString(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Dato(
    icono: ImageVector,
    valor: String,
    titulo: String,
    fondo: Color,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(fondo)
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Text(valor, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = color, maxLines = 1)
        Text(titulo, style = MaterialTheme.typography.labelSmall, color = color, maxLines = 1)
    }
}
