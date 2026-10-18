package com.tecsup.lavacampus.model

object Casilleros {
    val TODOS: List<String> = (1..30).map { "C-" + it.toString().padStart(2, '0') }

    fun esValido(casillero: String): Boolean = casillero in TODOS
}
