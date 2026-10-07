package com.DiegoRamos.semana06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.DiegoRamos.semana06.navigaton.AppNavigation
import com.DiegoRamos.semana06.ui.theme.Semana06Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Semana06Theme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}