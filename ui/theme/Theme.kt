package com.alubarika.store.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BrandGreen = Color(0xFF2E7D32)
private val BrandGold = Color(0xFFF9A825)

private val LightColors = lightColorScheme(
    primary = BrandGreen,
    secondary = BrandGold
)

private val DarkColors = darkColorScheme(
    primary = BrandGreen,
    secondary = BrandGold
)

@Composable
fun AlubarikaStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
