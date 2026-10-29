package com.example.doki_doki.model

data class RegistroEstado(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val aceptaTerminos: Boolean = false,
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorConfirmar: String? = null,
    val errorTerminos: String? = null,
    val cargando: Boolean = false,
    val registroExitoso: Boolean = false
)