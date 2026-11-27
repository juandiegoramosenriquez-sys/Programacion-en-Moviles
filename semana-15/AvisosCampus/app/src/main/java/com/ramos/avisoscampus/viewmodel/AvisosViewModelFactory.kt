package com.ramos.avisoscampus.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramos.avisoscampus.data.AvisosRepositoryRoom
import com.ramos.avisoscampus.data.local.AvisosDatabase
import com.ramos.avisoscampus.receiver.SolicitudesPosponer

class AvisosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AvisosDatabase.obtener(context).avisoDao()
        return AvisosViewModel(AvisosRepositoryRoom(dao), SolicitudesPosponer.solicitudes) as T
    }
}
