package com.ramos.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramos.avisoscampus.ui.PantallaAvisos
import com.ramos.avisoscampus.ui.theme.AvisosCampusTheme
import com.ramos.avisoscampus.viewmodel.AvisosViewModel
import com.ramos.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvisosCampusTheme {
                val vm: AvisosViewModel = viewModel(factory = AvisosViewModelFactory(applicationContext))
                PantallaAvisos(vm)
            }
        }
    }
}
