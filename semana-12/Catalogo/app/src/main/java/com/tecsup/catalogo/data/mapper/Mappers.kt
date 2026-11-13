package com.tecsup.catalogo.data.mapper

import com.tecsup.catalogo.data.local.ItemEntity
import com.tecsup.catalogo.data.remote.dto.ItemDto
import com.tecsup.catalogo.domain.model.Item
import java.util.Locale

const val TIPO_DE_CAMBIO = 3.75

fun enSoles(dolares: Double): String = String.format(Locale.US, "S/ %.2f", dolares * TIPO_DE_CAMBIO)

private fun ItemDto.subtitulo(): String = "${brand ?: "Sin marca"} · ${enSoles(price)}"

fun ItemDto.aModelo(esFavorito: Boolean = false): Item = Item(
    id = id,
    titulo = title,
    subtitulo = subtitulo(),
    imagenUrl = thumbnail,
    esFavorito = esFavorito,
    descripcion = description,
    rating = rating,
    stock = stock
)

fun ItemDto.aEntity(esFavorito: Boolean = false): ItemEntity = ItemEntity(
    id = id,
    titulo = title,
    subtitulo = subtitulo(),
    imagenUrl = thumbnail,
    descripcion = description,
    rating = rating,
    stock = stock,
    esFavorito = esFavorito
)

fun ItemEntity.aModelo(): Item = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito,
    descripcion = descripcion,
    rating = rating,
    stock = stock
)
