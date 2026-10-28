package com.example.doki_doki.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// tabla de proyectos, el estado puede ser en curso o finalizado
@Entity(tableName = "proyectos")
data class Proyecto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val linea: String,
    val anio: Int,
    val estado: String
)