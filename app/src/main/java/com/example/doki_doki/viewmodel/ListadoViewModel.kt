package com.example.doki_doki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.doki_doki.model.Investigador
import com.example.doki_doki.model.ListadoEstado
import com.example.doki_doki.repository.InvestigadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// la logica del listado es  cargar los investigadores, buscar, filtrar por línea y abre el modal
class ListadoViewModel(private val investigadorRepository: InvestigadorRepository) : ViewModel() {

    private val _estado = MutableStateFlow(ListadoEstado())
    val estado: StateFlow<ListadoEstado> = _estado.asStateFlow()

    // apenas se crea el viewmodel se cargan los datos
    init {
        cargarInvestigadores()
    }

    private fun cargarInvestigadores() {
        viewModelScope.launch {
            val lista = investigadorRepository.obtenerInvestigadores()
            _estado.update {
                it.copy(
                    investigadores = lista,
                    investigadoresFiltrados = lista,
                    lineas = lista.map { investigador -> investigador.linea }.distinct(),
                    cargando = false
                )
            }
        }
    }

    fun alCambiarBusqueda(texto: String) {
        _estado.update { it.copy(busqueda = texto) }
        aplicarFiltros()
    }

    // si llega null se muestran todas las líneas
    fun alSeleccionarLinea(linea: String?) {
        _estado.update { it.copy(lineaSeleccionada = linea) }
        aplicarFiltros()
    }

    // deja solo los investigadores que cumplen con la búsqueda y la línea elegida
    private fun aplicarFiltros() {
        _estado.update { actual ->
            val filtrados = actual.investigadores.filter { investigador ->
                val coincideLinea = actual.lineaSeleccionada == null ||
                        investigador.linea == actual.lineaSeleccionada
                val coincideNombre = actual.busqueda.isBlank() ||
                        investigador.nombre.contains(actual.busqueda.trim(), ignoreCase = true)
                coincideLinea && coincideNombre
            }
            actual.copy(investigadoresFiltrados = filtrados)
        }
    }

    // al tocar un investigador se buscan sus proyectos y se abre el modal
    fun abrirDetalle(investigador: Investigador) {
        viewModelScope.launch {
            val proyectos = investigadorRepository.obtenerProyectosDeInvestigador(investigador.id)
            _estado.update {
                it.copy(investigadorSeleccionado = investigador, proyectosDelSeleccionado = proyectos)
            }
        }
    }

    fun cerrarDetalle() {
        _estado.update { it.copy(investigadorSeleccionado = null, proyectosDelSeleccionado = emptyList()) }
    }

    companion object {
        fun fabrica(investigadorRepository: InvestigadorRepository) = viewModelFactory {
            initializer { ListadoViewModel(investigadorRepository) }
        }
    }
}