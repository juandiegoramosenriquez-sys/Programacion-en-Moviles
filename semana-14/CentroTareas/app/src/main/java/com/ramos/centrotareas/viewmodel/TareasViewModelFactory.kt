package com.ramos.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramos.centrotareas.data.TareasRepositoryLocal

class TareasViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TareasViewModel(TareasRepositoryLocal()) as T
}
