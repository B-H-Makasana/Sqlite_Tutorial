package com.example.demo1.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.demo1.R

private val DarkColorPalette = darkColors(
    primary = Color(R.color.colorPrimary),
    primaryVariant = Color(R.color.transparent),
    secondary = Color(R.color.white)
)

private val LightColorPalette = lightColors(
    primary = Color(R.color.colorPrimary),
    primaryVariant = Color(R.color.transparent),
    secondary = Color(R.color.white),
    secondaryVariant = Color(R.color.white)

//    background = Color(R.color.colorPrimary)
    /* Other default colors to override
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun Demo1Theme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,

    )
}