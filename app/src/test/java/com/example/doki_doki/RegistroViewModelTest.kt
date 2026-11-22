package com.example.doki_doki

import com.example.doki_doki.repository.UsuarioRepository
import com.example.doki_doki.viewmodel.RegistroViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun configurarDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun limpiarDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registrar con formulario invalido muestra errores y no registra`() {
        val repository = UsuarioRepository(FakeUsuarioDao(), FakeXanoApiService())
        val viewModel = RegistroViewModel(repository)

        viewModel.registrar()

        assertNotNull(viewModel.estado.value.errorNombre)
        assertNotNull(viewModel.estado.value.errorCorreo)
        assertNotNull(viewModel.estado.value.errorContrasena)
        assertNotNull(viewModel.estado.value.errorConfirmar)
        assertNotNull(viewModel.estado.value.errorTerminos)
        assertFalse(viewModel.estado.value.registroExitoso)
    }

    @Test
    fun `registrar con datos validos marca registroExitoso en true`() = runTest {
        val fakeApi = FakeXanoApiService()
        val repository = UsuarioRepository(FakeUsuarioDao(), fakeApi)
        val viewModel = RegistroViewModel(repository)

        viewModel.alCambiarNombre("Diego")
        viewModel.alCambiarCorreo("diego@cenamad.cl")
        viewModel.alCambiarContrasena("Madera2026")
        viewModel.alCambiarConfirmar("Madera2026")
        viewModel.alCambiarTerminos(true)
        viewModel.registrar()

        assertTrue(viewModel.estado.value.registroExitoso)
        assertEquals(1, fakeApi.usuariosRemotos.size)
    }

    @Test
    fun `registrar con correo duplicado muestra errorCorreo`() = runTest {
        val fakeApi = FakeXanoApiService()
        val repository = UsuarioRepository(FakeUsuarioDao(), fakeApi)
        repository.registrar("Diego", "diego@cenamad.cl", "Madera2026")

        val viewModel = RegistroViewModel(repository)
        viewModel.alCambiarNombre("Otro Diego")
        viewModel.alCambiarCorreo("diego@cenamad.cl")
        viewModel.alCambiarContrasena("Madera2026")
        viewModel.alCambiarConfirmar("Madera2026")
        viewModel.alCambiarTerminos(true)
        viewModel.registrar()

        assertFalse(viewModel.estado.value.registroExitoso)
        assertEquals("Este correo ya está registrado", viewModel.estado.value.errorCorreo)
    }
}