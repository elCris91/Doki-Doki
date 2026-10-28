package com.example.doki_doki.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doki_doki.model.Usuario
import com.example.doki_doki.repository.InvestigadorRepository
import com.example.doki_doki.repository.UsuarioRepository
import com.example.doki_doki.viewmodel.ListadoViewModel
import com.example.doki_doki.viewmodel.LoginViewModel
import com.example.doki_doki.viewmodel.RegistroViewModel

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
    investigadorRepository: InvestigadorRepository
) {
    val navController = rememberNavController()

    // guarda el usuario que inició sesión para mostrar sus datos en el perfil
    var usuarioConectado by remember { mutableStateOf<Usuario?>(null) }

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
                        usuarioConectado = usuario
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
                // Vista de perfil con la cámara y los datos del usuario conectado
                PerfilUsuarioScreen(usuario = usuarioConectado)
            }
        }
    }
}