package com.ramos.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "campus_prefs")

class PreferenciasRepositoryDataStore(private val context: Context) : PreferenciasRepository {

    private val claveUltimoLugar = intPreferencesKey("ultimo_lugar")
    private val claveModoOscuro = booleanPreferencesKey("modo_oscuro")

    override val ultimoLugar: Flow<Int?> =
        context.dataStore.data.map { it[claveUltimoLugar] }

    override suspend fun guardarUltimoLugar(id: Int) {
        context.dataStore.edit { it[claveUltimoLugar] = id }
    }

    override val modoOscuro: Flow<Boolean?> =
        context.dataStore.data.map { it[claveModoOscuro] }

    override suspend fun guardarModoOscuro(oscuro: Boolean) {
        context.dataStore.edit { it[claveModoOscuro] = oscuro }
    }
}
