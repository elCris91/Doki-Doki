package com.example.doki_doki.repository

import com.example.doki_doki.model.Usuario
import com.example.doki_doki.model.UsuarioDao
import java.security.MessageDigest

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {


        val usuario = usuarioDao.buscarPorCorreo(correo.trim().lowercase()) ?: return null
        return if (usuario.contrasenaHash == hashear(contrasena)) usuario else null
    }

    // trae un usuario por su correo, se usa para recuperar la sesión guardada
    suspend fun obtenerPorCorreo(correo: String): Usuario? =
        usuarioDao.buscarPorCorreo(correo.trim().lowercase())
    suspend fun existeCorreo(correo: String): Boolean =
        usuarioDao.buscarPorCorreo(correo.trim().lowercase()) != null

    suspend fun registrar(nombre: String, correo: String, contrasena: String) {
        usuarioDao.insertar(
            Usuario(
                nombre = nombre.trim(),
                correo = correo.trim().lowercase(),
                contrasenaHash = hashear(contrasena)
            )
        )
    }

    private fun hashear(texto: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(texto.toByteArray())
        return bytes.joinToString("") { byte -> "%02x".format(byte) }
    }
}