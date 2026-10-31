package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlChips
import com.feldelysium.catalog.ui.ControlChoice
import com.feldelysium.catalog.ui.ControlField
import com.feldelysium.catalog.ui.ControlSlider
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ControlText
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.catalog.ui.kotlinCall
import com.feldelysium.catalog.ui.quoted
import com.feldelysium.designsystem.components.button.FeldButton
import com.feldelysium.designsystem.components.button.FeldButtonDefaults
import com.feldelysium.designsystem.components.button.FeldFab
import com.feldelysium.designsystem.components.button.FeldFabVariant
import com.feldelysium.designsystem.components.button.FeldFieldButton
import com.feldelysium.designsystem.components.button.FeldFilmButton
import com.feldelysium.designsystem.components.button.FeldGhostButton
import com.feldelysium.designsystem.components.button.FeldIconButton
import com.feldelysium.designsystem.components.button.FeldTextButton
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.input.FeldTextField
import com.feldelysium.designsystem.theme.FeldContinueStyle
import com.feldelysium.designsystem.theme.FeldPalette


private val SpecLabels = listOf("Cream", "Orange", "Fresh")
private val SpecNames = listOf("Cream", "Orange", "OrangeFresh")
private val Specs = listOf(FeldButtonDefaults.Cream, FeldButtonDefaults.Orange, FeldButtonDefaults.OrangeFresh)
private const val LongLabel = "A label that is far too long to fit"

private val IconIds = listOf(FeldIconId.Add, FeldIconId.Edit, FeldIconId.Delete, FeldIconId.Settings, FeldIconId.Search, FeldIconId.Play)
private val IconNames = listOf("Add", "Edit", "Delete", "Settings", "Search", "Play")

