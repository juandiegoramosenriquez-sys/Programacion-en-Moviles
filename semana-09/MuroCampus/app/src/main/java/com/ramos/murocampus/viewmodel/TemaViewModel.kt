package com.ramos.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramos.murocampus.data.local.PreferenciasTema
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TemaViewModel(private val preferencias: PreferenciasTema) : ViewModel() {

    val modoOscuro: StateFlow<Boolean?> = preferencias.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun cambiar(oscuro: Boolean) {
        viewModelScope.launch { preferencias.guardar(oscuro) }
    }
}
