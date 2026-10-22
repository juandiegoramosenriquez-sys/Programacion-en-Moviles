package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.ui.theme.LavaTheme
import com.tecsup.lavacampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: SesionViewModel, onIngresar: (Usuario) -> Unit, onRegistrarse: () -> Unit) {
    val usuario by vm.usuarioActual.collectAsState()
    val mensaje by vm.error.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    var rol by rememberSaveable { mutableStateOf(vm.rolLogin.value) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(usuario) {
        usuario?.let(onIngresar)
    }

    val opciones = listOf(Rol.CLIENTE to "Estudiante", Rol.ADMIN to "Administrador")

    LavaTheme(rol = rol, oscuro = oscuro) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(48.dp))
                IconoServicio("wash", tamano = 80.dp)
                Spacer(Modifier.height(16.dp))
                Text("LavaCampus", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                Text(
                    "Lavandería universitaria",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(32.dp))

                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    opciones.forEachIndexed { i, (opcion, texto) ->
                        SegmentedButton(
                            selected = rol == opcion,
                            onClick = {
                                rol = opcion
                                vm.limpiarError()
                            },
                            shape = SegmentedButtonDefaults.itemShape(i, opciones.size),
                            icon = {}
                        ) {
                            Text(texto)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))

                LavaTextField(correo, { correo = it }, "Correo", teclado = KeyboardType.Email)
                Spacer(Modifier.height(12.dp))
                LavaTextField(clave, { clave = it }, "Contraseña", esClave = true)

                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    mensaje?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                LavaButton("Ingresar", onClick = { vm.login(correo, clave, rol) })

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    if (rol == Rol.CLIENTE) {
                        TextButton(onClick = {
                            vm.limpiarError()
                            onRegistrarse()
                        }) {
                            Text("¿No tienes cuenta? Regístrate")
                        }
                    }
                }
            }
        }
    }
}
