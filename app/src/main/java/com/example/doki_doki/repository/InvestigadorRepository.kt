package com.example.doki_doki.repository

import com.example.doki_doki.model.Investigador
import com.example.doki_doki.model.InvestigadorDao
import com.example.doki_doki.model.Proyecto
import com.example.doki_doki.model.ProyectoDao

// intermediario entre los viewmodels y las tablas de investigadores y proyectos
// recibe ambos dao para consultar las 2 tablas
class InvestigadorRepository(
    private val investigadorDao: InvestigadorDao,
    private val proyectoDao: ProyectoDao
) {

    // para el listado
    suspend fun obtenerInvestigadores(): List<Investigador> =
        investigadorDao.obtenerTodos()

    // para mostrar los proyectos de un investigador en el modal
    suspend fun obtenerProyectosDeInvestigador(investigadorId: Int): List<Proyecto> =
        investigadorDao.obtenerProyectosDeInvestigador(investigadorId)

    // para la pantalla de detalle de proyecto
    suspend fun obtenerProyecto(proyectoId: Int): Proyecto? =
        proyectoDao.obtenerPorId(proyectoId)

    suspend fun obtenerInvestigadoresDeProyecto(proyectoId: Int): List<Investigador> =
        proyectoDao.obtenerInvestigadoresDeProyecto(proyectoId)
}