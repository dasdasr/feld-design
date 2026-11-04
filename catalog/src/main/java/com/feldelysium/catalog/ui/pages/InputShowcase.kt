package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.AccentColors
import com.feldelysium.catalog.ui.AccentNames
import com.feldelysium.catalog.ui.ControlChoice
import com.feldelysium.catalog.ui.ControlSlider
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ControlText
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.catalog.ui.kotlinCall
import com.feldelysium.catalog.ui.quoted
import com.feldelysium.designsystem.components.button.FeldGhostButton
import com.feldelysium.designsystem.components.input.FeldCheckbox
import com.feldelysium.designsystem.components.input.FeldChip
import com.feldelysium.designsystem.components.input.FeldDropdown
import com.feldelysium.designsystem.components.input.FeldMenu
import com.feldelysium.designsystem.components.input.FeldMenuItem
import com.feldelysium.designsystem.components.input.FeldRadioButton
import com.feldelysium.designsystem.components.input.FeldSegmentedControl
import com.feldelysium.designsystem.components.input.FeldSlider
import com.feldelysium.designsystem.components.input.FeldSwitch
import com.feldelysium.designsystem.components.input.FeldTextField
import com.feldelysium.designsystem.components.input.FeldThumb


private val ThumbNames = listOf("Rectangle", "Diamond", "Circle")
private val Thumbs = listOf(FeldThumb.Rectangle, FeldThumb.Diamond, FeldThumb.Circle)
private val Percent: (Float) -> String = { "${(it * 100).toInt()}%" }

