package com.ramos.campusgps

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ramos.campusgps.ui.PantallaMapa
import com.ramos.campusgps.ui.theme.CampusGPSTheme
import com.ramos.campusgps.viewmodel.CampusViewModelFactory
import com.ramos.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {

    private val vm: MapaViewModel by viewModels { CampusViewModelFactory(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            val preferencia by vm.modoOscuro.collectAsState()
            val oscuro = preferencia ?: isSystemInDarkTheme()
            CampusGPSTheme(darkTheme = oscuro) {
                PantallaMapa(vm, oscuro)
            }
        }
    }
}
