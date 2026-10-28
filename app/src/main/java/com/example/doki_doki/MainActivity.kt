package com.example.doki_doki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.example.doki_doki.model.AppDatabase
import com.example.doki_doki.model.DatosIniciales
import com.example.doki_doki.repository.InvestigadorRepository
import com.example.doki_doki.repository.UsuarioRepository
import com.example.doki_doki.ui.Navegacion
import com.example.doki_doki.ui.theme.DokiDokiTheme

class MainActivity : ComponentActivity() {

    // la base de datos se crea recién cuando se usa por primera vez
    private val baseDeDatos by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "cenamad_db")
            .addCallback(DatosIniciales)
            // si cambiamos las tablas mientras desarrollamos, se borra y se crea de nuevo
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    private val usuarioRepository by lazy { UsuarioRepository(baseDeDatos.usuarioDao()) }

    private val investigadorRepository by lazy {
        InvestigadorRepository(baseDeDatos.investigadorDao(), baseDeDatos.proyectoDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DokiDokiTheme {
                Navegacion(
                    usuarioRepository = usuarioRepository,
                    investigadorRepository = investigadorRepository
                )
            }
        }
    }
}