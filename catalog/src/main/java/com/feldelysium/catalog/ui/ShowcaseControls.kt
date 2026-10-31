package com.feldelysium.catalog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.components.input.FeldAccentColors
import com.feldelysium.designsystem.components.input.FeldChip
import com.feldelysium.designsystem.components.input.FeldSegmentedControl
import com.feldelysium.designsystem.components.input.FeldSlider
import com.feldelysium.designsystem.components.input.FeldSwitch
import com.feldelysium.designsystem.components.input.FeldTextField
import com.feldelysium.designsystem.theme.FeldPalette

internal val AccentNames = listOf("Cream", "Teal", "Orange")
internal val AccentColors = listOf(FeldAccentColors.Cream, FeldAccentColors.Teal, FeldAccentColors.Orange)

@Composable
internal fun SectionLabel(text: String) {
    Text(text.uppercase(), style = CatalogMono, color = FeldPalette.Orange, modifier = Modifier.padding(top = 8.dp))
    FeldDivider(modifier = Modifier.padding(vertical = 8.dp))
}

/** A gallery item with a label, content preview and divider. */
@Composable
internal fun Specimen(name: String, content: @Composable () -> Unit) {
    Column(Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)) {
        Text(name, style = CatalogMono, color = FeldPalette.Muted)
        Spacer(Modifier.height(12.dp))
        content()
    }
    FeldDivider()
}

@Composable
internal fun ControlField(label: String, content: @Composable () -> Unit) {
    Column(Modifier
        .fillMaxWidth()
        .padding(vertical = 14.dp)) {
        Text(label.uppercase(), style = CatalogMono, color = FeldPalette.Muted)
        Spacer(Modifier.height(8.dp))
        content()
    }
    FeldDivider()
}

@Composable
internal fun ControlSwitch(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label.uppercase(), style = CatalogMono, color = FeldPalette.Cream, modifier = Modifier.weight(1f))
        FeldSwitch(checked = checked, onCheckedChange = onChange)
    }
    FeldDivider()
}

/** Displays a small set of mutually exclusive options. */
@Composable
internal fun ControlChoice(label: String, options: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit) {
    ControlField(label) { FeldSegmentedControl(options = options, selectedIndex = selectedIndex, onSelect = onSelect) }
}

/** Displays selectable options as a wrapping row of chips. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ControlChips(label: String, options: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit) {
    ControlField(label) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            options.forEachIndexed { i, name ->
                FeldChip(text = name, selected = i == selectedIndex, onClick = { onSelect(i) })
            }
        }
    }
}

@Composable
internal fun ControlText(label: String, value: String, onChange: (String) -> Unit) {
    ControlField(label) {
        FeldTextField(value = value, onValueChange = onChange, modifier = Modifier.fillMaxWidth(), singleLine = true)
    }
}

@Composable
internal fun ControlSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onChange: (Float) -> Unit,
    format: (Float) -> String = { "%.1f".format(it) },
) {
    ControlField(label) { FeldSlider(value = value, onValueChange = onChange, valueRange = range, valueText = format) }
}
