package com.ishika.githubexplorer.presentation.theme

import android.graphics.Color as AndroidColor
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val ColorPurpleContainerLight = Color(0xFFE8E2FF)
private val ColorPurpleContainerDark = Color(0xFF3F356B)

private val LightColorScheme = lightColorScheme(
    primary = GitHubPurple,
    onPrimary = LightSurface,
    primaryContainer = ColorPurpleContainerLight,
    onPrimaryContainer = LightTextPrimary,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightOutline,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = GitHubPurpleDark,
    onPrimary = DarkBackground,
    primaryContainer = ColorPurpleContainerDark,
    onPrimaryContainer = DarkTextPrimary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkOutline,
    error = Color(0xFFFFB4AB)
)

private val LightSystemBarScrim =
    AndroidColor.rgb(247, 247, 251)

private val DarkSystemBarScrim =
    AndroidColor.rgb(17, 17, 22)

@Composable
fun GitHubExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

fun ComponentActivity.configureSystemBars() {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            lightScrim = LightSystemBarScrim,
            darkScrim = DarkSystemBarScrim
        ),
        navigationBarStyle = SystemBarStyle.auto(
            lightScrim = LightSystemBarScrim,
            darkScrim = DarkSystemBarScrim
        )
    )
}