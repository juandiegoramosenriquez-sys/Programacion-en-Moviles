package com.saludplus.citas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.saludplus.citas.navigation.AppNavigation
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaludPlusCitasTheme {
                AppNavigation()
            }
        }
    }
}
