package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.saludplus.citas.ui.theme.AzulClaro
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(AzulClaro),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_saludplus),
                    contentDescription = "Logo SaludPlus",
                    modifier = Modifier.size(72.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("¡Bienvenido de nuevo!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Ingresa para gestionar tus citas", color = TextoSecundario)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CampoTexto(correo, { correo = it }, "Correo", icono = Icons.Default.Email)
                    CampoTexto(password, { password = it }, "Contraseña", PasswordVisualTransformation(), Icons.Default.Lock)

                    if (error.isNotEmpty()) {
                        Text(error, color = Rojo, fontSize = 14.sp)
                    }

                    Spacer(Modifier.height(4.dp))

                    BotonPrincipal(
                        texto = "Ingresar",
                        onClick = {
                            if (correo.isBlank() || password.isBlank()) {
                                error = "Completa todos los campos"
                            } else if (Repositorio.iniciarSesion(correo.trim(), password)) {
                                onLoginExitoso()
                            } else {
                                error = "Correo o contraseña incorrectos"
                            }
                        }
                    )
                }
            }

            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = AzulPrimario,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onIrARegistro() }
            )

            Text(
                "Usuario de prueba: ramos@gmail.com / 123456",
                color = TextoSecundario,
                fontSize = 12.sp
            )
        }
    }
}