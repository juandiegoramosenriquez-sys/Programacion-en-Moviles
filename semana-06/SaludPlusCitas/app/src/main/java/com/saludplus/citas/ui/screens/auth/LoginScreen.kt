package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoTexto

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit,
    onBack: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Iniciar sesión", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CampoTexto(correo, { correo = it }, "Correo")
            CampoTexto(password, { password = it }, "Contraseña", PasswordVisualTransformation())

            if (error.isNotEmpty()) {
                Text(error, color = Color.Red)
            }

            BotonPrincipal(
                texto = "Ingresar",
                onClick = {
                    if (correo.isBlank() || password.isBlank()) {
                        error = "Completa todos los campos"
                    } else if (Repositorio.iniciarSesion(correo, password)) {
                        onLoginExitoso()
                    } else {
                        error = "Correo o contraseña incorrectos"
                    }
                }
            )

            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onIrARegistro() }
            )
        }
    }
}