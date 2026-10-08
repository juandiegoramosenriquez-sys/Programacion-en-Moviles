package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.R
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoTexto
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoSecundario

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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_saludplus),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text("¡Bienvenido de nuevo!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Ingresa para gestionar tus citas", color = TextoSecundario)
            }

            Spacer(Modifier.height(8.dp))

            CampoTexto(correo, { correo = it }, "Correo", icono = Icons.Default.Email)
            CampoTexto(password, { password = it }, "Contraseña", PasswordVisualTransformation(), Icons.Default.Lock)

            if (error.isNotEmpty()) {
                Text(error, color = Rojo)
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
                color = AzulPrimario,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onIrARegistro() }
            )
        }
    }
}
