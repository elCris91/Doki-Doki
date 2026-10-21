package com.example.doki_doki.repository

import com.example.doki_doki.model.Usuario
import com.example.doki_doki.model.UsuarioDao
import java.security.MessageDigest

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {

        //verifica el login y devuelve el usuario si es que existe el correo y la contraseña es correcta, sino da null


        val usuario = usuarioDao.buscarPorCorreo(correo.trim().lowercase()) ?: return null
        return if (usuario.contrasenaHash == hashear(contrasena)) usuario else null
    }

    private fun hashear(texto: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(texto.toByteArray())
        return bytes.joinToString("") { byte -> "%02x".format(byte) }
    }
}