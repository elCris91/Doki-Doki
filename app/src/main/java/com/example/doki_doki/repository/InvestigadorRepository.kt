package com.example.doki_doki.repository

import com.example.doki_doki.model.Investigador
import com.example.doki_doki.model.InvestigadorDao
import com.example.doki_doki.model.Proyecto
import com.example.doki_doki.network.RetrofitClient
import com.example.doki_doki.network.XanoApiService

// intermediario entre el viewmodel del listado y los datos de investigadores (Xano + Room)
class InvestigadorRepository(
    private val investigadorDao: InvestigadorDao,
    private val apiService: XanoApiService = RetrofitClient.apiService
) {

    // para el listado: consulta primero en Xano y usa Room como respaldo
    suspend fun obtenerInvestigadores(): List<Investigador> {
        return try {
            val remotos = apiService.getInvestigadores()
            remotos.ifEmpty { investigadorDao.obtenerTodos() }
        } catch (e: Exception) {
            investigadorDao.obtenerTodos()
        }
    }

    // para mostrar los proyectos de un investigador en el modal (cruzando por campo "linea")
    suspend fun obtenerProyectosDeInvestigador(investigadorId: Int): List<Proyecto> {
        return try {
            val proyectosRemotos = apiService.getProyectos()
            val investigador = apiService.getInvestigadores()
                .find { it.id == investigadorId }

            if (investigador != null && proyectosRemotos.isNotEmpty()) {
                proyectosRemotos.filter {
                    it.linea.trim().equals(investigador.linea.trim(), ignoreCase = true)
                }
            } else {
                investigadorDao.obtenerProyectosDeInvestigador(investigadorId)
            }
        } catch (e: Exception) {
            investigadorDao.obtenerProyectosDeInvestigador(investigadorId)
        }
    }
}