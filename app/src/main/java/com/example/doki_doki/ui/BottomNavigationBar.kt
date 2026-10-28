package com.example.doki_doki.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class ItemNavegacion(val ruta: String, val icono: ImageVector, val titulo: String) {
    object Inicio : ItemNavegacion(Rutas.INICIO, Icons.Default.Home, "Inicio")
    object Perfil : ItemNavegacion(Rutas.PERFIL, Icons.Default.Person, "Mi Perfil")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        ItemNavegacion.Inicio,
        ItemNavegacion.Perfil
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry.value?.destination?.route

    // en el login no se muestra la barra
    if (rutaActual != Rutas.LOGIN) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            items.forEach { item ->
                val seleccionado = rutaActual == item.ruta

                NavigationBarItem(
                    icon = { Icon(imageVector = item.icono, contentDescription = item.titulo) },
                    label = { Text(text = item.titulo) },
                    selected = seleccionado,
                    onClick = {
                        if (!seleccionado) {
                            navController.navigate(item.ruta) {
                                // vuelve al inicio sin ir apilando pantallas
                                popUpTo(Rutas.INICIO) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.surface,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}