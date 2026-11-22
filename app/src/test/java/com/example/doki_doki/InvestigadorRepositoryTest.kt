package com.example.doki_doki

import com.example.doki_doki.model.Investigador
import com.example.doki_doki.model.Proyecto
import com.example.doki_doki.repository.InvestigadorRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class InvestigadorRepositoryTest {

    private val investigadoresXano = listOf(
        Investigador(
            id = 1,
            nombre = "Dra. Elena Rojas",
            linea = "Construcción en Madera",
            area = "Ingeniería",
            descripcion = "Especialista en CLT",
            correo = "elena@cenamad.cl"
        ),
        Investigador(
            id = 2,
            nombre = "Dr. Mateo Valdivia",
            linea = "Bioproductos",
            area = "Química",
            descripcion = "Lignina y bioplásticos",
            correo = "mateo@cenamad.cl"
        )
    )

    private val investigadoresRoom = listOf(
        Investigador(
            id = 99,
            nombre = "Investigador Local",
            linea = "Silvicultura",
            area = "Forestal",
            descripcion = "Respaldo offline",
            correo = "local@cenamad.cl"
        )
    )

    private val proyectosXano = listOf(
        Proyecto(
            id = 1,
            titulo = "Edificación en Altura con CLT",
            descripcion = "Torre en madera",
            linea = "construcción en madera",
            anio = 2025,
            estado = "Activo"
        ),
        Proyecto(
            id = 2,
            titulo = "Bioplásticos desde Lignina",
            descripcion = "Polímeros sustentables",
            linea = "Bioproductos",
            anio = 2025,
            estado = "Activo"
        )
    )

    @Test
    fun `obtenerInvestigadores retorna datos de Xano cuando hay conexion`() = runTest {
        val fakeApi = FakeXanoApiService(investigadores = investigadoresXano)
        val fakeDao = FakeInvestigadorDao(investigadoresLocales = investigadoresRoom)
        val repository = InvestigadorRepository(fakeDao, fakeApi)

        val resultado = repository.obtenerInvestigadores()

        assertEquals(2, resultado.size)
        assertEquals("Dra. Elena Rojas", resultado[0].nombre)
    }

    @Test
    fun `obtenerInvestigadores hace fallback a Room cuando falla internet`() = runTest {
        val fakeApi = FakeXanoApiService(simularSinInternet = true)
        val fakeDao = FakeInvestigadorDao(investigadoresLocales = investigadoresRoom)
        val repository = InvestigadorRepository(fakeDao, fakeApi)

        val resultado = repository.obtenerInvestigadores()

        assertEquals(1, resultado.size)
        assertEquals("Investigador Local", resultado[0].nombre)
    }

    @Test
    fun `obtenerProyectosDeInvestigador filtra proyectos por linea coincidente`() = runTest {
        val fakeApi = FakeXanoApiService(
            investigadores = investigadoresXano,
            proyectos = proyectosXano
        )
        val fakeDao = FakeInvestigadorDao(investigadoresLocales = investigadoresRoom)
        val repository = InvestigadorRepository(fakeDao, fakeApi)

        val proyectosElena = repository.obtenerProyectosDeInvestigador(1)

        assertEquals(1, proyectosElena.size)
        assertEquals("Edificación en Altura con CLT", proyectosElena[0].titulo)
    }
}