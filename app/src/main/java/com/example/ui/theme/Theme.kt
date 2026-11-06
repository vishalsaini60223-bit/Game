package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LuxuryDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = LuxuryBlack,
    primaryContainer = GoldContainer,
    onPrimaryContainer = GoldLight,
    secondary = EmeraldProfit,
    onSecondary = LuxuryBlack,
    secondaryContainer = EmeraldContainer,
    onSecondaryContainer = EmeraldProfit,
    tertiary = ElectricBlue,
    onTertiary = LuxuryBlack,
    background = LuxuryDarkBg,
    onBackground = TextPlatinum,
    surface = LuxurySurface,
    onSurface = TextPlatinum,
    surfaceVariant = LuxurySurfaceVariant,
    onSurfaceVariant = TextSilver,
    outline = LuxuryCardBorder,
    error = CrimsonError
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = LuxuryBlack.toArgb()
            window.navigationBarColor = LuxuryBlack.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = LuxuryDarkColorScheme,
        typography = Typography,
        content = content
    )
}
