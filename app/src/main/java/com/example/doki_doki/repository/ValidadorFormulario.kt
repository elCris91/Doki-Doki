package com.example.doki_doki.repository

import android.util.Patterns

object ValidadorFormulario {

    fun validarCorreo(correo: String): String? = when {
        correo.isBlank() -> "Ingresa tu correo"
        !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> "Ingresa un correo válido"
        else -> null
    }

    fun validarContrasenaLogin(contrasena: String): String? =
        if (contrasena.isBlank()) "Ingresa tu contraseña" else null

    // el nombre no puede estar vacío y debe tener al menos 3 letras
    fun validarNombre(nombre: String): String? = when {
        nombre.isBlank() -> "Ingresa tu nombre"
        nombre.trim().length < 3 -> "El nombre debe tener al menos 3 letras"
        else -> null
    }

    // en el registro la contraseña debe tener mínimo 8 caracteres y al menos un número
    fun validarContrasenaRegistro(contrasena: String): String? = when {
        contrasena.isBlank() -> "Ingresa una contraseña"
        contrasena.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
        !contrasena.any { it.isDigit() } -> "La contraseña debe tener al menos un número"
        else -> null
    }

    // las dos contraseñas tienen que ser iguales
    fun validarConfirmacion(contrasena: String, confirmacion: String): String? = when {
        confirmacion.isBlank() -> "Repite la contraseña"
        confirmacion != contrasena -> "Las contraseñas no coinciden"
        else -> null
    }

    fun validarTerminos(acepta: Boolean): String? =
        if (!acepta) "Debes aceptar los términos y condiciones" else null
}