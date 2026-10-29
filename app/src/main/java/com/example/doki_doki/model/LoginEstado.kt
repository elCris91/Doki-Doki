package com.example.doki_doki.model


// los errores seran null cuando el campo esta correcto,si tienen texto se vera debajo del campo
data class LoginEstado(
    val correo: String = "",
    val contrasena: String = "",
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorGeneral: String? = null,   // "Correo o contraseña incorrectos"
    val cargando: Boolean = false,       // dara true mientras se consulta la base de datos
    val usuarioIngresado: Usuario? = null // tiene valor cuando el login fue correcto
)