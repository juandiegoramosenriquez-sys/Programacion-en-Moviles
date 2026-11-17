package com.ramos.centrotareas.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "historial")

class HistorialRepositoryDataStore(private val context: Context) : HistorialRepository {
    private val clave = stringPreferencesKey("resultados")

    override val historial: Flow<List<String>> = context.dataStore.data.map { preferencias ->
        preferencias[clave]?.split(SEPARADOR)?.filter { it.isNotBlank() } ?: emptyList()
    }

    override suspend fun agregar(resultado: String) {
        context.dataStore.edit { preferencias ->
            val actuales = preferencias[clave]?.split(SEPARADOR)?.filter { it.isNotBlank() } ?: emptyList()
            preferencias[clave] = (listOf(resultado) + actuales).take(MAXIMO).joinToString(SEPARADOR)
        }
    }

    companion object {
        private const val SEPARADOR = "|"
        private const val MAXIMO = 5
    }
}
