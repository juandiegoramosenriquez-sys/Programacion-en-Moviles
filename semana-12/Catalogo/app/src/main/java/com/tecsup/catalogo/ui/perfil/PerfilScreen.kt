package com.tecsup.catalogo.ui.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.catalogo.R
import com.tecsup.catalogo.domain.model.Rol
import com.tecsup.catalogo.domain.model.Sesion
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.admin.Totales
import com.tecsup.catalogo.ui.components.Encabezado
import com.tecsup.catalogo.ui.theme.Cian
import com.tecsup.catalogo.ui.theme.MedianocheClaro
import com.tecsup.catalogo.ui.theme.TextoEncabezado
import com.tecsup.catalogo.ui.theme.TextoEncabezadoSuave

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    sesion: Sesion?,
    onVerFavoritos: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val totales = (estado as? UiState.Exito<Totales>)?.datos
    val esAdmin = sesion?.rol == Rol.ADMIN
    val nombre = stringResource(if (esAdmin) R.string.rol_admin else R.string.rol_usuario)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Encabezado(alineacion = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nombre.take(2).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextoEncabezado
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextoEncabezado)
            Text(sesion?.correo.orEmpty(), style = MaterialTheme.typography.bodySmall, color = TextoEncabezadoSuave)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.perfil_rol, if (esAdmin) "admin" else "usuario"),
                style = MaterialTheme.typography.labelMedium,
                color = Cian,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MedianocheClaro)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Contador(
                    valor = totales?.favoritos?.toString() ?: "-",
                    titulo = stringResource(R.string.perfil_favoritos),
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
                Contador(
                    valor = totales?.guardados?.toString() ?: "-",
                    titulo = stringResource(R.string.perfil_catalogo),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }

            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Opcion(
                    icono = Icons.Outlined.FavoriteBorder,
                    color = MaterialTheme.colorScheme.tertiary,
                    texto = stringResource(R.string.perfil_mis_favoritos),
                    onClick = onVerFavoritos
                ) {
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Opcion(
                    icono = Icons.Outlined.Payments,
                    color = MaterialTheme.colorScheme.primary,
                    texto = stringResource(R.string.perfil_moneda)
                ) {
                    Text(stringResource(R.string.perfil_moneda_valor), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Opcion(
                    icono = Icons.Outlined.Info,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    texto = stringResource(R.string.perfil_version)
                ) {
                    Text("1.0", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            OutlinedButton(
                onClick = onCerrarSesion,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.perfil_cerrar))
            }
        }
    }
}

@Composable
private fun Contador(valor: String, titulo: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
        Text(titulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Opcion(
    icono: ImageVector,
    color: Color,
    texto: String,
    onClick: (() -> Unit)? = null,
    final: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = color)
        Spacer(modifier = Modifier.width(12.dp))
        Text(texto, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        final()
    }
}
