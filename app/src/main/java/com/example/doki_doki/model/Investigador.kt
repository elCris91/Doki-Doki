package com.example.doki_doki.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// tabla de investigadores, "linea" se refiere a la lineaa de investigacion en la que trabaja
@Entity(tableName = "investigadores")
data class Investigador(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val linea: String,
    val area: String,
    val descripcion: String,
    val correo: String
)