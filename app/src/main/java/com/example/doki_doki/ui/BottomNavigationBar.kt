package com.example.doki_doki.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
    object Inicio : ItemNavegacion(Rutas.inicio("Usuario"), Icons.Default.Home, "Inicio")
    object Favoritos : ItemNavegacion(Rutas.FAVORITOS, Icons.Default.Favorite, "Favoritos")
    object Perfil : ItemNavegacion(Rutas.PERFIL, Icons.Default.Person, "Mi Perfil")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        ItemNavegacion.Inicio,
        ItemNavegacion.Favoritos,
        ItemNavegacion.Perfil
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry.value?.destination?.route

    if (rutaActual != Rutas.LOGIN) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            items.forEach { item ->
                val seleccionado = when (item) {
                    is ItemNavegacion.Inicio -> rutaActual?.startsWith("inicio") == true
                    else -> rutaActual == item.ruta
                }

                NavigationBarItem(
                    icon = { Icon(imageVector = item.icono, contentDescription = item.titulo) },
                    label = { Text(text = item.titulo) },
                    selected = seleccionado,
                    onClick = {
                        if (!seleccionado) {
                            navController.navigate(item.ruta) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
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