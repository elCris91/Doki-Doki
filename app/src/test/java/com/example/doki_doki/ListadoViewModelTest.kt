package com.example.doki_doki

import com.example.doki_doki.model.Investigador
import com.example.doki_doki.model.Proyecto
import com.example.doki_doki.repository.InvestigadorRepository
import com.example.doki_doki.viewmodel.ListadoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListadoViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val listaPrueba = listOf(
        Investigador(1, "Dra. Elena Rojas", "Construcción en Madera", "Ingeniería", "Desc", "elena@cenamad.cl"),
        Investigador(2, "Dr. Mateo Valdivia", "Bioproductos", "Química", "Desc", "mateo@cenamad.cl"),
        Investigador(3, "Dra. Sofía Contreras", "Sustentabilidad", "Forestal", "Desc", "sofia@cenamad.cl")
    )

    private val proyectosPrueba = listOf(
        Proyecto(1, "Edificación en Altura con CLT", "Desc", "Construcción en Madera", 2025, "Activo")
    )

    @Before
    fun configurarDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun limpiarDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al iniciar carga investigadores y lineas unicas desde el repositorio`() {
        val repository = InvestigadorRepository(
            FakeInvestigadorDao(),
            FakeXanoApiService(investigadores = listaPrueba)
        )
        val viewModel = ListadoViewModel(repository)

        assertEquals(3, viewModel.estado.value.investigadores.size)
        assertEquals(3, viewModel.estado.value.investigadoresFiltrados.size)
        assertEquals(3, viewModel.estado.value.lineas.size)
    }

    @Test
    fun `alCambiarBusqueda y alSeleccionarLinea filtran el listado`() {
        val repository = InvestigadorRepository(
            FakeInvestigadorDao(),
            FakeXanoApiService(investigadores = listaPrueba)
        )
        val viewModel = ListadoViewModel(repository)

        viewModel.alCambiarBusqueda("sofía")
        assertEquals(1, viewModel.estado.value.investigadoresFiltrados.size)
        assertEquals("Dra. Sofía Contreras", viewModel.estado.value.investigadoresFiltrados[0].nombre)

        viewModel.alCambiarBusqueda("")
        viewModel.alSeleccionarLinea("Bioproductos")
        assertEquals(1, viewModel.estado.value.investigadoresFiltrados.size)
        assertEquals("Dr. Mateo Valdivia", viewModel.estado.value.investigadoresFiltrados[0].nombre)
    }

    @Test
    fun `abrirDetalle y cerrarDetalle gestionan el modal de proyectos`() {
        val repository = InvestigadorRepository(
            FakeInvestigadorDao(),
            FakeXanoApiService(investigadores = listaPrueba, proyectos = proyectosPrueba)
        )
        val viewModel = ListadoViewModel(repository)

        viewModel.abrirDetalle(listaPrueba[0])
        assertNotNull(viewModel.estado.value.investigadorSeleccionado)
        assertEquals(1, viewModel.estado.value.proyectosDelSeleccionado.size)

        viewModel.cerrarDetalle()
        assertNull(viewModel.estado.value.investigadorSeleccionado)
        assertEquals(0, viewModel.estado.value.proyectosDelSeleccionado.size)
    }
}