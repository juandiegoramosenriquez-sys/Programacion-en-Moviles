package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun TerminosScreen(
    onBack: () -> Unit
) {
    val terminos = listOf(
        "1. Uso del servicio" to "La app SaludPlus permite agendar, consultar y cancelar citas médicas en la Clínica SaludPlus.",
        "2. Datos personales" to "Tus datos (nombres, DNI, correo y celular) se usan solo para gestionar tus citas y no se comparten con terceros.",
        "3. Responsabilidad del usuario" to "Debes ingresar información real y mantener tu contraseña en privado.",
        "4. Citas" to "Puedes cancelar una cita desde la app. Si no asistes, la cita se considerará perdida.",
        "5. Cambios" to "La clínica puede actualizar estos términos. Te avisaremos si hay cambios importantes."
    )

    Scaffold(
        topBar = { BarraSuperior(titulo = "Términos y condiciones", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            terminos.forEach { (titulo, texto) ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(texto, style = MaterialTheme.typography.bodyMedium)
                }
            }

            BotonPrincipal(texto = "Entendido", onClick = onBack)
        }
    }
}