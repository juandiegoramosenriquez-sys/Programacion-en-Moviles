package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.model.Usuario
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoTexto

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onVerTerminos: () -> Unit,
    onBack: () -> Unit
) {
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var aceptaTerminos by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Registro", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CampoTexto(nombres, { nombres = it }, "Nombres")
            CampoTexto(apellidos, { apellidos = it }, "Apellidos")
            CampoTexto(dni, { dni = it }, "DNI")
            CampoTexto(correo, { correo = it }, "Correo")
            CampoTexto(celular, { celular = it }, "Celular")
            CampoTexto(password, { password = it }, "Contraseña", PasswordVisualTransformation())

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = aceptaTerminos,
                    onCheckedChange = { aceptaTerminos = it }
                )
                Text("Acepto los ")
                Text(
                    text = "términos y condiciones",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onVerTerminos() }
                )
            }

            if (error.isNotEmpty()) {
                Text(error, color = Color.Red)
            }

            BotonPrincipal(
                texto = "Registrarme",
                onClick = {
                    error = when {
                        nombres.isBlank() || apellidos.isBlank() || dni.isBlank() ||
                                correo.isBlank() || celular.isBlank() || password.isBlank() ->
                            "Completa todos los campos"
                        !correo.contains("@") -> "El correo no es válido"
                        dni.length != 8 || !dni.all { it.isDigit() } -> "El DNI debe tener 8 dígitos"
                        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                        !aceptaTerminos -> "Debes aceptar los términos y condiciones"
                        else -> ""
                    }

                    if (error.isEmpty()) {
                        val registrado = Repositorio.registrarUsuario(
                            Usuario(
                                nombres = nombres,
                                apellidos = apellidos,
                                dni = dni,
                                correo = correo,
                                celular = celular,
                                password = password
                            )
                        )
                        if (registrado) onRegistroExitoso()
                        else error = "Ese correo ya está registrado"
                    }
                }
            )
        }
    }
}