package com.example.doki_doki

import com.example.doki_doki.repository.UsuarioRepository
import com.example.doki_doki.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

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
    fun `iniciarSesion con campos vacios marca errores de validacion`() {
        val repository = UsuarioRepository(FakeUsuarioDao(), FakeXanoApiService())
        val viewModel = LoginViewModel(repository)

        viewModel.iniciarSesion()

        assertNotNull(viewModel.estado.value.errorCorreo)
        assertNotNull(viewModel.estado.value.errorContrasena)
        assertNull(viewModel.estado.value.usuarioIngresado)
    }

    @Test
    fun `iniciarSesion exitoso actualiza usuarioIngresado en el estado`() = runTest {
        val fakeApi = FakeXanoApiService()
        val fakeDao = FakeUsuarioDao()
        val repository = UsuarioRepository(fakeDao, fakeApi)
        repository.registrar("Diego", "diego@cenamad.cl", "Clave1234")

        val viewModel = LoginViewModel(repository)
        viewModel.alCambiarCorreo("diego@cenamad.cl")
        viewModel.alCambiarContrasena("Clave1234")
        viewModel.iniciarSesion()

        assertNotNull(viewModel.estado.value.usuarioIngresado)
        assertEquals("Diego", viewModel.estado.value.usuarioIngresado?.nombre)
        assertNull(viewModel.estado.value.errorGeneral)
    }

    @Test
    fun `iniciarSesion con credenciales invalidas muestra errorGeneral`() = runTest {
        val repository = UsuarioRepository(FakeUsuarioDao(), FakeXanoApiService())
        val viewModel = LoginViewModel(repository)

        viewModel.alCambiarCorreo("nadie@cenamad.cl")
        viewModel.alCambiarContrasena("Clave1234")
        viewModel.iniciarSesion()

        assertNull(viewModel.estado.value.usuarioIngresado)
        assertEquals("Correo o contraseña incorrectos", viewModel.estado.value.errorGeneral)
    }
}