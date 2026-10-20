package com.example.doki_doki.model

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
object DatosIniciales : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL(
            """
            INSERT INTO usuarios (nombre, correo, contrasenaHash)
            VALUES ('Usuario Demo', 'demo@cenamad.cl',
                    'b22f213ec710f0b0e86297d10279d69171f50f01a04edf40f472a563e7ad8576')
            """.trimIndent()
        )
    }
}


// el usuario de prueba es demo@cenamad.cl Demo1234