package com.example.doki_doki.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doki_doki.repository.InvestigadorRepository
import com.example.doki_doki.repository.SesionRepository
import com.example.doki_doki.repository.UsuarioRepository
import com.example.doki_doki.viewmodel.ListadoViewModel
import com.example.doki_doki.viewmodel.LoginViewModel
import com.example.doki_doki.viewmodel.RegistroViewModel
import com.example.doki_doki.viewmodel.SesionViewModel

// nombres de las rutas, así no hay que escribirlos a mano en cada lado
object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val PERFIL = "perfil"
}

// acá se define qué pantalla va en cada ruta
@Composable
fun Navegacion(
    usuarioRepository: UsuarioRepository,
    investigadorRepository: InvestigadorRepository,
    sesionRepository: SesionRepository
) {
    val navController = rememberNavController()

    // la sesión vive en un viewmodel: se guarda al cerrar la app y se mantiene al girar la pantalla
    val sesionViewModel: SesionViewModel =
        viewModel(factory = SesionViewModel.fabrica(usuarioRepository, sesionRepository))
    val sesion by sesionViewModel.estado.collectAsState()

    // si al abrir la app había una sesión guardada, se salta el login y se va al listado
    LaunchedEffect(sesion.revisada) {
        if (sesion.usuario != null && navController.currentDestination?.route == Rutas.LOGIN) {
            navController.navigate(Rutas.INICIO) {
                popUpTo(Rutas.LOGIN) { inclusive = true }
            }
        }
    }

    // Scaffold con la barra inferior agregada
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { espacioInterno ->
        NavHost(
            navController = navController,
            startDestination = Rutas.LOGIN,
            modifier = Modifier.padding(espacioInterno)
        ) {
            composable(Rutas.LOGIN) {
                val loginViewModel: LoginViewModel =
                    viewModel(factory = LoginViewModel.fabrica(usuarioRepository))
                LoginScreen(
                    viewModel = loginViewModel,
                    alIngresar = { usuario ->
                        sesionViewModel.iniciarSesion(usuario)
                        // saca el login del historial, así con "atrás" no se vuelve al login
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    alIrARegistro = { navController.navigate(Rutas.REGISTRO) }
                )
            }

            composable(Rutas.REGISTRO) {
                val registroViewModel: RegistroViewModel =
                    viewModel(factory = RegistroViewModel.fabrica(usuarioRepository))
                RegistroScreen(
                    viewModel = registroViewModel,
                    // popBackStack vuelve a la pantalla anterior, o sea al login
                    alRegistrarse = { navController.popBackStack() },
                    alVolverALogin = { navController.popBackStack() }
                )
            }

            // la pantalla de inicio es el listado de investigadores
            composable(Rutas.INICIO) {
                val listadoViewModel: ListadoViewModel =
                    viewModel(factory = ListadoViewModel.fabrica(investigadorRepository))
                ListadoScreen(viewModel = listadoViewModel)
            }

            composable(Rutas.PERFIL) {
                // Vista de perfil con la cámara, los datos del usuario conectado y cerrar sesión
                PerfilUsuarioScreen(
                    usuario = sesion.usuario,
                    alCerrarSesion = {
                        sesionViewModel.cerrarSesion()
                        // borra todo el historial y vuelve al login
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}