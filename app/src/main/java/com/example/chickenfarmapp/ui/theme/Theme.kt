package com.example.chickenfarmapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ChickenDarkScheme = darkColorScheme(
    primary = ChickenYellow80, secondary = GrassGreen80, tertiary = RoosterRed80,
    background = DarkFarmBackground, surface = DarkFarmSurface, surfaceVariant = DarkFarmSurfaceVariant,
    onPrimary = ChickenYellow20, onBackground = FarmSurface, onSurface = FarmSurface,
)
private val ChickenLightScheme = lightColorScheme(
    primary = ChickenYellow40, secondary = GrassGreen40, tertiary = RoosterRed40,
    background = FarmBackground, surface = FarmSurface, surfaceVariant = FarmSurfaceVariant,
    onPrimary = FarmSurface, onBackground = ChickenYellow20, onSurface = ChickenYellow20,
)

@Composable
fun ChickenFarmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> ChickenDarkScheme
        else -> ChickenLightScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}