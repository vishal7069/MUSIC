package com.dafli.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

private val scheme = darkColorScheme(
    primary = DColor.Marigold,
    onPrimary = DColor.Ink,
    background = DColor.Night,
    onBackground = DColor.Text,
    surface = DColor.Surface,
    onSurface = DColor.Text,
    surfaceVariant = DColor.Elevated,
    onSurfaceVariant = DColor.TextDim,
    outline = DColor.Line,
    error = DColor.Error,
)

@Composable
fun DafliTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = scheme,
        typography = Typography(
            bodyLarge = DType.Body, bodyMedium = DType.Meta, labelLarge = DType.Button,
            titleLarge = DType.Title, headlineMedium = DType.H1,
        ),
        content = content,
    )
}
