package com.samoggino.intermit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Definisci il tema scuro con il colore violetto
private val DarkColorScheme = darkColorScheme(
    primary = VioletDark, // Usa il violetto scuro per il tema scuro
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Definisci il tema chiaro con il colore violetto
private val LightColorScheme = lightColorScheme(
    primary = VioletLight, // Usa il violetto chiaro per il tema chiaro
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun IntermitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Se non vuoi usare il colore dinamico (Android 12+), imposta `dynamicColor = false`
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
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
