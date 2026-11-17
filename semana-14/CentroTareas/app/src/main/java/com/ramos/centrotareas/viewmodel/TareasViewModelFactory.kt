package com.ramos.centrotareas.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramos.centrotareas.data.HistorialRepositoryDataStore
import com.ramos.centrotareas.data.TareasRepositoryLocal

class TareasViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TareasViewModel(
            TareasRepositoryLocal(),
            HistorialRepositoryDataStore(context.applicationContext)
        ) as T
}
