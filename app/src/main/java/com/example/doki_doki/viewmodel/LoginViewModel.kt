package com.example.doki_doki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.doki_doki.model.LoginEstado
import com.example.doki_doki.repository.UsuarioRepository
import com.example.doki_doki.repository.ValidadorFormulario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



//login de la pantalla del login, se guarda lo escrito por el usuario, y valida y consulta el repo
class LoginViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _estado = MutableStateFlow(LoginEstado())
    val estado: StateFlow<LoginEstado> = _estado.asStateFlow()

    fun alCambiarCorreo(nuevoCorreo: String) {
        _estado.update { it.copy(correo = nuevoCorreo, errorCorreo = null, errorGeneral = null) }
    }

    fun alCambiarContrasena(nuevaContrasena: String) {
        _estado.update { it.copy(contrasena = nuevaContrasena, errorContrasena = null, errorGeneral = null) }
    }



        //se llama al ingresar
    fun iniciarSesion() {
        val estadoActual = _estado.value
        val errorCorreo = ValidadorFormulario.validarCorreo(estadoActual.correo)
        val errorContrasena = ValidadorFormulario.validarContrasenaLogin(estadoActual.contrasena)



            //si algun campo tiene error o esta mal, mostrara errores sin consultar a la base de datos
        if (errorCorreo != null || errorContrasena != null) {
            _estado.update { it.copy(errorCorreo = errorCorreo, errorContrasena = errorContrasena) }
            return
        }

        _estado.update { it.copy(cargando = true, errorGeneral = null) }




            //consulta base de datos en 2do plano
        viewModelScope.launch {
            val usuario = usuarioRepository.iniciarSesion(estadoActual.correo, estadoActual.contrasena)
            _estado.update {
                if (usuario != null) {
                    it.copy(cargando = false, usuarioIngresado = usuario)
                } else {



                    //mensaje general por si hay fallo.
                    it.copy(cargando = false, errorGeneral = "Correo o contraseña incorrectos")
                }
            }
        }
    }

    companion object {
        fun fabrica(usuarioRepository: UsuarioRepository) = viewModelFactory {
            initializer { LoginViewModel(usuarioRepository) }
        }
    }
}