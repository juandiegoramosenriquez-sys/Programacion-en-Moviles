package com.ramos.centrotareas.model

sealed interface PrimosUi {
    data object Inicial : PrimosUi
    data class Calculando(val porcentaje: Float = 0f, val parcial: Int = 0) : PrimosUi
    data class Listo(val cantidad: Int, val milisegundos: Long) : PrimosUi
    data object Cancelado : PrimosUi
}

sealed interface CargaUi {
    data object Inicial : CargaUi
    data object Cargando : CargaUi
    data class Exito(
        val modo: String,
        val perfil: String,
        val pedidos: Int,
        val milisegundos: Long
    ) : CargaUi
    data class Error(val mensaje: String) : CargaUi
}

data class AvancePrimos(val porcentaje: Float, val cantidad: Int, val terminado: Boolean)

data class ComparacionTiempos(
    val unaPorUna: List<Long> = emptyList(),
    val aLaVez: List<Long> = emptyList()
) {
    val promedioUnaPorUna: Long? get() = unaPorUna.takeIf { it.isNotEmpty() }?.average()?.toLong()
    val promedioALaVez: Long? get() = aLaVez.takeIf { it.isNotEmpty() }?.average()?.toLong()

    val ahorroMs: Long?
        get() {
            val lento = promedioUnaPorUna ?: return null
            val rapido = promedioALaVez ?: return null
            return lento - rapido
        }

    val ahorroPorcentaje: Int?
        get() {
            val lento = promedioUnaPorUna ?: return null
            val ahorro = ahorroMs ?: return null
            return if (lento > 0) (ahorro * 100 / lento).toInt() else null
        }
}
