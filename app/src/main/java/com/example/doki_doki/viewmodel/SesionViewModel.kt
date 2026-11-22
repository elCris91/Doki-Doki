package com.example.doki_doki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.doki_doki.model.SesionEstado
import com.example.doki_doki.model.Usuario
import com.example.doki_doki.repository.SesionRepository
import com.example.doki_doki.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// maneja quién está conectado en toda la app: recuperar, iniciar y cerrar la sesión
class SesionViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sesionRepository: SesionRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(SesionEstado())
    val estado: StateFlow<SesionEstado> = _estado.asStateFlow()

    init {
        revisarSesionGuardada()
    }

    // al abrir la app busca si quedó un correo guardado y trae ese usuario desde Room
    private fun revisarSesionGuardada() {
        viewModelScope.launch {
            val correo = sesionRepository.obtenerCorreoGuardado()
            val usuario = correo?.let { usuarioRepository.obtenerPorCorreo(it) }
            _estado.update { it.copy(revisada = true, usuario = usuario) }
        }
    }

    fun iniciarSesion(usuario: Usuario) {
        sesionRepository.guardarSesion(usuario.correo)
        _estado.update { it.copy(usuario = usuario) }
    }

    fun cerrarSesion() {
        sesionRepository.cerrarSesion()
        _estado.update { it.copy(usuario = null) }
    }

    companion object {
        fun fabrica(usuarioRepository: UsuarioRepository, sesionRepository: SesionRepository) =
            viewModelFactory {
                initializer { SesionViewModel(usuarioRepository, sesionRepository) }
            }
    }
}