package com.example.doki_doki

import com.example.doki_doki.repository.ValidadorFormulario
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorFormularioTest {

    @Test
    fun `validarCorreo acepta correo bien formado y rechaza vacio o invalido`() {
        assertNull(ValidadorFormulario.validarCorreo("elena.rojas@cenamad.cl"))
        assertEquals("Ingresa tu correo", ValidadorFormulario.validarCorreo("   "))
        assertEquals("Ingresa un correo válido", ValidadorFormulario.validarCorreo("correo-sin-arroba"))
    }

    @Test
    fun `validarNombre exige minimo 3 letras`() {
        assertNull(ValidadorFormulario.validarNombre("Diego"))
        assertNotNull(ValidadorFormulario.validarNombre(""))
        assertNotNull(ValidadorFormulario.validarNombre("Al"))
    }

    @Test
    fun `validarContrasenaRegistro exige 8 caracteres y al menos un numero`() {
        assertNull(ValidadorFormulario.validarContrasenaRegistro("Madera2026"))
        assertEquals("Ingresa una contraseña", ValidadorFormulario.validarContrasenaRegistro(""))
        assertEquals("La contraseña debe tener al menos 8 caracteres", ValidadorFormulario.validarContrasenaRegistro("Mad123"))
        assertEquals("La contraseña debe tener al menos un número", ValidadorFormulario.validarContrasenaRegistro("MaderaSinNumero"))
    }

    @Test
    fun `validarConfirmacion y validarTerminos validan coincidencia y aceptacion`() {
        assertNull(ValidadorFormulario.validarConfirmacion("Madera2026", "Madera2026"))
        assertEquals("Las contraseñas no coinciden", ValidadorFormulario.validarConfirmacion("Madera2026", "OtraClave1"))
        assertNull(ValidadorFormulario.validarTerminos(true))
        assertEquals("Debes aceptar los términos y condiciones", ValidadorFormulario.validarTerminos(false))
    }
}