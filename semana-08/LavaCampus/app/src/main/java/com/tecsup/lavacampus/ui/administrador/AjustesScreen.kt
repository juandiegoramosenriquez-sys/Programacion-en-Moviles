package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.ui.components.Avatar
import com.tecsup.lavacampus.ui.components.FilaSwitch
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    usuario: Usuario,
    sesion: SesionViewModel,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val oscuro by sesion.modoOscuro.collectAsState()
    val avisos by sesion.notificaciones.collectAsState()
    val recibir by sesion.recibirPedidos.collectAsState()
    val limite by sesion.limitePedidos.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Avatar(usuario.nombre, tamano = 56.dp)
                Column(Modifier.padding(start = 12.dp)) {
                    Text(usuario.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(
                        usuario.correo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LavaButton("Cambiar de perfil", onClick = onCambiarPerfil)

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                FilaSwitch("Modo oscuro", "Cambia los colores de la app", oscuro, sesion::cambiarModoOscuro)
                FilaSwitch("Notificaciones", "Avisos de pedidos nuevos", avisos, sesion::cambiarNotificaciones)
                FilaSwitch("Recibir pedidos", "Si lo apagas, los estudiantes no pueden pedir", recibir, sesion::cambiarRecibirPedidos)
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Máximo de pedidos activos", style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Cuántos pedidos sin entregar puede tener cada estudiante a la vez",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    (1..5).forEach { n ->
                        SegmentedButton(
                            selected = limite == n,
                            onClick = { sesion.cambiarLimitePedidos(n) },
                            shape = SegmentedButtonDefaults.itemShape(n - 1, 5),
                            icon = {}
                        ) {
                            Text("$n")
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onCerrarSesion,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}
