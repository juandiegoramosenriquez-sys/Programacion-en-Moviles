package com.ramos.murocampus.viewmodel

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    is HttpException -> if (e.code() == 404) {
        "No encontramos esta publicación.\nPuede que ya no esté disponible."
    } else {
        "El servidor no está respondiendo bien.\nInténtalo de nuevo en unos minutos."
    }
    is SerializationException -> "No pudimos leer la información recibida.\nInténtalo de nuevo más tarde."
    else -> "Algo salió mal.\nInténtalo de nuevo."
}
