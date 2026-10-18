package com.tecsup.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.lavacampus.data.local.LavaDatabase
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.data.repository.LavaRepositoryRoom
import com.tecsup.lavacampus.ui.navigation.NavLava
import com.tecsup.lavacampus.ui.theme.LavaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = LavaDatabase.obtener(applicationContext)
        val repo: LavaRepository = LavaRepositoryRoom(db)
        val prefs = PreferenciasStore(applicationContext)

        setContent {
            LavaTheme {
                NavLava(repo, prefs)
            }
        }
    }
}
