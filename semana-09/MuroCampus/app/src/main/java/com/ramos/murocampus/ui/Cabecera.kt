package com.ramos.murocampus.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun colorCabecera(): Color {
    val esquema = MaterialTheme.colorScheme
    return if (esquema.background.luminance() < 0.5f) esquema.primaryContainer else esquema.primary
}

@Composable
fun colorSobreCabecera(): Color {
    val esquema = MaterialTheme.colorScheme
    return if (esquema.background.luminance() < 0.5f) esquema.onPrimaryContainer else esquema.onPrimary
}

@Composable
fun FondoOlas(modifier: Modifier = Modifier) {
    val fondo = colorCabecera()
    val superficie = MaterialTheme.colorScheme.surface
    val olaClara = lerp(fondo, superficie, 0.35f)

    Canvas(modifier = modifier) {
        val ancho = size.width
        val alto = size.height
        drawRect(fondo)

        val ola = Path().apply {
            moveTo(0f, alto * 0.70f)
            cubicTo(ancho * 0.30f, alto * 0.52f, ancho * 0.62f, alto * 0.92f, ancho, alto * 0.64f)
            lineTo(ancho, alto)
            lineTo(0f, alto)
            close()
        }
        drawPath(ola, olaClara)

        val base = Path().apply {
            moveTo(0f, alto * 0.86f)
            cubicTo(ancho * 0.28f, alto * 0.70f, ancho * 0.58f, alto * 1.02f, ancho, alto * 0.80f)
            lineTo(ancho, alto)
            lineTo(0f, alto)
            close()
        }
        drawPath(base, superficie)
    }
}

@Composable
fun TituloMarca() {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = colorSobreCabecera())) { append("Muro") }
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiaryContainer)) { append("Campus") }
        },
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun CabeceraOlas(onMenu: () -> Unit) {
    val sobre = colorSobreCabecera()
    Box(modifier = Modifier.fillMaxWidth()) {
        FondoOlas(modifier = Modifier.matchParentSize())
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 52.dp)
        ) {
            IconButton(onClick = onMenu) {
                Icon(Icons.Outlined.Menu, contentDescription = "Abrir menú", tint = sobre)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                TituloMarca()
                Text(
                    text = "El muro de tu campus",
                    style = MaterialTheme.typography.bodySmall,
                    color = sobre.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun MenuLateral(
    soloMias: Boolean,
    creando: Boolean,
    oscuro: Boolean,
    onInicio: () -> Unit,
    onMisPublicaciones: () -> Unit,
    onNueva: () -> Unit,
    onCambiarTema: () -> Unit,
    onAcercaDe: () -> Unit
) {
    val colores = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer
    )

    ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.surface) {
        Box(modifier = Modifier.fillMaxWidth().height(190.dp)) {
            FondoOlas(modifier = Modifier.matchParentSize())
            Column(modifier = Modifier.statusBarsPadding().padding(20.dp)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Text(
                        text = "MC",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                TituloMarca()
                Text(
                    text = "Programación en Móviles · Tecsup",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorSobreCabecera().copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        NavigationDrawerItem(
            label = { Text("Inicio") },
            icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            selected = !soloMias && !creando,
            onClick = onInicio,
            colors = colores,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Mis publicaciones") },
            icon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            selected = soloMias && !creando,
            onClick = onMisPublicaciones,
            colors = colores,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Nueva publicación") },
            icon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
            selected = creando,
            onClick = onNueva,
            colors = colores,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp))

        NavigationDrawerItem(
            label = { Text("Modo oscuro") },
            icon = { Icon(Icons.Outlined.DarkMode, contentDescription = null) },
            badge = {
                Switch(
                    checked = oscuro,
                    onCheckedChange = { onCambiarTema() },
                    colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                )
            },
            selected = false,
            onClick = onCambiarTema,
            colors = colores,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Acerca de MuroCampus") },
            icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            selected = false,
            onClick = onAcercaDe,
            colors = colores,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
