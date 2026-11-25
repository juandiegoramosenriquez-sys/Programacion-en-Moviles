package com.ramos.murocampus.data

import com.ramos.murocampus.data.local.PostDao
import com.ramos.murocampus.data.local.PostEntity
import com.ramos.murocampus.data.local.UsuarioDao
import com.ramos.murocampus.data.local.aEntity
import com.ramos.murocampus.data.local.aPost
import com.ramos.murocampus.data.local.aUsuario
import com.ramos.murocampus.data.remote.PostApi
import com.ramos.murocampus.model.Comentario
import com.ramos.murocampus.model.EdicionPost
import com.ramos.murocampus.model.Listado
import com.ramos.murocampus.model.NuevoPost
import com.ramos.murocampus.model.Post
import com.ramos.murocampus.model.Usuario
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
    suspend fun usuarios(): Result<List<Usuario>>
    suspend fun usuariosGuardados(): List<Usuario>
}

class PostRepositoryRemote(
    private val api: PostApi,
    private val dao: PostDao,
    private val usuarioDao: UsuarioDao
) : PostRepository {

    override suspend fun usuariosGuardados(): List<Usuario> =
        llamar { usuarioDao.todos() }.getOrDefault(emptyList()).map { it.aUsuario() }

    override suspend fun usuarios(): Result<List<Usuario>> {
        val red = llamar { api.usuarios() }
        val usuarios = red.getOrNull()
        if (usuarios != null) {
            llamar { usuarioDao.guardar(usuarios.map { it.aEntity() }) }
            return Result.success(usuarios)
        }
        val guardados = llamar { usuarioDao.todos() }.getOrDefault(emptyList())
        return if (guardados.isNotEmpty()) {
            Result.success(guardados.map { it.aUsuario() })
        } else {
            Result.failure(red.exceptionOrNull() ?: IllegalStateException("Sin usuarios"))
        }
    }

    override suspend fun listar(): Result<Listado> =
        conPropios(
            listado = conCache(remoto = { api.listar() }, local = { dao.todos() }),
            propios = { dao.propios() }
        )

    override suspend fun pagina(inicio: Int, cantidad: Int): Result<Listado> {
        val listado = conCache(
            remoto = { api.pagina(inicio, cantidad) },
            local = { if (inicio == 0) dao.todos() else emptyList() }
        )
        return if (inicio == 0) conPropios(listado, propios = { dao.propios() }) else listado
    }

    override suspend fun porUsuario(userId: Int): Result<Listado> =
        conPropios(
            listado = conCache(remoto = { api.porUsuario(userId) }, local = { dao.porUsuario(userId) }),
            propios = { dao.propiosDe(userId) }
        )

    override suspend fun detalle(id: Int): Result<Post> {
        val local = llamar { dao.porId(id) }.getOrNull()
        if (local != null && local.propio) return Result.success(local.aPost())
        return llamar { api.detalle(id) }
    }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> {
        val local = llamar { dao.porId(id) }.getOrNull()
        if (local != null && local.propio) return Result.success(emptyList())
        return llamar { api.comentarios(id) }
    }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar {
        val respuesta = api.crear(nuevo)
        val idLocal = maxOf((dao.mayorId() ?: 0) + 1, respuesta.id)
        val propio = Post(
            userId = nuevo.userId,
            id = idLocal,
            title = respuesta.title,
            body = respuesta.body,
            propio = true
        )
        dao.guardar(listOf(propio.aEntity()))
        propio
    }

    override suspend fun editar(id: Int, titulo: String): Result<Post> = llamar {
        val local = dao.porId(id)
        if (local != null && local.propio) {
            dao.cambiarTitulo(id, titulo)
            local.aPost().copy(title = titulo)
        } else {
            val respuesta = api.editar(id, EdicionPost(title = titulo))
            dao.cambiarTitulo(id, respuesta.title)
            respuesta
        }
    }

    override suspend fun borrar(id: Int): Result<Unit> = llamar {
        val local = dao.porId(id)
        if (local == null || !local.propio) {
            val respuesta = api.borrar(id)
            if (!respuesta.isSuccessful) throw HttpException(respuesta)
        }
        dao.borrar(id)
    }

    private suspend fun conPropios(
        listado: Result<Listado>,
        propios: suspend () -> List<PostEntity>
    ): Result<Listado> {
        val mios = llamar { propios() }.getOrDefault(emptyList()).map { it.aPost() }
        if (mios.isEmpty()) return listado
        val datos = listado.getOrNull()
        return if (datos != null) {
            Result.success(datos.copy(posts = mios + datos.posts))
        } else {
            Result.success(Listado(mios, desdeCache = true))
        }
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
