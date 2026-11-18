package com.ramos.campusgps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramos.campusgps.ui.theme.TextoCabecera

@Composable
fun SelectorRadio(
    radios: List<Int>,
    seleccionado: Int,
    onElegir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        radios.forEach { metros ->
            val activo = seleccionado == metros
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (activo) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { onElegir(metros) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$metros m",
                    color = if (activo) Color.White else TextoCabecera,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
