package com.ramos.lab03registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ramos.lab03registronotas.ui.theme.Lab03RegistroNotasTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistroNotasTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Registro de Notas") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = Color.White
                            )
                        )
                    }
                ) { innerPadding ->
                    PantallaNotas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class Curso(val nombre: String, val peso: Float)

val listaCursos = listOf(
    Curso("Fundamentos de Programación", 0.20f),
    Curso("Programación Orientada a Objetos", 0.25f),
    Curso("Programación en Móviles", 0.30f),
    Curso("Base de Datos", 0.25f)
)

@Composable
fun PantallaNotas(modifier: Modifier = Modifier) {
    var nota1 by rememberSaveable { mutableStateOf(0f) }
    var nota2 by rememberSaveable { mutableStateOf(0f) }
    var nota3 by rememberSaveable { mutableStateOf(0f) }
    var nota4 by rememberSaveable { mutableStateOf(0f) }

    var redondear by rememberSaveable { mutableStateOf(false) }
    var confirmado by rememberSaveable { mutableStateOf(false) }
    var mostrarResultado by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Notas del ciclo",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Desliza para asignar cada nota (0 a 20)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        FilaCurso(listaCursos[0], nota1) { nota1 = it; mostrarResultado = false }
        FilaCurso(listaCursos[1], nota2) { nota2 = it; mostrarResultado = false }
        FilaCurso(listaCursos[2], nota3) { nota3 = it; mostrarResultado = false }
        FilaCurso(listaCursos[3], nota4) { nota4 = it; mostrarResultado = false }

        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Redondear promedio final", modifier = Modifier.weight(1f))
            Switch(
                checked = redondear,
                onCheckedChange = { redondear = it; mostrarResultado = false }
            )
        }


        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = confirmado,
                onCheckedChange = { confirmado = it; mostrarResultado = false }
            )
            Text("Confirmo que las notas son correctas")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { mostrarResultado = true },
            enabled = confirmado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CALCULAR PROMEDIO")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!mostrarResultado) {
            Text(
                text = "Asigna las notas y confirma para calcular",
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            val ponderado = nota1 * listaCursos[0].peso + nota2 * listaCursos[1].peso +
                    nota3 * listaCursos[2].peso + nota4 * listaCursos[3].peso

            val finalRedondeado = ponderado.roundToInt()
            val promedioFinal = if (redondear) finalRedondeado.toFloat() else ponderado

            val (observacion, colorChip) = when {
                promedioFinal >= 17f -> "EXCELENTE" to Color(0xFF1B5E20)
                promedioFinal >= 13f -> "APROBADO" to Color(0xFF4CAF50)
                promedioFinal >= 10f -> "EN RECUPERACIÓN" to Color(0xFFFFA000)
                else -> "DESAPROBADO" to Color(0xFFD32F2F)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Promedio ponderado: " + String.format("%.2f", ponderado))
                    Text(
                        text = "Promedio final: " +
                                if (redondear) finalRedondeado.toString()
                                else String.format("%.2f", promedioFinal),
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (redondear) {
                        Text(
                            "(redondeado)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorChip)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(observacion, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "✓ Promedio calculado correctamente",
                color = Color(0xFF2E7D32)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Juan Diego Ramos Enriquez",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun FilaCurso(curso: Curso, nota: Float, onNotaChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = curso.nombre + " (" + (curso.peso * 100).toInt() + "%)",
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(nota.toInt().toString(), color = Color.White)
            }
        }
        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19
        )
    }
}