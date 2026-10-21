package com.tecsup.lavacampus.ui.components

import com.tecsup.lavacampus.model.Unidad
import java.util.Locale

fun formatoSoles(monto: Double): String = String.format(Locale.US, "S/ %.2f", monto)

fun textoCantidad(unidad: String, cantidad: Double): String =
    if (unidad == Unidad.KG.name) String.format(Locale.US, "%.1f kg", cantidad)
    else "${cantidad.toInt()} ${unidad.lowercase()}"
