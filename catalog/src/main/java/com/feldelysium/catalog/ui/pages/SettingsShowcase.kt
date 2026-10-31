package com.feldelysium.catalog.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.feldelysium.catalog.ui.ControlChoice
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ControlText
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.catalog.ui.kotlinCall
import com.feldelysium.catalog.ui.quoted
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.settings.FeldSettingRow
import com.feldelysium.designsystem.components.settings.FeldSettingSwitchRow
import com.feldelysium.designsystem.components.settings.FeldSettingValueRow
import com.feldelysium.designsystem.components.settings.FeldSettingsDivider
import com.feldelysium.designsystem.components.settings.FeldSettingsSection


private val SettingsSubjects = listOf(
    Subject("Row") { _ ->
        var title by remember { mutableStateOf("Advanced") }
        var subtitle by remember { mutableStateOf("Open more options") }
        var chevron by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = {
                FeldSettingRow(
                    icon = FeldIconId.Settings,
                    title = title,
                    subtitle = subtitle,
                    showChevron = chevron,
                    onClick = {},
                )
            },
            controls = {
                ControlText("title", title) { title = it }
                ControlText("subtitle", subtitle) { subtitle = it }
                ControlSwitch("chevron", chevron) { chevron = it }
            },
            code = {
                kotlinCall(
                    "FeldSettingRow",
                    "icon" to "FeldIconId.Settings",
                    "title" to title.quoted(),
                    "subtitle" to subtitle.quoted(),
                    "showChevron" to "false".takeIf { !chevron },
                    "onClick" to "{ }",
                )
            },
        )
    },
    Subject("Value row") { _ ->
        var title by remember { mutableStateOf("Speed") }
        var value by remember { mutableStateOf("2x") }
        ShowcaseSpec(
            preview = { FeldSettingValueRow(icon = FeldIconId.Play, title = title, value = value, onClick = {}) },
            controls = {
                ControlText("title", title) { title = it }
                ControlText("value", value) { value = it }
            },
            code = {
                kotlinCall(
                    "FeldSettingValueRow",
                    "icon" to "FeldIconId.Play",
                    "title" to title.quoted(),
                    "value" to value.quoted(),
                    "onClick" to "{ }",
                )
            },
        )
    },
    Subject("Switch row") { _ ->
        var title by remember { mutableStateOf("Sound") }
        var subtitle by remember { mutableStateOf("Page-turn effects") }
        var checked by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = {
                FeldSettingSwitchRow(
                    icon = FeldIconId.Sound,
                    title = title,
                    subtitle = subtitle,
                    checked = checked,
                    onCheckedChange = { checked = it },
                )
            },
            controls = {
                ControlText("title", title) { title = it }
                ControlText("subtitle", subtitle) { subtitle = it }
                ControlSwitch("checked", checked) { checked = it }
            },
            code = {
                kotlinCall(
                    "FeldSettingSwitchRow",
                    "icon" to "FeldIconId.Sound",
                    "title" to title.quoted(),
                    "subtitle" to subtitle.quoted(),
                    "checked" to "checked",
                    "onCheckedChange" to "{ checked = it }",
                )
            },
        )
    },
    Subject("Section") { _ ->
        var title by remember { mutableStateOf("Playback") }
        var sound by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = {
                FeldSettingsSection(title = title) {
                    FeldSettingSwitchRow(
                        icon = FeldIconId.Sound,
                        title = "Sound",
                        subtitle = "Page-turn effects",
                        checked = sound,
                        onCheckedChange = { sound = it },
                    )
                    FeldSettingsDivider()
                    FeldSettingValueRow(icon = FeldIconId.Play, title = "Speed", value = "2x", onClick = {})
                }
            },
            controls = { ControlText("section title", title) { title = it } },
            code = {
                "FeldSettingsSection(title = ${title.quoted()}) {\n" +
                        "    FeldSettingSwitchRow(/* ... */)\n" +
                        "    FeldSettingsDivider()\n" +
                        "    FeldSettingValueRow(/* ... */)\n" +
                        "}"
            },
        )
    },
)

@Composable
internal fun SettingsShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Settings", "COMPONENTS", onBack, SettingsSubjects)
