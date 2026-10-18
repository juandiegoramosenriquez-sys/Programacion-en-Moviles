package com.tecsup.lavacampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tecsup.lavacampus.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("ajustes")

class PreferenciasStore(private val context: Context) {

    private val perfilKey = stringPreferencesKey("perfilActivo")
    private val usuarioKey = intPreferencesKey("usuarioId")
    private val oscuroKey = booleanPreferencesKey("modoOscuro")
    private val notificacionesKey = booleanPreferencesKey("notificaciones")
    private val recibirKey = booleanPreferencesKey("recibirPedidos")

    val perfilActivo: Flow<String> = context.dataStore.data.map { it[perfilKey] ?: "" }
    val usuarioId: Flow<Int> = context.dataStore.data.map { it[usuarioKey] ?: 0 }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[oscuroKey] ?: false }
    val notificaciones: Flow<Boolean> = context.dataStore.data.map { it[notificacionesKey] ?: true }
    val recibirPedidos: Flow<Boolean> = context.dataStore.data.map { it[recibirKey] ?: true }

    suspend fun guardarSesion(rol: Rol, usuarioId: Int) {
        context.dataStore.edit {
            it[perfilKey] = rol.name
            it[usuarioKey] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(activo: Boolean) {
        context.dataStore.edit { it[oscuroKey] = activo }
    }

    suspend fun guardarNotificaciones(activo: Boolean) {
        context.dataStore.edit { it[notificacionesKey] = activo }
    }

    suspend fun guardarRecibirPedidos(activo: Boolean) {
        context.dataStore.edit { it[recibirKey] = activo }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit {
            it.remove(perfilKey)
            it.remove(usuarioKey)
        }
    }
}
