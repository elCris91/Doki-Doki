package com.example.doki_doki.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favoritos",
    indices = [Index(value = ["usuarioId", "investigadorId"], unique = true)]
)
data class Favorito(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val investigadorId: Int
)