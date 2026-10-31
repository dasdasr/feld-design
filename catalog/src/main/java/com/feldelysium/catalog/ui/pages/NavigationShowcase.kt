package com.feldelysium.catalog.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.feldelysium.catalog.ui.ControlChoice
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ControlText
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.catalog.ui.kotlinCall
import com.feldelysium.catalog.ui.quoted
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.navigation.FeldActionBar
import com.feldelysium.designsystem.components.navigation.FeldScreenHeader
import com.feldelysium.designsystem.components.navigation.FeldToolbar
import com.feldelysium.designsystem.components.navigation.FeldToolbarItem
import com.feldelysium.designsystem.theme.FeldPalette


private val NavigationSubjects = listOf(
    Subject("Screen header") { _ ->
        var title by remember { mutableStateOf("Scenes") }
        var eyebrow by remember { mutableStateOf("Library") }
        var subtitle by remember { mutableStateOf("12 entries") }
        var back by remember { mutableStateOf(true) }
        var divider by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = {
                FeldScreenHeader(
                    title = title,
                    eyebrow = eyebrow,
                    subtitle = subtitle.ifBlank { null },
                    navIcon = if (back) FeldIconId.Back else null,
                    onNav = {},
                    divider = divider,
                )
            },
            controls = {
                ControlText("title", title) { title = it }
                ControlText("eyebrow", eyebrow) { eyebrow = it }
                ControlText("subtitle (empty hides it)", subtitle) { subtitle = it }
                ControlSwitch("back icon", back) { back = it }
                ControlSwitch("divider", divider) { divider = it }
            },
            code = {
                kotlinCall(
                    "FeldScreenHeader",
                    "title" to title.quoted(),
                    "eyebrow" to eyebrow.quoted(),
                    "subtitle" to subtitle.ifBlank { null }?.quoted(),
                    "navIcon" to if (back) "FeldIconId.Back" else "null",
                    "onNav" to "{ }",
                    "divider" to "false".takeIf { !divider },
                )
            },
        )
    },
    Subject("Action bar") { _ ->
        var cancel by remember { mutableStateOf("Cancel") }
        var confirm by remember { mutableStateOf("Apply") }
        var enabled by remember { mutableStateOf(true) }
        var divider by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = {
                FeldActionBar(
                    cancelText = cancel,
                    onCancel = {},
                    confirmText = confirm.ifBlank { null },
                    onConfirm = {},
                    confirmEnabled = enabled,
                    divider = divider,
                )
            },
            controls = {
                ControlText("cancel", cancel) { cancel = it }
                ControlText("confirm (empty hides it)", confirm) { confirm = it }
                ControlSwitch("confirm enabled", enabled) { enabled = it }
                ControlSwitch("divider", divider) { divider = it }
            },
            code = {
                kotlinCall(
                    "FeldActionBar",
                    "cancelText" to cancel.quoted(),
                    "onCancel" to "{ }",
                    "confirmText" to confirm.ifBlank { null }?.quoted(),
                    "onConfirm" to "{ }".takeIf { confirm.isNotBlank() },
                    "confirmEnabled" to "false".takeIf { !enabled },
                    "divider" to "false".takeIf { !divider },
                )
            },
        )
    },
    Subject("Toolbar") { _ ->
        var enabled by remember { mutableStateOf(true) }
        var accent by remember { mutableIntStateOf(2) }
        val names = listOf("None", "Edit", "Add", "Delete")
        ShowcaseSpec(
            preview = {
                FeldToolbar(
                    items = listOf(
                        FeldToolbarItem(icon = FeldIconId.Edit, label = "Edit", enabled = enabled, accent = FeldPalette.Cream, onClick = {}),
                        FeldToolbarItem(icon = FeldIconId.Add, label = "Add", enabled = enabled, accent = FeldPalette.Orange, onClick = {}),
                        FeldToolbarItem(icon = FeldIconId.Delete, label = "Delete", enabled = enabled, accent = FeldPalette.Cream, onClick = {}),
                    ),
                )
            },
            controls = {
                ControlChoice("accent item", names, accent) { accent = it }
                ControlSwitch("enabled", enabled) { enabled = it }
            },
            code = {
                """
                FeldToolbar(
                    items = listOf(
                        FeldToolbarItem(icon = FeldIconId.Edit, label = "Edit", onClick = { }),
                        FeldToolbarItem(icon = FeldIconId.Add, label = "Add", accent = ${accent == 2}, onClick = { }),
                        FeldToolbarItem(icon = FeldIconId.Delete, label = "Delete", onClick = { }),
                    ),
                )
                """.trimIndent()
            },
            notes = listOf("An accent item is wider and tints its icon."),
        )
    },
)

@Composable
internal fun NavigationShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Navigation", "COMPONENTS", onBack, NavigationSubjects)