private val InputSubjects = listOf(
    Subject("Text field") { seed ->
        var value by remember { mutableStateOf("") }
        var label by remember { mutableStateOf("") }
        var multiline by remember { mutableStateOf(false) }
        var counter by remember { mutableStateOf(false) }
        var enabled by remember { mutableStateOf(true) }
        ShowcaseSpec(
            textured = true,
            preview = {
                FeldTextField(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.fillMaxWidth(),
                    seed = seed,
                    label = label.ifBlank { null },
                    placeholder = "Type something",
                    singleLine = !multiline,
                    minLines = if (multiline) 3 else 1,
                    maxLength = if (counter) 60 else null,
                    showLength = counter,
                    enabled = enabled,
                )
            },
            controls = {
                ControlText("label (tag on the border)", label) { label = it }
                ControlSwitch("multiline", multiline) { multiline = it }
                ControlSwitch("counter · limit 60", counter) { counter = it }
                ControlSwitch("enabled", enabled) { enabled = it }
            },
            code = {
                kotlinCall(
                    "FeldTextField",
                    "value" to "value",
                    "onValueChange" to "{ value = it }",
                    "label" to label.ifBlank { null }?.quoted(),
                    "placeholder" to "\"Type something\"",
                    "singleLine" to "false".takeIf { multiline },
                    "minLines" to "3".takeIf { multiline },
                    "maxLength" to "60".takeIf { counter },
                    "showLength" to "true".takeIf { counter },
                    "enabled" to "false".takeIf { !enabled },
                )
            },
            notes = listOf("Type in the preview: the field is fully live."),
        )
    },
    Subject("Checkbox") { seed ->
        var checked by remember { mutableStateOf(true) }
        var label by remember { mutableStateOf("Remember choice") }
        var enabled by remember { mutableStateOf(true) }
        var accent by remember { mutableIntStateOf(0) }
        ShowcaseSpec(
            textured = true,
            preview = {
                FeldCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    label = label.ifBlank { null },
                    enabled = enabled,
                    accent = AccentColors[accent],
                    seed = seed,
                )
            },
            controls = {
                ControlText("label", label) { label = it }
                ControlChoice("accent", AccentNames, accent) { accent = it }
                ControlSwitch("checked", checked) { checked = it }
                ControlSwitch("enabled", enabled) { enabled = it }
            },
            code = {
                kotlinCall(
                    "FeldCheckbox",
                    "checked" to checked.toString(),
                    "onCheckedChange" to "{ checked = it }",
                    "label" to label.ifBlank { null }?.quoted(),
                    "accent" to "FeldAccentColors.${AccentNames[accent]}".takeIf { accent != 0 },
                    "enabled" to "false".takeIf { !enabled },
                )
            },
            notes = listOf("The whole row is tappable. Uses the switch sounds for now."),
        )
    },
    Subject("Radio") { seed ->
        var selected by remember { mutableIntStateOf(0) }
        var enabled by remember { mutableStateOf(true) }
        val options = listOf("Cream", "Teal", "Orange")
        ShowcaseSpec(
            textured = true,
            preview = {
                Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEachIndexed { i, name ->
                        FeldRadioButton(
                            selected = i == selected,
                            onClick = { selected = i },
                            label = name,
                            enabled = enabled,
                            seed = seed + i,
                        )
                    }
                }
            },
            controls = { ControlSwitch("enabled", enabled) { enabled = it } },
            code = {
                kotlinCall(
                    "FeldRadioButton",
                    "selected" to "selected == 0",
                    "onClick" to "{ selected = 0 }",
                    "label" to "\"Cream\"",
                    "enabled" to "false".takeIf { !enabled },
                ) + "\n\n// wrap a group in Modifier.selectableGroup()"
            },
        )
    },
    Subject("Dropdown") { seed ->
        val options = listOf("English", "Deutsch", "Беларуская", "Polski")
        var selected by remember { mutableStateOf<String?>(null) }
        var label by remember { mutableStateOf("Language") }
        var enabled by remember { mutableStateOf(true) }
        ShowcaseSpec(
            textured = true,
            preview = {
                FeldDropdown(
                    options = options,
                    selected = selected,
                    onSelect = { selected = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = label.ifBlank { null },
                    placeholder = "Choose language",
                    enabled = enabled,
                    seed = seed,
                )
            },
            controls = {
                ControlText("label", label) { label = it }
                ControlSwitch("enabled", enabled) { enabled = it }
            },
            code = {
                kotlinCall(
                    "FeldDropdown",
                    "options" to "listOf(" + options.joinToString { it.quoted() } + ")",
                    "selected" to "selected",
                    "onSelect" to "{ selected = it }",
                    "label" to label.ifBlank { null }?.quoted(),
                    "placeholder" to "\"Choose language\"",
                    "enabled" to "false".takeIf { !enabled },
                )
            },
            notes = listOf("selected = null shows the placeholder. The menu is as wide as its content (FeldMenu)."),
        )
    },
    Subject("Chip") { seed ->
        var text by remember { mutableStateOf("Teal") }
        var selected by remember { mutableStateOf(true) }
        var enabled by remember { mutableStateOf(true) }
        var accent by remember { mutableIntStateOf(1) }
        ShowcaseSpec(
            textured = true,
            preview = {
                FeldChip(
                    text = text,
                    selected = selected,
                    onClick = { selected = !selected },
                    enabled = enabled,
                    accent = AccentColors[accent],
                )
            },
            controls = {
                ControlText("label", text) { text = it }
                ControlChoice("accent", AccentNames, accent) { accent = it }
                ControlSwitch("selected", selected) { selected = it }
                ControlSwitch("enabled", enabled) { enabled = it }
            },
            variants = {
                Specimen("Accents · selected") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AccentNames.forEachIndexed { i, name ->
                            FeldChip(text = name, selected = true, onClick = {}, accent = AccentColors[i])
                        }
                    }
                }
                Specimen("Accents · idle") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AccentNames.forEachIndexed { i, name ->
                            FeldChip(text = name, selected = false, onClick = {}, accent = AccentColors[i])
                        }
                    }
                }
            },
            code = {
                kotlinCall(
                    "FeldChip",
                    "text" to text.quoted(),
                    "selected" to if (selected) "true" else "false",
                    "onClick" to "{ }",
                    "accent" to "FeldAccentColors.${AccentNames[accent]}",
                    "enabled" to "false".takeIf { !enabled },
                )
            },
            notes = listOf("Tap the chip in the preview: the fill grows from the touch point."),
        )
    },
    Subject("Switch") { _ ->
        var checked by remember { mutableStateOf(true) }
        var enabled by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = { FeldSwitch(checked = checked, onCheckedChange = { checked = it }, enabled = enabled) },
            controls = {
                ControlSwitch("checked", checked) { checked = it }
                ControlSwitch("enabled", enabled) { enabled = it }
            },
            code = {
                kotlinCall(
                    "FeldSwitch",
                    "checked" to checked.toString(),
                    "onCheckedChange" to "{ checked = it }",
                    "enabled" to "false".takeIf { !enabled },
                )
            },
            notes = listOf("Tap or drag the thumb."),
        )
    },
    Subject("Slider") { seed ->
        var value by remember { mutableFloatStateOf(0.6f) }
        var accent by remember { mutableIntStateOf(0) }
        var thumb by remember { mutableIntStateOf(0) }
        var showLabel by remember { mutableStateOf(true) }
        var showValue by remember { mutableStateOf(true) }
        var track by remember { mutableFloatStateOf(10f) }
        var thumbShadow by remember { mutableFloatStateOf(1f) }
        var trackCutout by remember { mutableFloatStateOf(0f) }
        ShowcaseSpec(
            textured = true,
            preview = {
                FeldSlider(
                    value = value,
                    onValueChange = { value = it },
                    accent = AccentColors[accent],
                    thumb = Thumbs[thumb],
                    label = if (showLabel) "Volume" else null,
                    valueText = if (showValue) Percent else null,
                    trackHeight = track.dp,
                    seed = seed,
                    thumbShadow = thumbShadow,
                    trackCutout = trackCutout.dp,
                )
            },
            controls = {
                ControlChoice("accent", AccentNames, accent) { accent = it }
                ControlChoice("thumb", ThumbNames, thumb) { thumb = it }
                ControlSwitch("label", showLabel) { showLabel = it }
                ControlSwitch("value text", showValue) { showValue = it }
                ControlSlider("track height", track, 4f..20f, { track = it }) { "${it.toInt()} dp" }
                ControlSlider(
                    "thumb shadow",
                    thumbShadow,
                    0f..2f,
                    { thumbShadow = it },
                ) { "%.1f".format(it) }

                ControlSlider(
                    "track cutout",
                    trackCutout,
                    0f..6f,
                    { trackCutout = it },
                ) { "%.1f".format(it) + " dp" }
            },
            code = {
                kotlinCall(
                    "FeldSlider",
                    "value" to "value",
                    "onValueChange" to "{ value = it }",
                    "accent" to "FeldAccentColors.${AccentNames[accent]}",
                    "thumb" to "FeldThumb.${ThumbNames[thumb]}".takeIf { thumb != 0 },
                    "label" to "\"Volume\"".takeIf { showLabel },
                    "valueText" to "{ \"\${(it * 100).toInt()}%\" }".takeIf { showValue },
                    "trackHeight" to "${track.toInt()}.dp".takeIf { track != 10f },
                    "thumbShadow" to "${"%.1f".format(thumbShadow)}f"
                        .takeIf { thumbShadow != 1f },
                    "trackCutout" to "${trackCutout.toInt()}.dp"
                        .takeIf { trackCutout != 0f },
                )
            },
        )
    },
    Subject("Segmented control") { seed ->
        var selected by remember { mutableIntStateOf(0) }
        var count by remember { mutableIntStateOf(1) }
        var long by remember { mutableStateOf(false) }
        val labels =
            if (long) listOf("Tab 1", "Tab 2", "Tab 33333333333", "Tab 4") else listOf("Day", "Week", "Month", "All")
        ShowcaseSpec(
            textured = true,
            preview = {
                val n = count + 2
                FeldSegmentedControl(
                    options = labels.take(n),
                    selectedIndex = selected.coerceAtMost(n - 1),
                    onSelect = { selected = it },
                    seed = seed,
                )
            },
            controls = {
                ControlChoice("segments", listOf("2", "3", "4"), count) { count = it }
                ControlSwitch("long labels", long) { long = it }
            },
            code = {
                kotlinCall(
                    "FeldSegmentedControl",
                    "options" to "listOf(" + labels.take(count + 2).joinToString { it.quoted() } + ")",
                    "selectedIndex" to "selected",
                    "onSelect" to "{ selected = it }",
                )
            },
            notes = listOf("Long labels are truncated with an ellipsis."),
        )
    },
    Subject("Menu") { _ ->
        var open by remember { mutableStateOf(false) }
        var destructive by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = {
                Box {
                    FeldGhostButton(text = "Open menu", onClick = { open = true })
                    FeldMenu(
                        expanded = open,
                        onDismiss = { open = false },
                        items = buildList {
                            add(FeldMenuItem(label = "Rename", onClick = { open = false }))
                            if (destructive) add(
                                FeldMenuItem(
                                    label = "Delete",
                                    destructive = true,
                                    onClick = { open = false })
                            )
                        },
                    )
                }
            },
            controls = { ControlSwitch("destructive item", destructive) { destructive = it } },
            code = {
                """
                FeldMenu(
                    expanded = open,
                    onDismiss = { open = false },
                    items = listOf(
                        FeldMenuItem(label = "Rename", onClick = { }),${if (destructive) "\n        FeldMenuItem(label = \"Delete\", destructive = true, onClick = { })," else ""}
                    ),
                )
                """.trimIndent()
            },
        )
    },
)

@Composable
internal fun InputShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Input", "COMPONENTS", onBack, InputSubjects)
