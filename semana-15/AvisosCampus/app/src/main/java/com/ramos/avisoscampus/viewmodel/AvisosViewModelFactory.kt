package com.ramos.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramos.avisoscampus.data.AvisosRepositoryMemoria
import com.ramos.avisoscampus.receiver.SolicitudesPosponer

class AvisosViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AvisosViewModel(AvisosRepositoryMemoria(), SolicitudesPosponer.solicitudes) as T
    }
}
