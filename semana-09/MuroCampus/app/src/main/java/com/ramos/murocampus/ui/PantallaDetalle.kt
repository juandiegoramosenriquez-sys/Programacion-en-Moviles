package com.ramos.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramos.murocampus.viewmodel.PostsViewModel
import com.ramos.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    editando: Boolean,
    onCerrarEdicion: () -> Unit,
    confirmarBorrado: Boolean,
    onCerrarBorrado: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val borrado by vm.borrado.collectAsStateWithLifecycle()
    val editado by vm.editado.collectAsStateWithLifecycle()
    val autores by vm.autores.collectAsStateWithLifecycle()
    var nuevoTitulo by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(editando) {
        val actual = estado
        if (editando && actual is UiState.Exito) nuevoTitulo = actual.datos.post.title
    }

    if (editando) {
        AlertDialog(
            onDismissRequest = onCerrarEdicion,
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
                        onCerrarEdicion()
                        vm.editar(id, nuevoTitulo.trim())
                    },
                    enabled = nuevoTitulo.isNotBlank()
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = onCerrarEdicion) { Text("Cancelar") }
            }
        )
    }

    if (confirmarBorrado) {
        AlertDialog(
            onDismissRequest = onCerrarBorrado,
            title = { Text("¿Eliminar publicación?") },
            text = { Text("La publicación dejará de aparecer en el muro. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    onCerrarBorrado()
                    vm.borrar(id)
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = onCerrarBorrado) { Text("Cancelar") }
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                val fondoOscuro = MaterialTheme.colorScheme.background.luminance() < 0.5f
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (fondoOscuro) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary,
                        contentColor = if (fondoOscuro) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val autor = nombreAutor(e.datos.post.userId, autores)
                            AvatarUsuario(
                                iniciales = iniciales(autor),
                                fondo = MaterialTheme.colorScheme.tertiaryContainer,
                                texto = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = autor,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                val usuario = autores[e.datos.post.userId]
                                if (usuario != null) {
                                    Text(
                                        text = "@${usuario.username} · ${usuario.email.lowercase()}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                        Text(
                            text = e.datos.post.title.comoTitulo(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 14.dp)
                        )
                        Text(
                            text = e.datos.post.body.comoParrafo(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                val ed = editado
                if (ed is UiState.Error) {
                    Text(ed.mensaje, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                }
                val b = borrado
                if (b is UiState.Error) {
                    Text(b.mensaje, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                }
            }
            item {
                Text(
                    text = "Comentarios · ${e.datos.comentarios.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(e.datos.comentarios, key = { it.id }) { comentario ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AvatarUsuario(
                                iniciales = comentario.email.first().uppercase(),
                                fondo = MaterialTheme.colorScheme.secondaryContainer,
                                texto = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = comentario.email.lowercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = comentario.body.comoParrafo(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
