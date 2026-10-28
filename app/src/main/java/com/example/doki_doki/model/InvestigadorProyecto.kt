package com.example.doki_doki.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

// la tabla intermedia es para una relacion de muchos a muchos
// cada fila dice qué investigador participa en que proyecto
@Entity(
    tableName = "investigador_proyecto",
    primaryKeys = ["investigadorId", "proyectoId"],
    foreignKeys = [
        ForeignKey(
            entity = Investigador::class,
            parentColumns = ["id"],
            childColumns = ["investigadorId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Proyecto::class,
            parentColumns = ["id"],
            childColumns = ["proyectoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["proyectoId"])]
)
data class InvestigadorProyecto(
    val investigadorId: Int,
    val proyectoId: Int
)