private val ButtonSubjects = listOf(
    Subject("Button") { seed ->
        var text by remember { mutableStateOf("Save scene") }
        var spec by remember { mutableIntStateOf(0) }
        var enabled by remember { mutableStateOf(true) }
        var clip by remember { mutableStateOf(false) }
        ShowcaseSpec(
            textured = true,
            preview = {
                FeldButton(
                    text = if (clip) LongLabel else text,
                    onClick = {},
                    modifier = if (clip) Modifier.width(220.dp) else Modifier,
                    seed = seed,
                    spec = Specs[spec],
                    enabled = enabled,
                    maxLines = if (clip) 1 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            controls = {
                ControlText("label", text) { text = it }
                ControlChoice("paper", SpecLabels, spec) { spec = it }
                ControlSwitch("enabled", enabled) { enabled = it }
                ControlSwitch("long label · one line", clip) { clip = it }
            },
            variants = {
                SpecLabels.forEachIndexed { i, name ->
                    Specimen("FeldButtonDefaults.${SpecNames[i]}") {
                        FeldButton(text = name, onClick = {}, seed = seed, spec = Specs[i])
                    }
                }
            },
            code = {
                kotlinCall(
                    "FeldButton",
                    "text" to (if (clip) LongLabel else text).quoted(),
                    "onClick" to "{ }",
                    "spec" to "FeldButtonDefaults.${SpecNames[spec]}",
                    "enabled" to "false".takeIf { !enabled },
                    "maxLines" to "1".takeIf { clip },
                    "overflow" to "TextOverflow.Ellipsis".takeIf { clip },
                )
            },
            notes = listOf("Text is uppercase.", "Disabled lowers alpha to 50% and blocks clicks."),
        )
    },
    Subject("Text button") { _ ->
        var text by remember { mutableStateOf("Cancel") }
        var clip by remember { mutableStateOf(false) }
        ShowcaseSpec(
            preview = {
                FeldTextButton(
                    text = if (clip) LongLabel else text,
                    onClick = {},
                    modifier = if (clip) Modifier.width(200.dp) else Modifier,
                    maxLines = if (clip) 1 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            controls = {
                ControlText("label", text) { text = it }
                ControlSwitch("long label · one line", clip) { clip = it }
            },
            code = {
                kotlinCall(
                    "FeldTextButton",
                    "text" to (if (clip) LongLabel else text).quoted(),
                    "onClick" to "{ }",
                    "maxLines" to "1".takeIf { clip },
                    "overflow" to "TextOverflow.Ellipsis".takeIf { clip },
                )
            },
        )
    },
    Subject("Icon button") { _ ->
        var icon by remember { mutableIntStateOf(0) }
        var enabled by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = { FeldIconButton(id = IconIds[icon], onClick = {}, enabled = enabled) },
            controls = {
                ControlChips("icon", IconNames, icon) { icon = it }
                ControlSwitch("enabled", enabled) { enabled = it }
            },
            variants = {
                Specimen("All icons here") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconIds.forEach { FeldIconButton(id = it, onClick = {}) }
                    }
                }
            },
            code = {
                kotlinCall(
                    "FeldIconButton",
                    "id" to "FeldIconId.${IconNames[icon]}",
                    "onClick" to "{ }",
                    "enabled" to "false".takeIf { !enabled },
                )
            },
        )
    },
    Subject("Ghost button") { seed ->
        var text by remember { mutableStateOf("Cancel") }
        var hasIcon by remember { mutableStateOf(true) }
        ShowcaseSpec(
            textured = true,
            preview = {
                FeldGhostButton(
                    text = text,
                    onClick = {},
                    icon = if (hasIcon) FeldIconId.Close else null,
                    seed = seed,
                )
            },
            controls = {
                ControlText("label", text) { text = it }
                ControlSwitch("icon", hasIcon) { hasIcon = it }
            },
            code = {
                kotlinCall(
                    "FeldGhostButton",
                    "text" to text.quoted(),
                    "onClick" to "{ }",
                    "icon" to "FeldIconId.Close".takeIf { hasIcon },
                )
            },
        )
    },
    Subject("Film button") { seed ->
        var text by remember { mutableStateOf("Save") }
        var icon by remember { mutableIntStateOf(0) }
        val ids = listOf(FeldIconId.Save, FeldIconId.Add, FeldIconId.Play)
        val names = listOf("Save", "Add", "Play")
        ShowcaseSpec(
            textured = true,
            preview = { FeldFilmButton(text = text, onClick = {}, icon = ids[icon], seed = seed) },
            controls = {
                ControlText("label", text) { text = it }
                ControlChoice("icon", names, icon) { icon = it }
            },
            code = {
                kotlinCall(
                    "FeldFilmButton",
                    "text" to text.quoted(),
                    "onClick" to "{ }",
                    "icon" to "FeldIconId.${names[icon]}",
                )
            },
        )
    },
    Subject("Field button") { _ ->
        var hasValue by remember { mutableStateOf(false) }
        var value by remember { mutableStateOf("English") }
        var enabled by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = {
                FeldFieldButton(
                    text = if (hasValue) value else null,
                    onClick = {},
                    placeholder = "Choose language",
                    enabled = enabled,
                )
            },
            controls = {
                ControlSwitch("has value", hasValue) { hasValue = it }
                ControlText("value", value) { value = it }
                ControlSwitch("enabled", enabled) { enabled = it }
            },
            code = {
                kotlinCall(
                    "FeldFieldButton",
                    "text" to if (hasValue) value.quoted() else "null",
                    "onClick" to "{ }",
                    "placeholder" to "\"Choose language\"",
                    "enabled" to "false".takeIf { !enabled },
                )
            },
            notes = listOf("With text = null the placeholder is shown dimmed."),
        )
    },

    Subject("FAB") { _ ->
        var size by remember { mutableFloatStateOf(56f) }
        var variant by remember { mutableStateOf(FeldFabVariant.Film) }

        val variants = FeldFabVariant.entries
        val variantNames = variants.map { it.name }

        ShowcaseSpec(
            preview = {
                FeldFab(
                    onClick = {},
                    contentDescription = "Add",
                    size = size.dp,
                    variant = variant,
                )
            },
            controls = {
                ControlChoice("variant", variantNames, variants.indexOf(variant)) {
                    variant = variants[it]
                }
                ControlSlider("size", size, 40f..80f, { size = it }) {
                    "${it.toInt()} dp"
                }
            },
            code = {
                kotlinCall(
                    "FeldFab",
                    "onClick" to "{ }",
                    "contentDescription" to "\"Add\"",
                    "size" to "${size.toInt()}.dp".takeIf { size != 56f },
                    "variant" to "FeldFabVariant.${variant.name}".takeIf {
                        variant != FeldFabVariant.Film
                    },
                )
            },
        )
    },
)

@Composable
internal fun ButtonsShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Buttons", "COMPONENTS", onBack, ButtonSubjects)
