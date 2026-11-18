package com.ramos.campusgps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ramos.campusgps.ui.theme.TextoCabecera
import com.ramos.campusgps.ui.theme.Verde

@Composable
fun CabeceraPanel(
    masCercano: String,
    enRadio: String,
    ultimo: String,
    siguiendo: Boolean,
    radios: List<Int>,
    radio: Int,
    onRadio: (Int) -> Unit,
    oscuro: Boolean,
    onCambiarTema: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Campus central · Lima", color = TextoCabecera, style = MaterialTheme.typography.labelMedium)
                Text(
                    "CampusGPS",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            if (siguiendo) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Verde.copy(alpha = 0.18f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(Verde))
                    Spacer(Modifier.width(6.dp))
                    Text("EN VIVO", color = Verde, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
            IconButton(onClick = onCambiarTema) {
                Icon(
                    if (oscuro) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                    contentDescription = if (oscuro) "Cambiar a modo claro" else "Cambiar a modo oscuro",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Indicador("Más cercano", masCercano)
            Indicador("En el radio", enRadio)
            Indicador("Último", ultimo)
        }

        SelectorRadio(
            radios = radios,
            seleccionado = radio,
            onElegir = onRadio,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RowScope.Indicador(titulo: String, valor: String) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(titulo, color = TextoCabecera, style = MaterialTheme.typography.labelSmall)
        Text(
            valor,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
