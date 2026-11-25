package com.ramos.murocampus.data.remote

import com.ramos.murocampus.model.Comentario
import com.ramos.murocampus.model.EdicionPost
import com.ramos.murocampus.model.NuevoPost
import com.ramos.murocampus.model.Post
import com.ramos.murocampus.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {

    @GET("posts")
    suspend fun listar(): List<Post>

    @GET("posts")
    suspend fun pagina(@Query("_start") inicio: Int, @Query("_limit") cantidad: Int): List<Post>

    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post

    @PATCH("posts/{id}")
    suspend fun editar(@Path("id") id: Int, @Body cambios: EdicionPost): Post

    @DELETE("posts/{id}")
    suspend fun borrar(@Path("id") id: Int): Response<Unit>

    @GET("users")
    suspend fun usuarios(): List<Usuario>
}
