package com.example.player_test.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Gradient colors as specified in CLAUDE.md
val Indigo = Color(0xFF4F46E5)
val Violet = Color(0xFF7C3AED)
val Magenta = Color(0xFFDB2777)

// Dark mode variants (slightly deepened)
val IndigoDark = Color(0xFF3730A3)
val VioletDark = Color(0xFF5B21B6)
val MagentaDark = Color(0xFFBE185D)

// Material 3 color scheme colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Gradient helper function
fun gradientBrush(darkTheme: Boolean = false): Brush {
    return if (darkTheme) {
        Brush.linearGradient(listOf(IndigoDark, VioletDark, MagentaDark))
    } else {
        Brush.linearGradient(listOf(Indigo, Violet, Magenta))
    }
}