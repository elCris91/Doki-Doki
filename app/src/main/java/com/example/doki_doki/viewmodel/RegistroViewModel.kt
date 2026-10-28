package com.example.doki_doki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.doki_doki.model.RegistroEstado
import com.example.doki_doki.repository.UsuarioRepository
import com.example.doki_doki.repository.ValidadorFormulario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _estado = MutableStateFlow(RegistroEstado())
    val estado: StateFlow<RegistroEstado> = _estado.asStateFlow()

    fun alCambiarNombre(v: String) = _estado.update { it.copy(nombre = v, errorNombre = null) }
    fun alCambiarCorreo(v: String) = _estado.update { it.copy(correo = v, errorCorreo = null) }
    fun alCambiarContrasena(v: String) = _estado.update { it.copy(contrasena = v, errorContrasena = null) }
    fun alCambiarConfirmar(v: String) = _estado.update { it.copy(confirmarContrasena = v, errorConfirmar = null) }
    fun alCambiarTerminos(v: Boolean) = _estado.update { it.copy(aceptaTerminos = v, errorTerminos = null) }

    fun registrar() {
        val estadoActual = _estado.value

        val errorNombre = ValidadorFormulario.validarNombre(estadoActual.nombre)
        val errorCorreo = ValidadorFormulario.validarCorreo(estadoActual.correo)
        val errorContrasena = ValidadorFormulario.validarContrasenaRegistro(estadoActual.contrasena)
        val errorConfirmar = ValidadorFormulario.validarConfirmacion(estadoActual.contrasena, estadoActual.confirmarContrasena)
        val errorTerminos = ValidadorFormulario.validarTerminos(estadoActual.aceptaTerminos)

        if (listOf(errorNombre, errorCorreo, errorContrasena, errorConfirmar, errorTerminos).any { it != null }) {
            _estado.update {
                it.copy(
                    errorNombre = errorNombre,
                    errorCorreo = errorCorreo,
                    errorContrasena = errorContrasena,
                    errorConfirmar = errorConfirmar,
                    errorTerminos = errorTerminos
                )
            }
            return
        }

        _estado.update { it.copy(cargando = true) }

        viewModelScope.launch {
            val correo = estadoActual.correo.trim().lowercase()
            if (usuarioRepository.existeCorreo(correo)) {
                _estado.update { it.copy(cargando = false, errorCorreo = "Este correo ya está registrado") }
                return@launch
            }
            usuarioRepository.registrar(
                nombre = estadoActual.nombre,
                correo = correo,
                contrasena = estadoActual.contrasena
            )
            _estado.update { it.copy(cargando = false, registroExitoso = true) }
        }
    }

    companion object {
        fun fabrica(usuarioRepository: UsuarioRepository) = viewModelFactory {
            initializer { RegistroViewModel(usuarioRepository) }
        }
    }
}