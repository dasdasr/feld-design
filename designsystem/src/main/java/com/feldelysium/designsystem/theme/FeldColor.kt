package com.feldelysium.designsystem.theme

import androidx.compose.ui.graphics.Color

internal val FeldBackground = Color(0xFF0C1212)
internal val FeldSurface = Color(0xFF141C1C)
internal val FeldSurfaceHigh = Color(0xFF1F2929)
internal val FeldOutline = FeldPalette.Line

internal val FeldText = Color(0xFFE9EAEC)
internal val FeldTextMuted = FeldPalette.Muted

internal val FeldOrange = FeldPalette.Orange

internal val FeldTeal = FeldPalette.Teal
internal val FeldOrangePressed = Color(0xFFB84E1A)
internal val FeldCream = FeldPalette.Cream
internal val FeldInk = FeldPalette.Ink

internal val FeldSuccess = Color(0xFF19BC6D)
internal val FeldError = Color(0xFFAF1E01)

internal fun Long.toColor() = Color(this)
