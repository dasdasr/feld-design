package com.feldelysium.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FeldColorScheme = darkColorScheme(
    primary = FeldOrange,
    onPrimary = FeldInk,
    primaryContainer = FeldOrange,
    onPrimaryContainer = FeldInk,
    inversePrimary = FeldOrangePressed,

    secondary = FeldCream,
    onSecondary = FeldInk,
    secondaryContainer = FeldCream,
    onSecondaryContainer = FeldInk,

    tertiary = FeldOrangePressed,
    onTertiary = FeldText,
    tertiaryContainer = FeldSurfaceHigh,
    onTertiaryContainer = FeldText,

    background = FeldBackground,
    onBackground = FeldText,
    surface = FeldSurface,
    onSurface = FeldText,
    surfaceVariant = FeldSurfaceHigh,
    onSurfaceVariant = FeldTextMuted,
    surfaceTint = Color.Transparent,
    inverseSurface = FeldCream,
    inverseOnSurface = FeldInk,

    error = FeldError,
    onError = FeldText,
    errorContainer = FeldError,
    onErrorContainer = FeldText,

    outline = FeldOutline,
    outlineVariant = FeldOutline,

    surfaceDim = FeldBackground,
    surfaceBright = FeldSurfaceHigh,
    surfaceContainerLowest = FeldBackground,
    surfaceContainerLow = FeldSurface,
    surfaceContainer = FeldSurface,
    surfaceContainerHigh = FeldSurfaceHigh,
    surfaceContainerHighest = FeldSurfaceHigh,
)

/** Applies the Feld color scheme and typography. Dark only. */
@Composable
public fun FeldTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FeldColorScheme,
        typography = FeldTypography,
        content = content,
    )
}
