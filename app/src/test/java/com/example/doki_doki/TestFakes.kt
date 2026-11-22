package com.example.doki_doki

import com.example.doki_doki.model.Investigador
import com.example.doki_doki.model.InvestigadorDao
import com.example.doki_doki.model.Proyecto
import com.example.doki_doki.model.Usuario
import com.example.doki_doki.model.UsuarioDao
import com.example.doki_doki.network.XanoApiService
import retrofit2.Response
import java.io.IOException

class FakeXanoApiService(
    private val investigadores: List<Investigador> = emptyList(),
    private val proyectos: List<Proyecto> = emptyList(),
    val usuariosRemotos: MutableList<Usuario> = mutableListOf(),
    var simularSinInternet: Boolean = false
) : XanoApiService {

    private fun verificarConexion() {
        if (simularSinInternet) throw IOException("Sin conexión a internet")
    }

    override suspend fun getInvestigadores(): List<Investigador> {
        verificarConexion()
        return investigadores
    }

    override suspend fun crearInvestigador(investigador: Investigador): Investigador {
        verificarConexion()
        return investigador
    }

    override suspend fun actualizarInvestigador(id: Int, investigador: Investigador): Investigador {
        verificarConexion()
        return investigador
    }

    override suspend fun eliminarInvestigador(id: Int): Response<Unit> {
        verificarConexion()
        return Response.success(Unit)
    }

    override suspend fun getProyectos(): List<Proyecto> {
        verificarConexion()
        return proyectos
    }

    override suspend fun crearProyecto(proyecto: Proyecto): Proyecto {
        verificarConexion()
        return proyecto
    }

    override suspend fun actualizarProyecto(id: Int, proyecto: Proyecto): Proyecto {
        verificarConexion()
        return proyecto
    }

    override suspend fun eliminarProyecto(id: Int): Response<Unit> {
        verificarConexion()
        return Response.success(Unit)
    }

    override suspend fun getUsuarios(): List<Usuario> {
        verificarConexion()
        return usuariosRemotos
    }

    override suspend fun registrarUsuario(usuario: Usuario): Usuario {
        verificarConexion()
        val creado = usuario.copy(id = usuariosRemotos.size + 1)
        usuariosRemotos.add(creado)
        return creado
    }
}

class FakeUsuarioDao(
    val usuariosLocales: MutableList<Usuario> = mutableListOf()
) : UsuarioDao {

    override suspend fun insertar(usuario: Usuario) {
        usuariosLocales.add(usuario)
    }

    override suspend fun buscarPorCorreo(correo: String): Usuario? {
        return usuariosLocales.find { it.correo.equals(correo, ignoreCase = true) }
    }
}

class FakeInvestigadorDao(
    private val investigadoresLocales: List<Investigador> = emptyList(),
    private val proyectosPorInvestigador: Map<Int, List<Proyecto>> = emptyMap()
) : InvestigadorDao {

    override suspend fun obtenerTodos(): List<Investigador> = investigadoresLocales

    override suspend fun obtenerProyectosDeInvestigador(investigadorId: Int): List<Proyecto> =
        proyectosPorInvestigador[investigadorId] ?: emptyList()
}