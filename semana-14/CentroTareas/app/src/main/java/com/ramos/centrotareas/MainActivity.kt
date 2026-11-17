package com.ramos.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ramos.centrotareas.ui.PantallaTareas
import com.ramos.centrotareas.ui.theme.CentroTareasTheme
import com.ramos.centrotareas.viewmodel.TareasViewModel
import com.ramos.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    private val vm: TareasViewModel by viewModels { TareasViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroTareasTheme {
                PantallaTareas(vm)
            }
        }
    }
}
