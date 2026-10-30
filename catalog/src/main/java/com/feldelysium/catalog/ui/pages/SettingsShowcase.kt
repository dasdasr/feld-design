package com.feldelysium.catalog.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.feldelysium.catalog.ui.ControlChoice
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.settings.FeldSettingRow
import com.feldelysium.designsystem.components.settings.FeldSettingSwitchRow
import com.feldelysium.designsystem.components.settings.FeldSettingValueRow
import com.feldelysium.designsystem.components.settings.FeldSettingsDivider
import com.feldelysium.designsystem.components.settings.FeldSettingsSection


@Composable
internal fun SettingsShowcase(onBack: () -> Unit) {
    var sound by rememberSaveable { mutableStateOf(true) }
    var speedIndex by rememberSaveable { mutableIntStateOf(1) }
    val speeds = listOf("1x", "2x", "3x")

    ShowcaseScreen(
        title = "Settings",
        eyebrow = "COMPONENTS",
        onBack = onBack,
        preview = {
            FeldSettingsSection(title = "Playback") {
                FeldSettingSwitchRow(
                    icon = FeldIconId.Sound,
                    title = "Sound",
                    subtitle = "Page-turn effects",
                    checked = sound,
                    onCheckedChange = { sound = it },
                )
                FeldSettingsDivider()
                FeldSettingValueRow(
                    icon = FeldIconId.Play,
                    title = "Speed",
                    value = speeds[speedIndex],
                    onClick = { speedIndex = (speedIndex + 1) % speeds.size },
                )
            }
        },
        variants = {
            Specimen("FeldSettingRow") {
                FeldSettingRow(icon = FeldIconId.Settings, title = "Advanced", subtitle = "Open more options", onClick = {})
            }
            Specimen("FeldSettingValueRow") {
                FeldSettingValueRow(icon = FeldIconId.Play, title = "Speed", value = speeds[speedIndex], onClick = {})
            }
            Specimen("FeldSettingSwitchRow") {
                FeldSettingSwitchRow(icon = FeldIconId.Sound, title = "Sound", subtitle = "On or off", checked = sound, onCheckedChange = { sound = it })
            }
        },
        controls = {
            ControlSwitch("sound", sound) { sound = it }
            ControlChoice("speed", speeds, speedIndex) { speedIndex = it }
        },
        usage = """
            FeldSettingsSection(title = "Playback") {
                FeldSettingSwitchRow(
                    icon = FeldIconId.Sound,
                    title = "Sound",
                    checked = sound,
                    onCheckedChange = { sound = it },
                )
            }
        """.trimIndent(),
        notes = listOf("Rows also support a SOON / LOCK status stamp."),
    )
}
