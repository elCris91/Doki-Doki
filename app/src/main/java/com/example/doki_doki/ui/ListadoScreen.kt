package com.example.doki_doki.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.doki_doki.model.Investigador
import com.example.doki_doki.viewmodel.ListadoViewModel

// pantalla principal: listado de investigadores con buscador, filtro por línea y modal de detalle
@Composable
fun ListadoScreen(viewModel: ListadoViewModel) {
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Investigadores",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))

        // buscador por nombre
        OutlinedTextField(
            value = estado.busqueda,
            onValueChange = viewModel::alCambiarBusqueda,
            label = { Text("Buscar por nombre") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // filtro por línea, se puede deslizar hacia el lado; "Todas" quita el filtro
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = estado.lineaSeleccionada == null,
                onClick = { viewModel.alSeleccionarLinea(null) },
                label = { Text("Todas") }
            )
            estado.lineas.forEach { linea ->
                FilterChip(
                    selected = estado.lineaSeleccionada == linea,
                    onClick = { viewModel.alSeleccionarLinea(linea) },
                    label = { Text(linea) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            estado.cargando -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            estado.investigadoresFiltrados.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No se encontraron investigadores",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // animateItem anima las tarjetas cuando aparecen o desaparecen al filtrar
                    items(estado.investigadoresFiltrados, key = { it.id }) { investigador ->
                        TarjetaInvestigador(
                            investigador = investigador,
                            alTocar = { viewModel.abrirDetalle(investigador) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }

    // modal con el detalle del investigador y sus proyectos
    estado.investigadorSeleccionado?.let { investigador ->
        AlertDialog(
            onDismissRequest = viewModel::cerrarDetalle,
            title = { Text(investigador.nombre) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${investigador.area} · ${investigador.linea}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(investigador.descripcion)
                    Text(
                        text = investigador.correo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Proyectos", fontWeight = FontWeight.Bold)
                    estado.proyectosDelSeleccionado.forEach { proyecto ->
                        Text("• ${proyecto.titulo} (${proyecto.anio})")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = viewModel::cerrarDetalle) { Text("Cerrar") }
            }
        )
    }
}

// tarjeta de cada investigador: círculo con iniciales, nombre, línea y área
@Composable
private fun TarjetaInvestigador(
    investigador: Investigador,
    alTocar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = alTocar,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = iniciales(investigador.nombre),
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = investigador.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = investigador.linea,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = investigador.area,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// saca las iniciales para el círculo, ej: "Camila Rojas Fuentes" -> "CR"
private fun iniciales(nombre: String): String =
    nombre.split(" ")
        .take(2)
        .mapNotNull { palabra -> palabra.firstOrNull()?.uppercase() }
        .joinToString("")