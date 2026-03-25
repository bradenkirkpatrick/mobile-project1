package edu.moravian.csci215.tic_tac_toe

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

private val LightColors =
    lightColorScheme(
        primary = Color(0xFF1E5B52),
        onPrimary = Color(0xFFF7FAF8),
        primaryContainer = Color(0xFFB8E2D7),
        onPrimaryContainer = Color(0xFF0B312B),
        secondary = Color(0xFF8C4F3D),
        onSecondary = Color(0xFFFFF8F6),
        secondaryContainer = Color(0xFFF3D0C3),
        onSecondaryContainer = Color(0xFF442116),
        tertiary = Color(0xFF596A8B),
        onTertiary = Color(0xFFF8F9FF),
        tertiaryContainer = Color(0xFFD8E2FF),
        onTertiaryContainer = Color(0xFF132544),
        background = Color(0xFFF6F0E8),
        onBackground = Color(0xFF221D19),
        surface = Color(0xFFFFF8F3),
        onSurface = Color(0xFF221D19),
        surfaceVariant = Color(0xFFE8DED4),
        onSurfaceVariant = Color(0xFF514740),
        outline = Color(0xFF85766A),
    )

private val DarkColors =
    darkColorScheme(
        primary = Color(0xFF9FD0C4),
        onPrimary = Color(0xFF063730),
        primaryContainer = Color(0xFF1E5B52),
        onPrimaryContainer = Color(0xFFB8E2D7),
        secondary = Color(0xFFE0B9AB),
        onSecondary = Color(0xFF532B1E),
        secondaryContainer = Color(0xFF8C4F3D),
        onSecondaryContainer = Color(0xFFF3D0C3),
        tertiary = Color(0xFFBEC7EA),
        onTertiary = Color(0xFF283857),
        tertiaryContainer = Color(0xFF40506F),
        onTertiaryContainer = Color(0xFFD8E2FF),
        background = Color(0xFF171411),
        onBackground = Color(0xFFEBE1D8),
        surface = Color(0xFF1F1B18),
        onSurface = Color(0xFFEBE1D8),
        surfaceVariant = Color(0xFF514740),
        onSurfaceVariant = Color(0xFFD4C3B8),
        outline = Color(0xFF9F8F83),
    )

@Composable
fun TicTacToeTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content,
    )
}
