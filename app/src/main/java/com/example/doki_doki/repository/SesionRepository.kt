package com.example.doki_doki.repository

import android.content.Context
import androidx.core.content.edit

// guarda la sesión en SharedPreferences: un archivo chico de clave y valor dentro del teléfono
// solo se guarda el correo del usuario conectado; sus datos completos siguen en Room
class SesionRepository(context: Context) {

    private val preferencias = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)

    fun guardarSesion(correo: String) {
        preferencias.edit { putString(CLAVE_CORREO, correo) }
    }

    // devuelve null si no hay nadie con la sesión iniciada
    fun obtenerCorreoGuardado(): String? = preferencias.getString(CLAVE_CORREO, null)

    fun cerrarSesion() {
        preferencias.edit { remove(CLAVE_CORREO) }
    }

    private companion object {
        const val CLAVE_CORREO = "correo_usuario"
    }
}