package com.ramos.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramos.murocampus.viewmodel.PostsViewModel
import com.ramos.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val borrado by vm.borrado.collectAsStateWithLifecycle()
    val editado by vm.editado.collectAsStateWithLifecycle()
    var confirmar by remember { mutableStateOf(false) }
    var editando by remember { mutableStateOf(false) }
    var nuevoTitulo by rememberSaveable { mutableStateOf("") }

    if (editando) {
        AlertDialog(
            onDismissRequest = { editando = false },
            title = { Text("Editar título") },
            text = {
                OutlinedTextField(
                    value = nuevoTitulo,
                    onValueChange = { nuevoTitulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        editando = false
                        vm.editar(id, nuevoTitulo.trim())
                    },
                    enabled = nuevoTitulo.isNotBlank()
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { editando = false }) { Text("Cancelar") }
            }
        )
    }

    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("¿Eliminar publicación?") },
            text = { Text("Se enviará DELETE posts/$id al servidor.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmar = false
                    vm.borrar(id)
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) { Text("Cancelar") }
            }
        )
    }

    when (val e = estado) {
        is UiState.Cargando -> Cargando("Cargando publicación...", modifier)
        is UiState.Error -> Box(modifier = modifier) {
            ErrorConReintento(e.mensaje, onReintentar = { vm.abrir(id) })
        }
        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Usuario ${e.datos.post.userId}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = e.datos.post.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = e.datos.post.body,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                nuevoTitulo = e.datos.post.title
                                editando = true
                            },
                            enabled = editado !is UiState.Cargando
                        ) {
                            Text(if (editado is UiState.Cargando) "Guardando..." else "Editar título")
                        }
                        OutlinedButton(
                            onClick = { confirmar = true },
                            enabled = borrado !is UiState.Cargando,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(if (borrado is UiState.Cargando) "Eliminando..." else "Eliminar")
                        }
                    }
                    when (val ed = editado) {
                        is UiState.Exito -> Text(
                            text = "PATCH posts/${ed.datos.id} → el servidor respondió con el título «${ed.datos.title}»",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        is UiState.Error -> Text(
                            text = ed.mensaje,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        else -> Unit
                    }
                    val b = borrado
                    if (b is UiState.Error) {
                        Text(
                            text = b.mensaje,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
            item {
                HorizontalDivider()
                Text(
                    text = "Comentarios (${e.datos.comentarios.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            items(e.datos.comentarios, key = { it.id }) { comentario ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = comentario.email,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = comentario.body,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
