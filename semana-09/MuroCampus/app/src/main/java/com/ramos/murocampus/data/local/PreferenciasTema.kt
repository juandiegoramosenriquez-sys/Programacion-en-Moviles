package com.ramos.murocampus.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.almacenTema by preferencesDataStore(name = "tema")

class PreferenciasTema(private val context: Context) {

    private val claveOscuro = booleanPreferencesKey("modo_oscuro")

    val modoOscuro: Flow<Boolean?> = context.almacenTema.data.map { it[claveOscuro] }

    suspend fun guardar(oscuro: Boolean) {
        context.almacenTema.edit { it[claveOscuro] = oscuro }
    }
}
