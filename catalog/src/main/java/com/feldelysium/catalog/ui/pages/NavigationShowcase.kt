package com.feldelysium.catalog.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.input.FeldAccentColors
import com.feldelysium.designsystem.components.navigation.FeldActionBar
import com.feldelysium.designsystem.components.navigation.FeldScreenHeader
import com.feldelysium.designsystem.components.navigation.FeldToolbar
import com.feldelysium.designsystem.components.navigation.FeldToolbarItem


@Composable
internal fun NavigationShowcase(onBack: () -> Unit) {
    var showNav by rememberSaveable { mutableStateOf(true) }
    var confirmEnabled by rememberSaveable { mutableStateOf(true) }
    var toolbarEnabled by rememberSaveable { mutableStateOf(true) }

    ShowcaseScreen(
        title = "Navigation",
        eyebrow = "COMPONENTS",
        onBack = onBack,
        preview = {
            FeldScreenHeader(
                title = "Scenes",
                eyebrow = "LIBRARY",
                subtitle = "12 entries",
                navIcon = if (showNav) FeldIconId.Back else null,
                onNav = {},
            )
        },
        variants = {
            Specimen("FeldActionBar") {
                FeldActionBar(
                    cancelText = "Cancel",
                    onCancel = {},
                    confirmText = "Apply",
                    onConfirm = {},
                    confirmEnabled = confirmEnabled,
                )
            }
            Specimen("FeldToolbar") {
                FeldToolbar(
                    items = listOf(
                        FeldToolbarItem(icon = FeldIconId.Edit, label = "Edit", enabled = toolbarEnabled, onClick = {}),
                        FeldToolbarItem(
                            icon = FeldIconId.Add,
                            label = "Add",
                            enabled = toolbarEnabled,
                            accent = FeldAccentColors.Orange,
                            onClick = {},
                        ),
                        FeldToolbarItem(icon = FeldIconId.Delete, label = "Delete", enabled = toolbarEnabled, onClick = {}),
                    ),
                )
            }
        },
        controls = {
            ControlSwitch("header back icon", showNav) { showNav = it }
            ControlSwitch("action bar confirm", confirmEnabled) { confirmEnabled = it }
            ControlSwitch("toolbar enabled", toolbarEnabled) { toolbarEnabled = it }
        },
        usage = """
            FeldScreenHeader(
                title = "Scenes",
                eyebrow = "LIBRARY",
                navIcon = FeldIconId.Back,
                onNav = { },
            )
        """.trimIndent(),
        notes = listOf("navIcon = null hides the back button."),
    )
}
