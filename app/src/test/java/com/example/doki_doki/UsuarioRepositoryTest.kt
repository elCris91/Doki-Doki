package com.example.doki_doki

import com.example.doki_doki.repository.UsuarioRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UsuarioRepositoryTest {

    @Test
    fun `registrar guarda usuario en Xano y crea respaldo en Room`() = runTest {
        val fakeApi = FakeXanoApiService()
        val fakeDao = FakeUsuarioDao()
        val repository = UsuarioRepository(fakeDao, fakeApi)

        repository.registrar("Diego", "diego@cenamad.cl", "Clave1234")

        assertEquals(1, fakeApi.usuariosRemotos.size)
        assertEquals(1, fakeDao.usuariosLocales.size)
        assertEquals("diego@cenamad.cl", fakeApi.usuariosRemotos[0].correo)
    }

    @Test
    fun `iniciarSesion autentica desde Xano con credenciales validas`() = runTest {
        val fakeApi = FakeXanoApiService()
        val fakeDao = FakeUsuarioDao()
        val repository = UsuarioRepository(fakeDao, fakeApi)

        repository.registrar("Diego", "diego@cenamad.cl", "Clave1234")
        val usuario = repository.iniciarSesion("diego@cenamad.cl", "Clave1234")

        assertNotNull(usuario)
        assertEquals("Diego", usuario?.nombre)
    }

    @Test
    fun `iniciarSesion usa respaldo de Room cuando falla la red`() = runTest {
        val fakeApi = FakeXanoApiService()
        val fakeDao = FakeUsuarioDao()
        val repository = UsuarioRepository(fakeDao, fakeApi)

        repository.registrar("Diego Offline", "offline@cenamad.cl", "Clave1234")

        fakeApi.simularSinInternet = true
        val usuario = repository.iniciarSesion("offline@cenamad.cl", "Clave1234")

        assertNotNull(usuario)
        assertEquals("Diego Offline", usuario?.nombre)
    }

    @Test
    fun `iniciarSesion retorna null con contrasena incorrecta`() = runTest {
        val fakeApi = FakeXanoApiService()
        val fakeDao = FakeUsuarioDao()
        val repository = UsuarioRepository(fakeDao, fakeApi)

        repository.registrar("Diego", "diego@cenamad.cl", "Clave1234")
        val usuario = repository.iniciarSesion("diego@cenamad.cl", "ClaveMala")

        assertNull(usuario)
    }

    @Test
    fun `existeCorreo detecta correo ya registrado`() = runTest {
        val fakeApi = FakeXanoApiService()
        val fakeDao = FakeUsuarioDao()
        val repository = UsuarioRepository(fakeDao, fakeApi)

        repository.registrar("Diego", "diego@cenamad.cl", "Clave1234")

        assertTrue(repository.existeCorreo("DIEGO@cenamad.cl"))
    }
}