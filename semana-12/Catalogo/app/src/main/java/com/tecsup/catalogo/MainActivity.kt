package com.tecsup.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.lista.ListaViewModel
import com.tecsup.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {

    private val container by lazy { AppContainer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogoTheme {
                val listaViewModel: ListaViewModel = viewModel(factory = ListaViewModel.factory(container))
                ListaScreen(listaViewModel)
            }
        }
    }
}
