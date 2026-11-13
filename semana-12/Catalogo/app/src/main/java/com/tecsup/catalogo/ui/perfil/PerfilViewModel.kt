package com.tecsup.catalogo.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.catalogo.AppContainer
import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.admin.Totales
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PerfilViewModel(
    repo: ItemRepository
) : ViewModel() {

    val estado: StateFlow<UiState<Totales>> = combine(repo.items, repo.favoritos) { items, favoritos ->
        val totales: UiState<Totales> = UiState.Exito(Totales(items.size, favoritos.size))
        totales
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Cargando)

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { PerfilViewModel(container.itemRepository) }
        }
    }
}
