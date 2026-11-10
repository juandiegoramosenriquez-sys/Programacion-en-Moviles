package com.tecsup.catalogo.data.mapper

import com.tecsup.catalogo.data.remote.dto.ItemDto
import com.tecsup.catalogo.domain.model.Item

fun ItemDto.aModelo(esFavorito: Boolean = false): Item = Item(
    id = id,
    titulo = title,
    subtitulo = "${brand ?: "Sin marca"} · \$$price",
    imagenUrl = thumbnail,
    esFavorito = esFavorito
)
