package com.tecsup.catalogo.data.remote

import com.tecsup.catalogo.data.remote.dto.ItemDto
import com.tecsup.catalogo.data.remote.dto.ProductosRespuesta
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("products/category/smartphones")
    suspend fun listar(): ProductosRespuesta

    @GET("products/{id}")
    suspend fun detalle(@Path("id") id: Int): ItemDto
}
