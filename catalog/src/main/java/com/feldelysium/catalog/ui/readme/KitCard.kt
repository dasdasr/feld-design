package com.feldelysium.catalog.ui.readme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.AccentColors
import com.feldelysium.catalog.ui.AccentNames
import com.feldelysium.designsystem.components.button.FeldButton
import com.feldelysium.designsystem.components.button.FeldButtonDefaults
import com.feldelysium.designsystem.components.button.FeldGhostButton
import com.feldelysium.designsystem.components.feedback.FeldBadge
import com.feldelysium.designsystem.components.input.FeldCheckbox
import com.feldelysium.designsystem.components.input.FeldChip
import com.feldelysium.designsystem.components.input.FeldDropdown
import com.feldelysium.designsystem.components.input.FeldRadioButton
import com.feldelysium.designsystem.components.input.FeldSegmentedControl
import com.feldelysium.designsystem.components.input.FeldSlider
import com.feldelysium.designsystem.components.input.FeldSwitch
import com.feldelysium.designsystem.components.input.FeldTextField
import com.feldelysium.designsystem.components.input.FeldThumb
import com.feldelysium.designsystem.foundation.decal.DecalSpec
import com.feldelysium.designsystem.theme.FeldPalette


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun KitCard(seed: Int) {
    var name by remember { mutableStateOf("") }
    var chip by remember { mutableIntStateOf(1) }
    var volume by remember { mutableStateOf(0.6f) }
    var tab by remember { mutableIntStateOf(1) }
    var sound by remember { mutableStateOf(true) }
    var auto by remember { mutableStateOf(false) }
    var remember by remember { mutableStateOf(true) }
    var mode by remember { mutableIntStateOf(0) }
    var language by remember { mutableStateOf<String?>("English") }

    Board(seed, "Fig. 02 · Scene editor · draft 07", decal = DecalSpec.Dialog) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FeldTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.weight(1f),
                    seed = seed,
                    label = "Scene title",
                    placeholder = "Type something",
                    singleLine = true,
                )
                Spacer(Modifier.width(12.dp))
                FeldBadge(text = "Draft", accent = FeldPalette.Orange)
            }
            FeldDropdown(
                options = listOf("English", "Deutsch", "Беларуская", "Polski"),
                selected = language,
                onSelect = { language = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Language",
                placeholder = "Choose language",
                seed = seed + 5,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AccentNames.forEachIndexed { i, label ->
                    FeldChip(text = label, selected = i == chip, onClick = { chip = i }, accent = AccentColors[i])
                }
            }
            FeldSlider(
                value = volume,
                onValueChange = { volume = it },
                accent = AccentColors[0],
                thumb = FeldThumb.Diamond,
                label = "Volume",
                valueText = { "${(it * 100).toInt()}%" },
                seed = seed,
                modifier = Modifier.fillMaxWidth(),
            )
            FeldSegmentedControl(
                options = listOf("Day", "Week", "Month"),
                selectedIndex = tab,
                onSelect = { tab = it },
                seed = seed,
            )
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(
                    Modifier.weight(1f).selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    listOf("Draft", "Final").forEachIndexed { i, label ->
                        FeldRadioButton(
                            selected = mode == i,
                            onClick = { mode = i },
                            label = label,
                            seed = seed + 6 + i,
                        )
                    }
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    FeldCheckbox(
                        checked = remember,
                        onCheckedChange = { remember = it },
                        label = "Remember",
                        seed = seed + 8,
                    )
                    FeldCheckbox(
                        checked = false,
                        onCheckedChange = {},
                        label = "Share",
                        accent = AccentColors[1],
                        seed = seed + 9,
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                FeldSwitch(checked = sound, onCheckedChange = { sound = it }, seed = seed)
                Spacer(Modifier.width(10.dp))
                Text("SOUND", style = mono(11f), color = FeldPalette.Cream, modifier = Modifier.weight(1f))
                Text("AUTOPLAY", style = mono(11f), color = FeldPalette.Cream)
                Spacer(Modifier.width(10.dp))
                FeldSwitch(checked = auto, onCheckedChange = { auto = it }, seed = seed + 1)
            }
            Rule()
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) { FeldGhostButton(text = "Cancel", onClick = {}, seed = seed + 2) }
                FeldButton("Save", {}, Modifier.weight(1f), seed = seed + 3, spec = FeldButtonDefaults.Orange)
            }
        }
    }
}
