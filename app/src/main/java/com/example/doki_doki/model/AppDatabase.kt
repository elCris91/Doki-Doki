package com.example.doki_doki.model

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}

// base de datos de la app, acá se registraran todas las tablas