package com.example.doki_doki.model

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object DatosIniciales : RoomDatabase.Callback() {

    // esto se ejecuta una sola vez, cuando se crea la base de datos
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        db.execSQL(
            """
            INSERT INTO usuarios (nombre, correo, contrasenaHash)
            VALUES ('Usuario Demo', 'demo@cenamad.cl',
                    'b22f213ec710f0b0e86297d10279d69171f50f01a04edf40f472a563e7ad8576')
            """.trimIndent()
        )

        // investigadores inventados, repartidos en 3 líneas de investigación
        db.execSQL(
            """
            INSERT INTO investigadores (id, nombre, linea, area, descripcion, correo) VALUES
            (1, 'Camila Rojas Fuentes', 'Construcción en madera', 'Ingeniería estructural',
                'Estudia el comportamiento sísmico de edificios de mediana altura construidos en madera.', 'camila.rojas@cenamad.cl'),
            (2, 'Matías Herrera Soto', 'Construcción en madera', 'Arquitectura',
                'Diseña sistemas de vivienda industrializada en madera para zonas urbanas.', 'matias.herrera@cenamad.cl'),
            (3, 'Valentina Muñoz Pino', 'Construcción en madera', 'Eficiencia energética',
                'Investiga el aislamiento térmico y el confort en viviendas de madera.', 'valentina.munoz@cenamad.cl'),
            (4, 'Javier Contreras Lagos', 'Biomateriales', 'Química de la madera',
                'Desarrolla adhesivos y recubrimientos de origen natural para productos de madera.', 'javier.contreras@cenamad.cl'),
            (5, 'Fernanda Silva Ortiz', 'Biomateriales', 'Nanocelulosa',
                'Trabaja en materiales a partir de celulosa para fabricar envases biodegradables.', 'fernanda.silva@cenamad.cl'),
            (6, 'Nicolás Vergara Díaz', 'Biomateriales', 'Bioenergía',
                'Estudia el aprovechamiento de residuos forestales para producir energía.', 'nicolas.vergara@cenamad.cl'),
            (7, 'Antonia Reyes Campos', 'Bosques y sustentabilidad', 'Manejo forestal',
                'Analiza prácticas de manejo sostenible en plantaciones del centro sur de Chile.', 'antonia.reyes@cenamad.cl'),
            (8, 'Sebastián Morales Vidal', 'Bosques y sustentabilidad', 'Huella de carbono',
                'Mide cuánto carbono almacenan las construcciones de madera durante su vida útil.', 'sebastian.morales@cenamad.cl')
            """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO proyectos (id, titulo, descripcion, linea, anio, estado) VALUES
            (1, 'Edificios de madera resistentes a sismos',
                'Ensayos de muros y uniones para edificios de madera de hasta 8 pisos.', 'Construcción en madera', 2024, 'En curso'),
            (2, 'Vivienda social industrializada',
                'Prototipo de vivienda social prefabricada en madera, rápida de montar.', 'Construcción en madera', 2023, 'Finalizado'),
            (3, 'Adhesivos naturales para tableros',
                'Reemplazo de adhesivos derivados del petróleo por alternativas de origen vegetal.', 'Biomateriales', 2025, 'En curso'),
            (4, 'Envases biodegradables de nanocelulosa',
                'Desarrollo de envases para alimentos que se degradan en pocos meses.', 'Biomateriales', 2022, 'Finalizado'),
            (5, 'Plantaciones sostenibles y biodiversidad',
                'Evaluación del efecto de distintos tipos de manejo forestal en la flora y fauna.', 'Bosques y sustentabilidad', 2024, 'En curso'),
            (6, 'Carbono almacenado en la construcción',
                'Cálculo del carbono que guardan las viviendas de madera comparadas con las de hormigón.', 'Bosques y sustentabilidad', 2025, 'En curso')
            """.trimIndent()
        )

        // quien participa en cada proyecto (investigadorId, proyectoId)
        db.execSQL(
            """
            INSERT INTO investigador_proyecto (investigadorId, proyectoId) VALUES
            (1, 1), (2, 1),
            (2, 2), (3, 2),
            (4, 3), (5, 3),
            (5, 4), (6, 4),
            (7, 5), (8, 5),
            (8, 6), (1, 6), (3, 6), (6, 6)
            """.trimIndent()
        )
    }
}


// el usuario de prueba es demo@cenamad.cl Demo1234 (INICIO DE SESION)