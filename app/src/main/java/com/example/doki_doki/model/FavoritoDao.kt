package com.example.doki_doki.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {
    @Insert
    suspend fun agregar(favorito: Favorito): Long

    @Delete
    suspend fun quitar(favorito: Favorito)

    @Query("SELECT * FROM favoritos WHERE usuarioId = :usuarioId AND investigadorId = :investigadorId LIMIT 1")
    suspend fun buscar(usuarioId: Int, investigadorId: Int): Favorito?

    @Query("SELECT investigadorId FROM favoritos WHERE usuarioId = :usuarioId")
    fun obtenerIdsFavoritos(usuarioId: Int): Flow<List<Int>>
}