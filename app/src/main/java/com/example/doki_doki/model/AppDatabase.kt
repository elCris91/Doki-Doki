package com.example.doki_doki.model

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Usuario::class, Investigador::class, Proyecto::class, InvestigadorProyecto::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun investigadorDao(): InvestigadorDao
    abstract fun proyectoDao(): ProyectoDao
}

// base de datos de la app, aca se registraran todas las tablas