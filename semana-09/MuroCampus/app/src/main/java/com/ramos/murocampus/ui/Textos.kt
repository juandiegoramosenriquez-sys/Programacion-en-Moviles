package com.ramos.murocampus.ui

import com.ramos.murocampus.model.Usuario

fun String.comoTitulo(): String =
    replace("\n", " ").trim().replaceFirstChar { it.uppercase() }

fun String.comoParrafo(): String {
    val texto = lines().joinToString(" ") { it.trim() }.trim().replaceFirstChar { it.uppercase() }
    return if (texto.endsWith(".")) texto else "$texto."
}

fun nombreAutor(userId: Int, autores: Map<Int, Usuario>): String =
    autores[userId]?.name ?: "Autor $userId"

fun nombreCorto(userId: Int, autores: Map<Int, Usuario>): String {
    val partes = autores[userId]?.name?.split(" ")?.filterNot { it.endsWith(".") } ?: return "Autor $userId"
    return partes.firstOrNull() ?: "Autor $userId"
}

fun iniciales(nombre: String): String =
    nombre.split(" ")
        .filter { it.isNotBlank() && !it.endsWith(".") }
        .take(2)
        .joinToString("") { it.first().uppercase() }
