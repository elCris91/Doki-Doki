package com.example.doki_doki.model

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ProyectoDao {

    @Query("SELECT * FROM proyectos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Proyecto?

    // investigadores que participan en un proyecto
    @Query(
        """
        SELECT investigadores.* FROM investigadores
        INNER JOIN investigador_proyecto ON investigadores.id = investigador_proyecto.investigadorId
        WHERE investigador_proyecto.proyectoId = :proyectoId
        ORDER BY investigadores.nombre
        """
    )
    suspend fun obtenerInvestigadoresDeProyecto(proyectoId: Int): List<Investigador>
}