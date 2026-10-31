package com.feldelysium.catalog.ui

import androidx.compose.runtime.Composable
import com.feldelysium.catalog.ui.pages.ButtonsShowcase
import com.feldelysium.catalog.ui.pages.ColorsShowcase
import com.feldelysium.catalog.ui.pages.ContainersShowcase
import com.feldelysium.catalog.ui.pages.ContentShowcase
import com.feldelysium.catalog.ui.pages.DialogShowcase
import com.feldelysium.catalog.ui.pages.InputShowcase
import com.feldelysium.catalog.ui.pages.NavigationShowcase
import com.feldelysium.catalog.ui.pages.PaperShowcase
import com.feldelysium.catalog.ui.pages.SettingsShowcase
import com.feldelysium.catalog.ui.pages.SoundHapticsShowcase
import com.feldelysium.catalog.ui.pages.TypographyShowcase

internal enum class ShowcaseGroup(val title: String) {
    Foundations("Foundations"),
    Components("Components"),
}

internal class ShowcaseEntry(
    val id: String,
    val title: String,
    val summary: String,
    val group: ShowcaseGroup,
    val content: @Composable (onBack: () -> Unit) -> Unit,
)

internal val ShowcaseRegistry = listOf(
    ShowcaseEntry("colors", "Colors", "Palette and theme roles", ShowcaseGroup.Foundations) { ColorsShowcase(it) },
    ShowcaseEntry("typography", "Typography", "The type scale, live", ShowcaseGroup.Foundations) { TypographyShowcase(it) },
    ShowcaseEntry("paper", "Paper", "Surfaces and rough edges", ShowcaseGroup.Foundations) { PaperShowcase(it) },
    ShowcaseEntry("sound", "Sound & Haptics", "Feedback for every press", ShowcaseGroup.Foundations) { SoundHapticsShowcase(it) },
    ShowcaseEntry("buttons", "Buttons", "Press, tilt and tear", ShowcaseGroup.Components) { ButtonsShowcase(it) },
    ShowcaseEntry("input", "Input", "Fields, chips, sliders, menus", ShowcaseGroup.Components) { InputShowcase(it) },
    ShowcaseEntry("containers", "Containers", "Cards, boxes, dividers, toasts", ShowcaseGroup.Components) { ContainersShowcase(it) },
    ShowcaseEntry("navigation", "Navigation", "Headers, action bars, toolbars", ShowcaseGroup.Components) { NavigationShowcase(it) },
    ShowcaseEntry("content", "Content", "Icons, titles, tags", ShowcaseGroup.Components) { ContentShowcase(it) },
    ShowcaseEntry("settings", "Settings", "Rows and sections", ShowcaseGroup.Components) { SettingsShowcase(it) },
    ShowcaseEntry("dialog", "Dialog", "Modal with actions", ShowcaseGroup.Components) { DialogShowcase(it) },
)
