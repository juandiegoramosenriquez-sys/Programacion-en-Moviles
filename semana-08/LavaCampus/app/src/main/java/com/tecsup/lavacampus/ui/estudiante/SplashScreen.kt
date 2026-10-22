package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.theme.degradadoElectrico
import com.tecsup.lavacampus.ui.theme.textoSobreDestacado
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTerminar: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onTerminar()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(degradadoElectrico()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconoServicio("wash", tamano = 96.dp)
            Spacer(Modifier.height(16.dp))
            Text("LavaCampus", style = MaterialTheme.typography.headlineSmall, color = textoSobreDestacado())
            Text(
                "Lavandería universitaria",
                style = MaterialTheme.typography.bodyMedium,
                color = textoSobreDestacado().copy(alpha = 0.8f)
            )
        }
    }
}
