package com.example.doki_doki.repository

import com.example.doki_doki.model.Investigador
import com.example.doki_doki.model.InvestigadorDao
import com.example.doki_doki.model.Proyecto

// intermediario entre el viewmodel del listado y la tabla de investigadores
class InvestigadorRepository(private val investigadorDao: InvestigadorDao) {

    // para el listado
    suspend fun obtenerInvestigadores(): List<Investigador> =
        investigadorDao.obtenerTodos()

    // para mostrar los proyectos de un investigador en el modal
    suspend fun obtenerProyectosDeInvestigador(investigadorId: Int): List<Proyecto> =
        investigadorDao.obtenerProyectosDeInvestigador(investigadorId)
}