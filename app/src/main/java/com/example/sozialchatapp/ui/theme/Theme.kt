package com.example.sozialchatapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    background = LightBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    background = DarkBackground,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White
)

@Composable
fun SozialChatAppTheme(
    darkTheme: Boolean = false, // Optional: isSystemInDarkTheme()
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}