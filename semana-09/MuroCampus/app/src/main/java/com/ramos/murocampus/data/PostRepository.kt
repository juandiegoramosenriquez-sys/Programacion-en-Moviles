package com.ramos.murocampus.data

import com.ramos.murocampus.data.local.PostDao
import com.ramos.murocampus.data.local.PostEntity
import com.ramos.murocampus.data.local.aEntity
import com.ramos.murocampus.data.local.aPost
import com.ramos.murocampus.data.remote.PostApi
import com.ramos.murocampus.model.Comentario
import com.ramos.murocampus.model.EdicionPost
import com.ramos.murocampus.model.Listado
import com.ramos.murocampus.model.NuevoPost
import com.ramos.murocampus.model.Post
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

interface PostRepository {
    suspend fun listar(): Result<Listado>
    suspend fun pagina(inicio: Int, cantidad: Int): Result<Listado>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<Listado>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun editar(id: Int, titulo: String): Result<Post>
    suspend fun borrar(id: Int): Result<Unit>
}

class PostRepositoryRemote(
    private val api: PostApi,
    private val dao: PostDao
) : PostRepository {

    override suspend fun listar(): Result<Listado> =
        conCache(remoto = { api.listar() }, local = { dao.todos() })

    override suspend fun pagina(inicio: Int, cantidad: Int): Result<Listado> =
        conCache(
            remoto = { api.pagina(inicio, cantidad) },
            local = { if (inicio == 0) dao.todos() else emptyList() }
        )

    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> = llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<Listado> =
        conCache(remoto = { api.porUsuario(userId) }, local = { dao.porUsuario(userId) })

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }

    override suspend fun editar(id: Int, titulo: String): Result<Post> =
        llamar { api.editar(id, EdicionPost(title = titulo)) }

    override suspend fun borrar(id: Int): Result<Unit> = llamar {
        val respuesta = api.borrar(id)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }

    private suspend fun conCache(
        remoto: suspend () -> List<Post>,
        local: suspend () -> List<PostEntity>
    ): Result<Listado> {
        val red = llamar { remoto() }
        val posts = red.getOrNull()
        if (posts != null) {
            llamar { dao.guardar(posts.map { it.aEntity() }) }
            return Result.success(Listado(posts, desdeCache = false))
        }
        val guardadas = llamar { local() }.getOrDefault(emptyList())
        return if (guardadas.isNotEmpty()) {
            Result.success(Listado(guardadas.map { it.aPost() }, desdeCache = true))
        } else {
            Result.failure(red.exceptionOrNull() ?: IllegalStateException("Sin datos"))
        }
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
