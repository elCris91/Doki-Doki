package com.example.doki_doki.model

import androidx.room.Dao
import androidx.room.Query

@Dao
interface InvestigadorDao {

    // trae a todos los investigadores ordenados por nombre (para el listado)
    @Query("SELECT * FROM investigadores ORDER BY nombre")
    suspend fun obtenerTodos(): List<Investigador>



    // proyectos donde participa un investigador, se une con la tabla intermedia
    @Query(
        """
        SELECT proyectos.* FROM proyectos
        INNER JOIN investigador_proyecto ON proyectos.id = investigador_proyecto.proyectoId
        WHERE investigador_proyecto.investigadorId = :investigadorId
        ORDER BY proyectos.anio DESC
        """
    )
    suspend fun obtenerProyectosDeInvestigador(investigadorId: Int): List<Proyecto>
}