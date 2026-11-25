package com.ramos.murocampus.data

import com.ramos.murocampus.data.remote.PostApi
import com.ramos.murocampus.model.Comentario
import com.ramos.murocampus.model.EdicionPost
import com.ramos.murocampus.model.NuevoPost
import com.ramos.murocampus.model.Post
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun editar(id: Int, titulo: String): Result<Post>
    suspend fun borrar(id: Int): Result<Unit>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    override suspend fun listar(): Result<List<Post>> = llamar { api.listar() }

    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> = llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> = llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }

    override suspend fun editar(id: Int, titulo: String): Result<Post> =
        llamar { api.editar(id, EdicionPost(title = titulo)) }

    override suspend fun borrar(id: Int): Result<Unit> = llamar {
        val respuesta = api.borrar(id)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}
