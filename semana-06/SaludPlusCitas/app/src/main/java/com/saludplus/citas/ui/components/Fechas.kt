package com.saludplus.citas.ui.components

import java.time.DayOfWeek
import java.time.LocalDate

private val MESES = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"
)

private val DIAS = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

fun diasHabiles(desde: LocalDate, cantidad: Int): List<LocalDate> =
    generateSequence(desde) { it.plusDays(1) }
        .filter { it.dayOfWeek != DayOfWeek.SATURDAY && it.dayOfWeek != DayOfWeek.SUNDAY }
        .take(cantidad)
        .toList()

fun mesYAnio(fecha: LocalDate): String = "${MESES[fecha.monthValue - 1]} ${fecha.year}"

fun diaCorto(fecha: LocalDate): String = DIAS[fecha.dayOfWeek.value - 1]
