package com.example.doki_doki.model

// lo que muestra la pantalla del listado
data class ListadoEstado(
    val investigadores: List<Investigador> = emptyList(),          // lista completa de la base de datos
    val investigadoresFiltrados: List<Investigador> = emptyList(), // lo que se ve después de buscar/filtrar
    val lineas: List<String> = emptyList(),                        // lineas para los botones del filtro
    val busqueda: String = "",
    val lineaSeleccionada: String? = null,                         // null significa "odas
    val investigadorSeleccionado: Investigador? = null,            // si no es null se abre el modal
    val proyectosDelSeleccionado: List<Proyecto> = emptyList(),
    val cargando: Boolean = true
)