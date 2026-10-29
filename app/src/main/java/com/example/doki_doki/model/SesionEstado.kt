package com.example.doki_doki.model

// estado de la sesión de toda la app
// revisada: si ya se buscó una sesión guardada al abrir la app
// usuario: el usuario conectado, o null si nadie inició sesión
data class SesionEstado(
    val revisada: Boolean = false,
    val usuario: Usuario? = null
)