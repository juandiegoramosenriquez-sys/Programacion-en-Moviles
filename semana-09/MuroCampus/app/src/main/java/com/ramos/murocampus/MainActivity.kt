package com.ramos.murocampus

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramos.murocampus.ui.PantallaCrear
import com.ramos.murocampus.ui.PantallaDetalle
import com.ramos.murocampus.ui.PantallaLista
import com.ramos.murocampus.ui.theme.MuroCampusTheme
import com.ramos.murocampus.viewmodel.PostsViewModel
import com.ramos.murocampus.viewmodel.PostsViewModelFactory
import com.ramos.murocampus.viewmodel.TemaViewModel
import com.ramos.murocampus.viewmodel.UiState

class MainActivity : ComponentActivity() {

    private val vm: PostsViewModel by viewModels { PostsViewModelFactory(applicationContext) }
    private val temaVm: TemaViewModel by viewModels { PostsViewModelFactory(applicationContext) }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val elegido by temaVm.modoOscuro.collectAsStateWithLifecycle()
            val oscuro = elegido ?: isSystemInDarkTheme()

            LaunchedEffect(oscuro) {
                val barras = if (oscuro) {
                    SystemBarStyle.dark(Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                }
                enableEdgeToEdge(statusBarStyle = barras, navigationBarStyle = barras)
            }

            MuroCampusTheme(darkTheme = oscuro) {
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var creando by rememberSaveable { mutableStateOf(false) }
                var editando by rememberSaveable { mutableStateOf(false) }
                var confirmarBorrado by rememberSaveable { mutableStateOf(false) }
                val borrado by vm.borrado.collectAsStateWithLifecycle()
                val editado by vm.editado.collectAsStateWithLifecycle()
                val avisos = remember { SnackbarHostState() }

                LaunchedEffect(borrado) {
                    val b = borrado
                    if (b is UiState.Exito) {
                        idSeleccionado = null
                        vm.reiniciarBorrado()
                        avisos.showSnackbar("Publicación eliminada")
                    }
                }

                LaunchedEffect(editado) {
                    val ed = editado
                    if (ed is UiState.Exito) {
                        avisos.showSnackbar("Título actualizado")
                    }
                }

                val botonTema: @Composable () -> Unit = {
                    IconButton(onClick = { temaVm.cambiar(!oscuro) }) {
                        Icon(
                            imageVector = if (oscuro) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                            contentDescription = if (oscuro) "Cambiar a modo claro" else "Cambiar a modo oscuro",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                val id = idSeleccionado

                Scaffold(
                    snackbarHost = { SnackbarHost(avisos) },
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            navigationIcon = {
                                when {
                                    creando -> IconButton(onClick = { creando = false }) {
                                        Icon(Icons.Outlined.Close, contentDescription = "Cerrar")
                                    }
                                    id != null -> IconButton(onClick = { idSeleccionado = null }) {
                                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver")
                                    }
                                }
                            },
                            title = {
                                when {
                                    creando -> Text("Nueva publicación")
                                    id != null -> Text("Publicación")
                                    else -> Text(
                                        buildAnnotatedString {
                                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) { append("Muro") }
                                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary)) { append("Campus") }
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            actions = {
                                if (id != null && !creando) {
                                    IconButton(
                                        onClick = { editando = true },
                                        enabled = editado !is UiState.Cargando
                                    ) {
                                        Icon(Icons.Outlined.Edit, contentDescription = "Editar título")
                                    }
                                    IconButton(
                                        onClick = { confirmarBorrado = true },
                                        enabled = borrado !is UiState.Cargando
                                    ) {
                                        Icon(
                                            Icons.Outlined.Delete,
                                            contentDescription = "Eliminar publicación",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                botonTema()
                            }
                        )
                    },
                    floatingActionButton = {
                        if (id == null && !creando) {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    vm.reiniciarCreacion()
                                    creando = true
                                },
                                icon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                                text = { Text("Nueva") },
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                ) { padding ->
                    val contenido = Modifier
                        .fillMaxSize()
                        .padding(padding)
                    when {
                        creando -> {
                            BackHandler { creando = false }
                            PantallaCrear(vm = vm, modifier = contenido)
                        }
                        id != null -> {
                            BackHandler { idSeleccionado = null }
                            PantallaDetalle(
                                vm = vm,
                                id = id,
                                editando = editando,
                                onCerrarEdicion = { editando = false },
                                confirmarBorrado = confirmarBorrado,
                                onCerrarBorrado = { confirmarBorrado = false },
                                modifier = contenido
                            )
                        }
                        else -> PantallaLista(
                            vm = vm,
                            onAbrir = {
                                idSeleccionado = it
                                vm.abrir(it)
                            },
                            modifier = contenido
                        )
                    }
                }
            }
        }
    }
}
