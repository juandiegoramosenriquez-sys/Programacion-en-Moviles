package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.R
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun SplashScreen(
    onIniciarSesion: () -> Unit,
    onRegistrarse: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.logo_saludplus),
            contentDescription = "Logo de Clínica SaludPlus",
            modifier = Modifier.size(130.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("Clínica", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = TextoPrincipal)
        Text("SaludPlus", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = AzulPrimario)
        Spacer(Modifier.height(8.dp))
        Text("Tu salud, nuestra prioridad", color = TextoSecundario)

        Spacer(Modifier.weight(1.3f))

        BotonPrincipal(texto = "Comenzar", onClick = onRegistrarse)
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onIniciarSesion) {
            Text("Ya tengo una cuenta", color = AzulPrimario, fontWeight = FontWeight.SemiBold)
        }
    }
}
