package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.model.Resultado
import com.saludplus.citas.ui.components.BarraSuperior

@Composable
fun ResultadosScreen(
    onBack: () -> Unit
) {
    val resultados = listOf(
        Resultado("Hemograma", "2026-10-01", "Listo"),
        Resultado("Glucosa", "2026-10-03", "Listo"),
        Resultado("Rayos X", "2026-10-05", "En proceso")
    )

    Scaffold(
        topBar = { BarraSuperior(titulo = "Resultados", onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(resultados) { resultado ->
                val colorEstado = if (resultado.estado == "Listo") Color(0xFF2E7D32) else Color(0xFFEF6C00)

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(resultado.examen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("📅 ${resultado.fecha}", color = Color.Gray)
                        Text(resultado.estado, color = colorEstado, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}