package com.ramos.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramos.murocampus.ui.PantallaCrear
import com.ramos.murocampus.ui.PantallaDetalle
import com.ramos.murocampus.ui.PantallaLista
import com.ramos.murocampus.ui.theme.MuroCampusTheme
import com.ramos.murocampus.viewmodel.PostsViewModel
import com.ramos.murocampus.viewmodel.PostsViewModelFactory
import com.ramos.murocampus.viewmodel.UiState

class MainActivity : ComponentActivity() {

    private val vm: PostsViewModel by viewModels { PostsViewModelFactory(applicationContext) }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var creando by rememberSaveable { mutableStateOf(false) }
                val borrado by vm.borrado.collectAsStateWithLifecycle()
                val avisos = remember { SnackbarHostState() }

                LaunchedEffect(borrado) {
                    val b = borrado
                    if (b is UiState.Exito) {
                        idSeleccionado = null
                        vm.reiniciarBorrado()
                        avisos.showSnackbar("Publicación ${b.datos} eliminada")
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(avisos) },
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    floatingActionButton = {
                        if (idSeleccionado == null && !creando) {
                            FloatingActionButton(
                                onClick = {
                                    vm.reiniciarCreacion()
                                    creando = true
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text("+", style = MaterialTheme.typography.headlineMedium)
                            }
                        }
                    }
                ) { padding ->
                    val contenido = Modifier
                        .fillMaxSize()
                        .padding(padding)
                    val id = idSeleccionado
                    when {
                        creando -> {
                            BackHandler { creando = false }
                            PantallaCrear(vm = vm, modifier = contenido)
                        }
                        id != null -> {
                            BackHandler { idSeleccionado = null }
                            PantallaDetalle(vm = vm, id = id, modifier = contenido)
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
