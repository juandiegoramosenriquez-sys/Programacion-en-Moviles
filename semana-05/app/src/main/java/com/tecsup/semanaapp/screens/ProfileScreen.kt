package com.tecsup.semanaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.semanaapp.components.InfoRow
import com.tecsup.semanaapp.components.StudentAvatar
import com.tecsup.semanaapp.navigation.Screen
import com.tecsup.semanaapp.ui.AppLanguage
import com.tecsup.semanaapp.ui.LocalAppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    currentLanguage: AppLanguage,
    onLanguageChanged: (AppLanguage) -> Unit
) {
    val strings = LocalAppStrings.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = strings.profileTitle,
                        color = Color(0xFF6750A4),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color(0xFF6750A4)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFEADDFF))
            )
        },
        containerColor = Color(0xFFF7F2FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Banner degradado horizontal con avatar y nombre
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6750A4), Color(0xFF7D5260))
                        )
                    )
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StudentAvatar(
                        name = "Juan Diego Ramos Enriquez",
                        size = 80.dp,
                        fontSize = 26.sp,
                        hasWhiteBorder = true,
                        borderWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Juan Diego Ramos Enriquez",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // INFORMACIÓN PERSONAL
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = strings.personalInfoSection,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6750A4)
                    )
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoRow(
                                icon = Icons.Default.Person,
                                label = strings.fullNameLabel,
                                value = "Juan Diego Ramos Enriquez",
                                iconInSquare = true
                            )
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                            InfoRow(
                                icon = Icons.Default.Email,
                                label = strings.emailDetailLabel,
                                value = "diego.ramos@tecsup.edu.pe",
                                iconInSquare = true
                            )
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                            InfoRow(
                                icon = Icons.Default.Phone,
                                label = strings.phoneLabel,
                                value = "+51 987 654 321",
                                iconInSquare = true
                            )
                        }
                    }
                }

                // ACADÉMICO
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = strings.academicSection,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6750A4)
                    )
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoRow(
                                icon = Icons.Default.Star,
                                label = strings.careerLabel,
                                value = "Diseño y Desarrollo de Software",
                                iconInSquare = true
                            )
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                            InfoRow(
                                icon = Icons.Default.DateRange,
                                label = strings.currentCycleLabel,
                                value = "IV Ciclo",
                                iconInSquare = true
                            )
                        }
                    }
                }

                // PREFERENCIAS (Cambio de idioma en tiempo real)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = strings.preferencesSection,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6750A4)
                    )
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = strings.languageLabel,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D1B20)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onLanguageChanged(AppLanguage.SPANISH) }
                            ) {
                                RadioButton(
                                    selected = currentLanguage == AppLanguage.SPANISH,
                                    onClick = { onLanguageChanged(AppLanguage.SPANISH) },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6750A4))
                                )
                                Text(
                                    text = strings.spanishLang,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1D1B20)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onLanguageChanged(AppLanguage.ENGLISH) }
                            ) {
                                RadioButton(
                                    selected = currentLanguage == AppLanguage.ENGLISH,
                                    onClick = { onLanguageChanged(AppLanguage.ENGLISH) },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6750A4))
                                )
                                Text(
                                    text = strings.englishLang,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1D1B20)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón Cerrar Sesión
                Button(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9DEDC)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = Color(0xFFB3261E)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = strings.logoutButton,
                        color = Color(0xFFB3261E),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
