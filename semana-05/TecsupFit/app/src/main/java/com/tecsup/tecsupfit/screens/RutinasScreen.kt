package com.tecsup.tecsupfit.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Rutinas") }) }
    ) { padding ->
        Text(
            text = "Aquí encontrarás tus rutinas de entrenamiento.",
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
        )
    }
}
