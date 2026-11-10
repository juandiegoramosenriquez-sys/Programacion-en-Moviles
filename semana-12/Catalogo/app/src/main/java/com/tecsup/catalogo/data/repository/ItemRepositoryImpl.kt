package com.tecsup.catalogo.data.repository

import com.tecsup.catalogo.data.mapper.aModelo
import com.tecsup.catalogo.data.remote.ApiService
import com.tecsup.catalogo.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService
) : ItemRepository {

    private val guardados = MutableStateFlow<List<Item>>(emptyList())

    override val items: Flow<List<Item>> = guardados

    override val favoritos: Flow<List<Item>> = guardados.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> = try {
        val idsFavoritos = guardados.value.filter { it.esFavorito }.map { it.id }.toSet()
        val nuevos = api.listar().products.map { it.aModelo(esFavorito = it.id in idsFavoritos) }
        guardados.value = nuevos
        Result.success(nuevos.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val esFavorito = guardados.value.any { it.id == id && it.esFavorito }
        Result.success(api.detalle(id).aModelo(esFavorito))
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        guardados.value = guardados.value.map {
            if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
        }
    }

    override suspend fun cantidadGuardada(): Int = guardados.value.size

    override suspend fun cantidadFavoritos(): Int = guardados.value.count { it.esFavorito }

    override suspend fun limpiarCache() {
        guardados.value = emptyList()
    }
}
