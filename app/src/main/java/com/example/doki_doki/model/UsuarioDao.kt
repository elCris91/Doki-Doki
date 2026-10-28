package com.example.doki_doki.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert

@Dao
interface UsuarioDao {
    // guarda un usuario nuevo (registro)
    @Insert
    suspend fun insertar(usuario: Usuario)
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?
}
