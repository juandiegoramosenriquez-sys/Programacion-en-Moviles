package com.DiegoRamos.semana06.navigaton

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.DiegoRamos.semana06.model.listaProductos
import com.DiegoRamos.semana06.screens.InicioScreen
import com.DiegoRamos.semana06.screens.ProductCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Lista compartida: la tarjeta la modifica y el drawer la lee
    val favoritos = remember { mutableStateListOf<String>() }
    val toggleFavorito: (String) -> Unit = { nombre ->
        if (nombre in favoritos) favoritos.remove(nombre) else favoritos.add(nombre)
    }

    // Navega y cierra el drawer
    val irA: (String) -> Unit = { ruta ->
        navController.navigate(ruta) { launchSingleTop = true }
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Encabezado del usuario
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "DR",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Diego Ramos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "diego.ramos@tecsup.edu.pe",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))

                ItemDrawer("Inicio", Icons.Default.Home, currentRoute == Screen.Inicio.route) {
                    irA(Screen.Inicio.route)
                }
                ItemDrawer("Mis pedidos", Icons.Default.ShoppingCart, currentRoute == Screen.Pedidos.route) {
                    irA(Screen.Pedidos.route)
                }
                ItemDrawer(
                    "Favoritos", Icons.Default.Favorite,
                    currentRoute == Screen.Favoritos.route,
                    contador = favoritos.size
                ) {
                    irA(Screen.Favoritos.route)
                }
                ItemDrawer("Perfil", Icons.Default.Person, currentRoute == Screen.Perfil.route) {
                    irA(Screen.Perfil.route)
                }
                ItemDrawer("Cerrar sesión", Icons.Default.ExitToApp, false) {
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    irA(Screen.Inicio.route)
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("TECSUP Store", fontWeight = FontWeight.Bold)
                            Text("Más vendidos", style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Inicio.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(Screen.Inicio.route) {
                    InicioScreen(favoritos = favoritos, onToggleFavorito = toggleFavorito)
                }
                composable(Screen.Pedidos.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Pantalla: Mis Pedidos")
                    }
                }
                composable(Screen.Favoritos.route) {
                    val productosFav = listaProductos.filter { it.nombre in favoritos }
                    if (productosFav.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Aún no tienes favoritos")
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(productosFav) { producto ->
                                ProductCard(
                                    producto = producto,
                                    esFavorito = true,
                                    onFavoritoClick = { toggleFavorito(producto.nombre) }
                                )
                            }
                        }
                    }
                }
                composable(Screen.Perfil.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Pantalla: Mi Perfil")
                    }
                }
            }
        }
    }
}

// Ítem del drawer: el activo se resalta con fondo de color
@Composable
private fun ItemDrawer(
    texto: String,
    icono: ImageVector,
    seleccionado: Boolean,
    contador: Int = 0,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(texto, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal)
        },
        icon = { Icon(icono, contentDescription = null) },
        badge = {
            if (contador > 0) {
                Badge { Text(contador.toString()) }
            }
        },
        selected = seleccionado,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}