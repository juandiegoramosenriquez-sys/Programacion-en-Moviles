package com.ramos.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramos.campusgps.model.TipoError
import com.ramos.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val icono = if (error.tipo == TipoError.SIN_PLAY_SERVICES) Icons.Outlined.WarningAmber else Icons.Outlined.LocationOff

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.width(10.dp))
                Text(error.mensaje, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            }

            when (error.tipo) {
                TipoError.SIN_PERMISO -> BotonError("Abrir ajustes", Icons.Outlined.Settings) {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", contexto.packageName, null)
                    )
                    contexto.startActivity(intent)
                }

                TipoError.GPS_APAGADO, TipoError.OTRO -> BotonError("Reintentar", Icons.Outlined.Refresh, onReintentar)

                TipoError.SIN_PLAY_SERVICES -> Unit
            }
        }
    }
}

@Composable
private fun BotonError(texto: String, icono: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth().height(48.dp)
    ) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(texto, fontWeight = FontWeight.SemiBold)
    }
}
