package com.tecsup.catalogo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.tecsup.catalogo.ui.theme.Medianoche
import com.tecsup.catalogo.ui.theme.MedianocheClaro
import com.tecsup.catalogo.ui.theme.TextoEncabezado
import com.tecsup.catalogo.ui.theme.TextoEncabezadoSuave

@Composable
fun Encabezado(
    modifier: Modifier = Modifier,
    alineacion: Alignment.Horizontal = Alignment.Start,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Medianoche)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalAlignment = alineacion,
        content = contenido
    )
}

@Composable
fun TituloEncabezado(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = TextoEncabezado
    )
}

@Composable
fun DatoEncabezado(
    valor: String,
    etiqueta: String,
    modifier: Modifier = Modifier,
    colorValor: Color = TextoEncabezado
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MedianocheClaro)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(valor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = colorValor)
        Text(etiqueta, style = MaterialTheme.typography.bodySmall, color = TextoEncabezadoSuave)
    }
}
