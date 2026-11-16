package com.ramos.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ramos.campusgps.ui.PantallaMapa
import com.ramos.campusgps.ui.theme.CampusGPSTheme
import com.ramos.campusgps.viewmodel.CampusViewModelFactory
import com.ramos.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {

    private val vm: MapaViewModel by viewModels { CampusViewModelFactory(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusGPSTheme {
                PantallaMapa(vm)
            }
        }
    }
}
