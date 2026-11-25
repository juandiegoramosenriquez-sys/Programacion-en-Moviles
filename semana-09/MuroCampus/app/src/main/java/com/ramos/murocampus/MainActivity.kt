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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramos.murocampus.ui.CabeceraOlas
import com.ramos.murocampus.ui.MenuLateral
import com.ramos.murocampus.ui.PantallaCrear
import com.ramos.murocampus.ui.PantallaDetalle
import com.ramos.murocampus.ui.PantallaLista
import com.ramos.murocampus.ui.colorCabecera
import com.ramos.murocampus.ui.colorSobreCabecera
import com.ramos.murocampus.ui.theme.MuroCampusTheme
import com.ramos.murocampus.viewmodel.PostsViewModel
import com.ramos.murocampus.viewmodel.PostsViewModelFactory
import com.ramos.murocampus.viewmodel.TemaViewModel
import com.ramos.murocampus.viewmodel.UiState
import kotlinx.coroutines.launch

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
                val barraInferior = if (oscuro) {
                    SystemBarStyle.dark(Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                }
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
                    navigationBarStyle = barraInferior
                )
            }

            MuroCampusTheme(darkTheme = oscuro) {
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var creando by rememberSaveable { mutableStateOf(false) }
                var editando by rememberSaveable { mutableStateOf(false) }
                var confirmarBorrado by rememberSaveable { mutableStateOf(false) }
                var acercaDe by rememberSaveable { mutableStateOf(false) }
                val borrado by vm.borrado.collectAsStateWithLifecycle()
                val editado by vm.editado.collectAsStateWithLifecycle()
                val soloMias by vm.soloMias.collectAsStateWithLifecycle()
                val avisos = remember { SnackbarHostState() }
                val menu = rememberDrawerState(DrawerValue.Closed)
                val alcance = rememberCoroutineScope()

                LaunchedEffect(borrado) {
                    val b = borrado
                    if (b is UiState.Exito) {
                        idSeleccionado = null
                        vm.reiniciarBorrado()
                        avisos.showSnackbar("Publicación eliminada")
                    }
                }

                LaunchedEffect(editado) {
                    if (editado is UiState.Exito) avisos.showSnackbar("Título actualizado")
                }

                if (acercaDe) {
                    AlertDialog(
                        onDismissRequest = { acercaDe = false },
                        title = { Text("MuroCampus") },
                        text = {
                            Text(
                                "El muro de publicaciones de tu campus.\n\n" +
                                    "Versión 1.0 · Laboratorio 09 de Programación en Móviles (Tecsup).\n\n" +
                                    "Las publicaciones de otros autores son datos de práctica de un servicio web público."
                            )
                        },
                        confirmButton = { TextButton(onClick = { acercaDe = false }) { Text("Entendido") } }
                    )
                }

                val id = idSeleccionado
                val cerrarMenuY: (() -> Unit) -> Unit = { accion ->
                    alcance.launch { menu.close() }
                    accion()
                }

                ModalNavigationDrawer(
                    drawerState = menu,
                    gesturesEnabled = id == null,
                    drawerContent = {
                        MenuLateral(
                            soloMias = soloMias,
                            creando = creando,
                            oscuro = oscuro,
                            onInicio = {
                                cerrarMenuY {
                                    creando = false
                                    vm.verSoloMias(false)
                                }
                            },
                            onMisPublicaciones = {
                                cerrarMenuY {
                                    creando = false
                                    vm.verSoloMias(true)
                                }
                            },
                            onNueva = {
                                cerrarMenuY {
                                    vm.reiniciarCreacion()
                                    creando = true
                                }
                            },
                            onCambiarTema = { temaVm.cambiar(!oscuro) },
                            onAcercaDe = { cerrarMenuY { acercaDe = true } }
                        )
                    }
                ) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(avisos) },
                        topBar = {
                            if (id == null && !creando) {
                                CabeceraOlas(onMenu = { alcance.launch { menu.open() } })
                            } else {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = colorCabecera(),
                                        titleContentColor = colorSobreCabecera(),
                                        navigationIconContentColor = colorSobreCabecera(),
                                        actionIconContentColor = colorSobreCabecera()
                                    ),
                                    navigationIcon = {
                                        if (creando) {
                                            IconButton(onClick = { alcance.launch { menu.open() } }) {
                                                Icon(Icons.Outlined.Menu, contentDescription = "Abrir menú")
                                            }
                                        } else {
                                            IconButton(onClick = { idSeleccionado = null }) {
                                                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver")
                                            }
                                        }
                                    },
                                    title = { Text(if (creando) "Nueva publicación" else "Publicación") },
                                    actions = {
                                        if (!creando) {
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
                                                Icon(Icons.Outlined.Delete, contentDescription = "Eliminar publicación")
                                            }
                                        }
                                    }
                                )
                            }
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
                                BackHandler {
                                    if (menu.isOpen) alcance.launch { menu.close() } else creando = false
                                }
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
                            else -> {
                                BackHandler(enabled = menu.isOpen) { alcance.launch { menu.close() } }
                                PantallaLista(
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
    }
}
