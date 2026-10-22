package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.viewmodel.SesionViewModel

@Composable
fun RegistroScreen(
    vm: SesionViewModel,
    onRegistrado: (Usuario) -> Unit,
    modifier: Modifier = Modifier
) {
    val usuario by vm.usuarioActual.collectAsState()
    val mensaje by vm.errorRegistro.collectAsState()

    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(usuario) {
        usuario?.let(onRegistrado)
    }

    DisposableEffect(Unit) {
        onDispose { vm.limpiarErrorRegistro() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "Crea tu cuenta de estudiante. Tu código de estudiante se genera automáticamente.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LavaTextField(nombre, { nombre = it }, "Nombre completo")
        LavaTextField(correo, { correo = it }, "Correo", teclado = KeyboardType.Email)
        LavaTextField(clave, { clave = it }, "Contraseña", esClave = true)
        LavaTextField(confirmar, { confirmar = it }, "Repite la contraseña", esClave = true)

        mensaje?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        LavaButton("Crear cuenta", onClick = { vm.registrar(nombre, correo, clave, confirmar) })
    }
}
