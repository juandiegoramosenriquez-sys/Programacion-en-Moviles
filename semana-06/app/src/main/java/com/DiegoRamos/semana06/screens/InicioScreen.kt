package com.DiegoRamos.semana06.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ProductCard(
    productName: String,
    esFavorito: Boolean,          // lo decide AppNavigation
    onFavoritoClick: () -> Unit   // avisa a AppNavigation que cambió
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = productName, style = MaterialTheme.typography.titleMedium)
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(if (esFavorito) "Quitar de favoritos" else "Favoritos") },
                        leadingIcon = {
                            Icon(
                                if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            expanded = false
                            val mensaje = if (esFavorito) "Quitado de favoritos" else "Añadido a favoritos"
                            onFavoritoClick()
                            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Compartir") },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Compartir producto", Toast.LENGTH_SHORT).show()
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Reportar") },
                        leadingIcon = { Icon(Icons.Default.Warning, contentDescription = null) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Producto reportado", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}