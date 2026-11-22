package com.example.doki_doki.repository

import com.example.doki_doki.model.Usuario
import com.example.doki_doki.model.UsuarioDao
import com.example.doki_doki.network.RetrofitClient
import com.example.doki_doki.network.XanoApiService
import java.security.MessageDigest

class UsuarioRepository(
    private val usuarioDao: UsuarioDao,
    private val apiService: XanoApiService = RetrofitClient.apiService
) {

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        val correoNormalizado = correo.trim().lowercase()
        val hashIngresado = hashear(contrasena)

        return try {
            // 1. Primero consulta en Xano (Cloud-first)
            val usuariosRemotos = apiService.getUsuarios()
            val usuarioRemoto = usuariosRemotos.find {
                it.correo.trim().lowercase() == correoNormalizado &&
                        it.contrasenaHash == hashIngresado
            }

            if (usuarioRemoto != null) {
                guardarRespaldoLocal(usuarioRemoto)
            }
            usuarioRemoto
        } catch (e: Exception) {
            // 2. Si falla internet, busca en Room (respaldo local)
            val usuarioLocal = usuarioDao.buscarPorCorreo(correoNormalizado) ?: return null
            if (usuarioLocal.contrasenaHash == hashIngresado) usuarioLocal else null
        }
    }

    suspend fun existeCorreo(correo: String): Boolean {
        val correoNormalizado = correo.trim().lowercase()
        return try {
            // 1. Verifica primero en Xano
            apiService.getUsuarios().any {
                it.correo.trim().lowercase() == correoNormalizado
            }
        } catch (e: Exception) {
            // 2. Si no hay conexión, verifica en Room
            usuarioDao.buscarPorCorreo(correoNormalizado) != null
        }
    }

    suspend fun registrar(nombre: String, correo: String, contrasena: String) {
        val nuevoUsuario = Usuario(
            nombre = nombre.trim(),
            correo = correo.trim().lowercase(),
            contrasenaHash = hashear(contrasena)
        )

        try {
            // 1. Guarda el usuario en Xano (POST /usuario)
            val usuarioCreadoEnXano = apiService.registrarUsuario(nuevoUsuario)
            // 2. Guarda una copia en Room como respaldo local
            guardarRespaldoLocal(usuarioCreadoEnXano)
        } catch (e: Exception) {
            // 3. Si falla internet, lo guarda al menos en Room para no perder el registro
            guardarRespaldoLocal(nuevoUsuario)
        }
    }

    private suspend fun guardarRespaldoLocal(usuario: Usuario) {
        try {
            val yaExisteEnRoom = usuarioDao.buscarPorCorreo(usuario.correo.trim().lowercase()) != null
            if (!yaExisteEnRoom) {
                usuarioDao.insertar(usuario)
            }
        } catch (_: Exception) {
            // Evita que un conflicto de ID en Room interrumpa el flujo principal
        }
    }

    private fun hashear(texto: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(texto.toByteArray())
        return bytes.joinToString("") { byte -> "%02x".format(byte) }
    }
}