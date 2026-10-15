package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NervaColorScheme = darkColorScheme(
    primary = NervaCyan,
    onPrimary = NervaBlueDeep,
    primaryContainer = NervaBlueElectric,
    onPrimaryContainer = NervaWhite,
    secondary = NervaBlueAzure,
    onSecondary = NervaBlueDeep,
    secondaryContainer = NervaBlueContainer,
    onSecondaryContainer = NervaWhite,
    tertiary = NervaGold,
    onTertiary = NervaBlueDeep,
    background = NervaBlueDeep,
    onBackground = NervaWhite,
    surface = NervaBlueSurface,
    onSurface = NervaWhite,
    surfaceVariant = NervaBlueSurfaceVariant,
    onSurfaceVariant = NervaTextMuted,
    outline = NervaBlueContainer,
    outlineVariant = NervaBlueSurfaceVariant
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = NervaBlueDeep.toArgb()
                window.navigationBarColor = NervaBlueDeep.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = NervaColorScheme,
        typography = Typography,
        content = content
    )
}
