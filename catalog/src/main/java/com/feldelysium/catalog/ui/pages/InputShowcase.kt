package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.designsystem.components.button.FeldGhostButton
import com.feldelysium.designsystem.components.input.FeldAccentColors
import com.feldelysium.designsystem.components.input.FeldChip
import com.feldelysium.designsystem.components.input.FeldMenu
import com.feldelysium.designsystem.components.input.FeldMenuItem
import com.feldelysium.designsystem.components.input.FeldSegmentedControl
import com.feldelysium.designsystem.components.input.FeldSlider
import com.feldelysium.designsystem.components.input.FeldSwitch
import com.feldelysium.designsystem.components.input.FeldTextField


@Composable
internal fun InputShowcase(onBack: () -> Unit) {
    var enabled by rememberSaveable { mutableStateOf(true) }
    var title by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    var teal by rememberSaveable { mutableStateOf(true) }
    var orange by rememberSaveable { mutableStateOf(false) }
    var cream by rememberSaveable { mutableStateOf(false) }
    var switchOn by rememberSaveable { mutableStateOf(true) }
    var volume by rememberSaveable { mutableFloatStateOf(0.6f) }
    var segment by rememberSaveable { mutableIntStateOf(0) }
    var menuOpen by remember { mutableStateOf(false) }

    ShowcaseScreen(
        title = "Input",
        eyebrow = "COMPONENTS",
        onBack = onBack,
        preview = {
            FeldTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Scene title",
                enabled = enabled,
                singleLine = true,
            )
        },
        variants = {
            Specimen("FeldTextField · multiline with counter") {
                FeldTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Description",
                    minLines = 3,
                    maxLength = 120,
                    showLength = true,
                    enabled = enabled,
                )
            }
            Specimen("FeldChip") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FeldChip(
                        text = "Teal",
                        selected = teal,
                        onClick = { teal = !teal },
                        enabled = enabled,
                        accent = FeldAccentColors.Teal
                    )
                    FeldChip(
                        text = "Orange",
                        selected = orange,
                        onClick = { orange = !orange },
                        enabled = enabled,
                        accent = FeldAccentColors.Orange
                    )
                    FeldChip(
                        text = "Cream",
                        selected = cream,
                        onClick = { cream = !cream },
                        enabled = enabled,
                        accent = FeldAccentColors.Cream
                    )
                }
            }
            Specimen("FeldSwitch") {
                FeldSwitch(
                    checked = switchOn,
                    onCheckedChange = { switchOn = it },
                    enabled = enabled
                )
            }
            Specimen("FeldSlider") {
                FeldSlider(
                    value = volume,
                    onValueChange = { volume = it },
                    label = "Volume",
                    valueText = { "${(it * 100).toInt()}%" },
                )
            }
            Specimen("FeldSegmentedControl") {
                FeldSegmentedControl(
                    options = listOf("Day", "Week", "All"),
                    selectedIndex = segment,
                    onSelect = { segment = it })
            }
            Specimen("FeldMenu") {
                Box {
                    FeldGhostButton(text = "Open menu", onClick = { menuOpen = true })
                    FeldMenu(
                        expanded = menuOpen,
                        onDismiss = { menuOpen = false },
                        items = listOf(
                            FeldMenuItem(label = "Rename", onClick = { menuOpen = false }),
                            FeldMenuItem(label = "Delete", destructive = true, onClick = { menuOpen = false }),
                        ),
                    )
                }
            }
        },
        controls = {
            ControlSwitch("enabled", enabled) { enabled = it }
        },
        usage = """
            FeldTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = "Scene title",
                singleLine = true,
            )
        """.trimIndent(),
        notes = listOf("Every input is live: change values in Variants."),
    )
}
