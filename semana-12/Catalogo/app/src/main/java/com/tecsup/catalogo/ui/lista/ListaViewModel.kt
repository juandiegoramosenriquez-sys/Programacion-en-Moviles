package com.tecsup.catalogo.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaViewModel : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val productosDePrueba = listOf(
        Item(121, "iPhone 5s", "Apple · $199.99", null),
        Item(122, "iPhone 6", "Apple · $299.99", null),
        Item(123, "iPhone 13 Pro", "Apple · $1099.99", null),
        Item(124, "iPhone X", "Apple · $899.99", null),
        Item(125, "Oppo A57", "Oppo · $249.99", null),
        Item(126, "Oppo F19 Pro Plus", "Oppo · $399.99", null),
        Item(127, "Realme C35", "Realme · $149.99", null),
        Item(128, "Samsung Galaxy S8", "Samsung · $499.99", null),
        Item(129, "Samsung Galaxy S10", "Samsung · $699.99", null),
        Item(130, "Vivo X21", "Vivo · $499.99", null)
    )

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            delay(1000)
            _estado.value = UiState.Exito(productosDePrueba)
        }
    }
}
