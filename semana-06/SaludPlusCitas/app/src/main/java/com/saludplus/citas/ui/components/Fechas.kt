package com.saludplus.citas.ui.components

import java.time.DayOfWeek
import java.time.LocalDate

private val MESES = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"
)

private val DIAS = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

private val DIAS_LARGOS = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

fun diasHabiles(desde: LocalDate, cantidad: Int): List<LocalDate> =
    generateSequence(desde) { it.plusDays(1) }
        .filter { it.dayOfWeek != DayOfWeek.SATURDAY && it.dayOfWeek != DayOfWeek.SUNDAY }
        .take(cantidad)
        .toList()

fun mesYAnio(fecha: LocalDate): String = "${MESES[fecha.monthValue - 1]} ${fecha.year}"

fun diaCorto(fecha: LocalDate): String = DIAS[fecha.dayOfWeek.value - 1]

fun fechaEnTexto(fecha: String): String {
    val dia = runCatching { LocalDate.parse(fecha) }.getOrNull() ?: return fecha
    val nombreDia = DIAS_LARGOS[dia.dayOfWeek.value - 1]
    val mes = MESES[dia.monthValue - 1].lowercase()
    return "$nombreDia ${dia.dayOfMonth} de $mes ${dia.year}"
}
