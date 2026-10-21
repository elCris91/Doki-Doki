package com.example.doki_doki.ui

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doki_doki.repository.UsuarioRepository
import com.example.doki_doki.viewmodel.LoginViewModel

// nombres de las rutas, así no hay que escribirlos a mano en cada lado
object Rutas {
    const val LOGIN = "login"
    const val INICIO = "inicio/{nombreUsuario}"



    fun inicio(nombreUsuario: String) = "inicio/${Uri.encode(nombreUsuario)}"
}
        //acá se fdefine que pantalla va en cada ruta
    @Composable
    fun Navegacion(usuarioRepository: UsuarioRepository) {
        val navController = rememberNavController()

            //esto es para que nada quede tapado por la barra que esta encima del celular
        Scaffold(modifier = Modifier.fillMaxSize()) { espacioInterno ->
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
                        alIngresar = { nombreUsuario ->


                            //saca el login del historial por si queremos retroceder
                            navController.navigate(Rutas.inicio(nombreUsuario)) {
                                popUpTo(Rutas.LOGIN) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Rutas.INICIO) { entrada ->
                    val nombreUsuario = entrada.arguments?.getString("nombreUsuario").orEmpty()
                    InicioScreen(nombreUsuario = nombreUsuario)
                }
            }
        }
    }