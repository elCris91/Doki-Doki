package com.example.doki_doki.repository

object ValidadorFormulario {

    private val patronCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    //hace que cada funcion devuelva un error o null si es que el valor esta bien


    fun validarCorreo(correo: String): String? = when {
        correo.isBlank() -> "Ingresa tu correo"
        !patronCorreo.matches(correo.trim()) -> "El correo no tiene un formato válido"
        else -> null
    }

    //asegura que el correo tenga formato valido

    fun validarContrasenaLogin(contrasena: String): String? =
        if (contrasena.isBlank()) "Ingresa tu contraseña" else null
}


