package com.ramos.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramos.murocampus.data.PostRepository
import com.ramos.murocampus.model.Comentario
import com.ramos.murocampus.model.NuevoPost
import com.ramos.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)

    private val _sinConexion = MutableStateFlow(false)
    val sinConexion: StateFlow<Boolean> = _sinConexion.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    val lista: StateFlow<UiState<List<Post>>> = combine(_lista, _busqueda) { estado, texto ->
        if (estado is UiState.Exito && texto.isNotBlank()) {
            UiState.Exito(estado.datos.filter { it.title.contains(texto.trim(), ignoreCase = true) })
        } else {
            estado
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private val _creado = MutableStateFlow<UiState<Post>?>(null)
    val creado: StateFlow<UiState<Post>?> = _creado.asStateFlow()

    private val _borrado = MutableStateFlow<UiState<Int>?>(null)
    val borrado: StateFlow<UiState<Int>?> = _borrado.asStateFlow()

    private val _editado = MutableStateFlow<UiState<Post>?>(null)
    val editado: StateFlow<UiState<Post>?> = _editado.asStateFlow()

    private var trabajoLista: Job? = null
    private var trabajoDetalle: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        val userId = _usuarioFiltro.value
        trabajoLista?.cancel()
        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val resultado = if (userId == null) repo.listar() else repo.porUsuario(userId)
            resultado
                .onSuccess {
                    _lista.value = UiState.Exito(it.posts)
                    _sinConexion.value = it.desdeCache
                }
                .onFailure {
                    _lista.value = UiState.Error(mensajeDe(it))
                    _sinConexion.value = false
                }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _creado.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _creado.value = UiState.Exito(it) }
                .onFailure { _creado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun reiniciarCreacion() {
        _creado.value = null
    }

    fun borrar(id: Int) {
        viewModelScope.launch {
            _borrado.value = UiState.Cargando
            repo.borrar(id)
                .onSuccess {
                    val actual = _lista.value
                    if (actual is UiState.Exito) {
                        _lista.value = UiState.Exito(actual.datos.filterNot { it.id == id })
                    }
                    _borrado.value = UiState.Exito(id)
                }
                .onFailure { _borrado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun editar(id: Int, titulo: String) {
        viewModelScope.launch {
            _editado.value = UiState.Cargando
            repo.editar(id, titulo)
                .onSuccess { respuesta ->
                    val detalleActual = _detalle.value
                    if (detalleActual is UiState.Exito && detalleActual.datos.post.id == id) {
                        _detalle.value = UiState.Exito(
                            detalleActual.datos.copy(post = detalleActual.datos.post.copy(title = respuesta.title))
                        )
                    }
                    val listaActual = _lista.value
                    if (listaActual is UiState.Exito) {
                        _lista.value = UiState.Exito(
                            listaActual.datos.map { if (it.id == id) it.copy(title = respuesta.title) else it }
                        )
                    }
                    _editado.value = UiState.Exito(respuesta)
                }
                .onFailure { _editado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun reiniciarBorrado() {
        _borrado.value = null
    }

    fun abrir(id: Int) {
        _borrado.value = null
        _editado.value = null
        trabajoDetalle?.cancel()
        trabajoDetalle = viewModelScope.launch {
            _detalle.value = UiState.Cargando
            val (resPost, resComentarios) = coroutineScope {
                val post = async { repo.detalle(id) }
                val comentarios = async { repo.comentarios(id) }
                post.await() to comentarios.await()
            }
            val post = resPost.getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it))
                return@launch
            }
            val comentarios = resComentarios.getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it))
                return@launch
            }
            _detalle.value = UiState.Exito(DetalleUi(post, comentarios))
        }
    }
}
