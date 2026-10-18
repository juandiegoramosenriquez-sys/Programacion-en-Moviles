package com.tecsup.lavacampus.ui.components

import java.util.Locale

fun formatoSoles(monto: Double): String = String.format(Locale.US, "S/ %.2f", monto)
