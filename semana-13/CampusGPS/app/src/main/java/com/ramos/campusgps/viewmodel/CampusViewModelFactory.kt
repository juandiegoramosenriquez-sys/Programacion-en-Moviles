package com.ramos.campusgps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramos.campusgps.data.LugaresRepositoryLocal
import com.ramos.campusgps.data.PreferenciasRepositoryDataStore
import com.ramos.campusgps.data.UbicacionRepositoryGoogle

class CampusViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MapaViewModel(
            UbicacionRepositoryGoogle(context.applicationContext),
            LugaresRepositoryLocal(),
            PreferenciasRepositoryDataStore(context.applicationContext)
        ) as T
}
