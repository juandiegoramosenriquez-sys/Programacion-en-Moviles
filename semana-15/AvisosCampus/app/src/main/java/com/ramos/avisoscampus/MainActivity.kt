package com.ramos.avisoscampus

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramos.avisoscampus.ui.Bienvenida
import com.ramos.avisoscampus.ui.PantallaAvisos
import com.ramos.avisoscampus.ui.theme.AvisosCampusTheme
import com.ramos.avisoscampus.viewmodel.AvisosViewModel
import com.ramos.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        setContent {
            val sistemaOscuro = isSystemInDarkTheme()
            var oscuro by remember { mutableStateOf(preferencias.getBoolean("oscuro", sistemaOscuro)) }
            var vioBienvenida by remember { mutableStateOf(preferencias.getBoolean("bienvenida", false)) }
            val barrasClaras = oscuro || !vioBienvenida
            LaunchedEffect(barrasClaras) {
                val estilo = if (barrasClaras) {
                    SystemBarStyle.dark(Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                }
                enableEdgeToEdge(statusBarStyle = estilo, navigationBarStyle = estilo)
            }
            AvisosCampusTheme(darkTheme = oscuro) {
                if (!vioBienvenida) {
                    Bienvenida(alTerminar = {
                        vioBienvenida = true
                        preferencias.edit { putBoolean("bienvenida", true) }
                    })
                } else {
                    val vm: AvisosViewModel = viewModel(factory = AvisosViewModelFactory(applicationContext))
                    PantallaAvisos(vm, oscuro, alCambiarTema = {
                        oscuro = it
                        preferencias.edit { putBoolean("oscuro", it) }
                    })
                }
            }
        }
    }
}
