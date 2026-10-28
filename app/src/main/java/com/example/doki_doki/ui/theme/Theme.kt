package com.example.doki_doki.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = VerdeClaro,
    secondary = TonoMadera,
    tertiary = VerdeCenamad,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Blanco,
    onSecondary = Blanco,
    onBackground = Blanco,
    onSurface = Blanco
)

private val LightColorScheme = lightColorScheme(
    primary = VerdeCenamad,
    secondary = TonoMadera,
    tertiary = VerdeClaro,
    background = FondoGrisSuave,
    surface = Blanco,
    onPrimary = Blanco,
    onSecondary = Blanco,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun DokiDokiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Se cambia a false para forzar el uso de los colores de CENAMAD
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}