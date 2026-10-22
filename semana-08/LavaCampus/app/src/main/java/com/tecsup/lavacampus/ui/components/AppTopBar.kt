package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.ui.theme.degradadoElectrico
import com.tecsup.lavacampus.ui.theme.esModoOscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    onAtras: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    val oscuro = esModoOscuro()
    val colores = MaterialTheme.colorScheme
    val fondo = if (oscuro) SolidColor(colores.surfaceContainerLow) else degradadoElectrico()
    val texto = if (oscuro) colores.primary else colores.onPrimary

    Column {
        Box(Modifier.background(fondo)) {
            TopAppBar(
                title = { Text(titulo, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    if (onAtras != null) {
                        IconButton(onClick = onAtras) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                },
                actions = acciones,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = texto,
                    navigationIconContentColor = texto,
                    actionIconContentColor = texto
                )
            )
        }
        if (oscuro) {
            HorizontalDivider(thickness = 1.dp, color = colores.primary.copy(alpha = 0.45f))
        }
    }
}
