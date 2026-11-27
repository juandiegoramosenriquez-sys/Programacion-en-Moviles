package com.ramos.avisoscampus.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class Pastel(val fondo: Color, val texto: Color)

@Immutable
data class Paleta(
    val fondo: Color,
    val tarjeta: Color,
    val borde: Color,
    val texto: Color,
    val textoSecundario: Color,
    val boton: Color,
    val textoBoton: Color,
    val portada: Color,
    val textoPortada: Color,
    val arena: Color,
    val morado: Color,
    val verde: Color,
    val rojo: Color,
    val etiquetaBoton: Pastel,
    val etiquetaRecordatorio: Pastel,
    val etiquetaAvion: Pastel,
    val etiquetaCargador: Pastel,
    val etiquetaError: Pastel,
    val etiquetaNeutra: Pastel
)

val PaletaClara = Paleta(
    fondo = Color(0xFFF6F5F3),
    tarjeta = Color(0xFFFFFFFF),
    borde = Color(0xFFE8E5E0),
    texto = Color(0xFF141414),
    textoSecundario = Color(0xFF66625D),
    boton = Color(0xFF111111),
    textoBoton = Color(0xFFFFFFFF),
    portada = Color(0xFF1C1815),
    textoPortada = Color(0xFFF6F2EC),
    arena = Color(0xFFEFE8DD),
    morado = Color(0xFF4A2C75),
    verde = Color(0xFF2F6B52),
    rojo = Color(0xFF8E241E),
    etiquetaBoton = Pastel(Color(0xFFEDE6F6), Color(0xFF4A2C75)),
    etiquetaRecordatorio = Pastel(Color(0xFFEFE8DD), Color(0xFF5C4630)),
    etiquetaAvion = Pastel(Color(0xFFE2EFE7), Color(0xFF1F5240)),
    etiquetaCargador = Pastel(Color(0xFFECEBE8), Color(0xFF3D3A36)),
    etiquetaError = Pastel(Color(0xFFF7E4E2), Color(0xFF8E241E)),
    etiquetaNeutra = Pastel(Color(0xFFECEBE8), Color(0xFF66625D))
)

val PaletaOscura = Paleta(
    fondo = Color(0xFF0F0F0F),
    tarjeta = Color(0xFF1A1918),
    borde = Color(0xFF2E2C29),
    texto = Color(0xFFF2F0ED),
    textoSecundario = Color(0xFFA8A39C),
    boton = Color(0xFFF2F0ED),
    textoBoton = Color(0xFF111111),
    portada = Color(0xFF2A231D),
    textoPortada = Color(0xFFF2F0ED),
    arena = Color(0xFF3A322A),
    morado = Color(0xFFC9B6EA),
    verde = Color(0xFF5FA584),
    rojo = Color(0xFFE8A39C),
    etiquetaBoton = Pastel(Color(0xFF2E2440), Color(0xFFD9CBF2)),
    etiquetaRecordatorio = Pastel(Color(0xFF3A322A), Color(0xFFE6D5BD)),
    etiquetaAvion = Pastel(Color(0xFF1C3329), Color(0xFFA9D8BF)),
    etiquetaCargador = Pastel(Color(0xFF2B2927), Color(0xFFD6D2CC)),
    etiquetaError = Pastel(Color(0xFF3D1F1C), Color(0xFFF0B8B0)),
    etiquetaNeutra = Pastel(Color(0xFF2B2927), Color(0xFFA8A39C))
)
