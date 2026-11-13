package com.tecsup.catalogo.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.catalogo.R
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.DatoEncabezado
import com.tecsup.catalogo.ui.components.Encabezado
import com.tecsup.catalogo.ui.components.TituloEncabezado
import com.tecsup.catalogo.ui.theme.Rosa

@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val sincronizando by viewModel.sincronizando.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    var confirmarLimpieza by rememberSaveable { mutableStateOf(false) }
    val totales = (estado as? UiState.Exito<Totales>)?.datos

    Column(modifier = Modifier.fillMaxSize()) {
        Encabezado {
            TituloEncabezado(stringResource(R.string.admin_titulo))
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DatoEncabezado(
                    valor = totales?.guardados?.toString() ?: "-",
                    etiqueta = stringResource(R.string.admin_guardados),
                    modifier = Modifier.weight(1f)
                )
                DatoEncabezado(
                    valor = totales?.favoritos?.toString() ?: "-",
                    etiqueta = stringResource(R.string.admin_favoritos),
                    colorValor = Rosa,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = viewModel::sincronizar,
                enabled = !sincronizando,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Outlined.Sync, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(if (sincronizando) R.string.admin_sincronizando else R.string.admin_sincronizar))
            }
            OutlinedButton(
                onClick = { confirmarLimpieza = true },
                enabled = !sincronizando,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Outlined.DeleteOutline, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.admin_limpiar))
            }
            mensaje?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (confirmarLimpieza) {
        AlertDialog(
            onDismissRequest = { confirmarLimpieza = false },
            title = { Text(stringResource(R.string.admin_confirmar_titulo)) },
            text = { Text(stringResource(R.string.admin_confirmar_texto)) },
            confirmButton = {
                TextButton(onClick = {
                    confirmarLimpieza = false
                    viewModel.limpiarCache()
                }) {
                    Text(stringResource(R.string.admin_confirmar_si), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarLimpieza = false }) {
                    Text(stringResource(R.string.admin_confirmar_no))
                }
            }
        )
    }
}
