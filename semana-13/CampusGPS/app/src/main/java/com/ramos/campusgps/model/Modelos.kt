package com.ramos.campusgps.model

data class Posicion(val latitud: Double, val longitud: Double)

data class Lugar(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val latitud: Double,
    val longitud: Double
)

// metros = null mientras todavía no se conoce la ubicación del usuario
data class LugarConDistancia(val lugar: Lugar, val metros: Float?)

sealed interface UbicacionUi {
    data object Inicial : UbicacionUi
    data object Buscando : UbicacionUi
    data class Lista(val posicion: Posicion) : UbicacionUi
    data class Error(val mensaje: String) : UbicacionUi
}
