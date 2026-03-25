package edu.moravian.csci215.tic_tac_toe

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Pine900 = Color(0xFF041A13)
val Pine800 = Color(0xFF0A2A1E)
val Pine700 = Color(0xFF123D2C)
val Pine600 = Color(0xFF1B5A41)
val Moss500 = Color(0xFF2E7A56)
val Mint200 = Color(0xFFA8D7C0)
val Fog100 = Color(0xFFF5FBF8)
val White = Color(0xFFFFFFFF)

private val EvergreenColors =
    darkColorScheme(
        primary = White,
        onPrimary = Pine800,
        primaryContainer = Pine600,
        onPrimaryContainer = White,
        secondary = Mint200,
        onSecondary = Pine800,
        secondaryContainer = Pine700,
        onSecondaryContainer = Fog100,
        tertiary = Color(0xFFBCE8D1),
        onTertiary = Pine800,
        background = Pine900,
        onBackground = White,
        surface = Color(0xFF0D241C),
        onSurface = White,
        surfaceVariant = Color(0xFF16372A),
        onSurfaceVariant = Color(0xFFD7E9E0),
        outline = Color(0xFF6EA98B),
        outlineVariant = Color(0xFF244A39),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
    )

private val AppTypography =
    Typography(
        headlineLarge =
            TextStyle(
                fontWeight = FontWeight.Black,
                fontSize = 46.sp,
                lineHeight = 48.sp,
                letterSpacing = (-1.1).sp,
            ),
        headlineMedium =
            TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 34.sp,
                lineHeight = 38.sp,
                letterSpacing = (-0.6).sp,
            ),
        headlineSmall =
            TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                letterSpacing = (-0.4).sp,
            ),
        titleLarge =
            TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 28.sp,
                letterSpacing = (-0.2).sp,
            ),
        titleMedium =
            TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.1.sp,
            ),
        bodyLarge =
            TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp,
            ),
        bodyMedium =
            TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp,
            ),
        labelLarge =
            TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 1.1.sp,
            ),
    )

@Composable
fun TicTacToeTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = EvergreenColors,
        typography = AppTypography,
        content = content,
    )
}
