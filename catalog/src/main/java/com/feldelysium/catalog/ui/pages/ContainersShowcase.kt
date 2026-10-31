package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
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
import com.feldelysium.designsystem.components.container.FeldCard
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.components.container.FeldNoteCard
import com.feldelysium.designsystem.components.container.FeldPaperBox
import com.feldelysium.designsystem.components.container.FeldSectionHeader
import com.feldelysium.designsystem.components.container.FeldToastHost
import com.feldelysium.designsystem.components.container.rememberFeldToastState
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.theme.FeldPalette

private val PaperLabels = listOf("Panel", "Field", "Note")
private val PaperSpecs = listOf(FeldPaper.Panel, FeldPaper.Field, FeldPaper.Note)

private val ContainerSubjects = listOf(
    Subject("Card") { seed ->
        var text by remember { mutableStateOf("A card on paper") }
        var textured by remember { mutableStateOf(true) }
        var decals by remember { mutableStateOf(true) }
        var clickable by remember { mutableStateOf(true) }
        var taps by remember { mutableIntStateOf(0) }
        ShowcaseSpec(
            textured = true,
            preview = {
                FeldCard(
                    modifier = Modifier.fillMaxWidth(),
                    seed = seed,
                    textured = textured,
                    showDecals = decals,
                    onClick = if (clickable) ({ taps += 1 }) else null,
                ) {
                    Text(
                        if (clickable) "$text · $taps" else text,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
            controls = {
                ControlText("text", text) { text = it }
                ControlSwitch("textured", textured) { textured = it }
                ControlSwitch("decals", decals) { decals = it }
                ControlSwitch("clickable", clickable) { clickable = it }
            },
            code = {
                kotlinCall(
                    "FeldCard",
                    "modifier" to "Modifier.fillMaxWidth()",
                    "textured" to "false".takeIf { !textured },
                    "showDecals" to "false".takeIf { !decals },
                    "onClick" to "{ }".takeIf { clickable },
                    "content" to "{ Text(${text.quoted()}) }",
                )
            },
            notes = listOf("The card reveals on enter and presses with a small scale."),
        )
    },
    Subject("Paper box") { seed ->
        var paper by remember { mutableIntStateOf(1) }
        var textured by remember { mutableStateOf(true) }
        ShowcaseSpec(
            textured = true,
            preview = {
                FeldPaperBox(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    seed = seed,
                    spec = PaperSpecs[paper],
                    textured = textured,
                )
            },
            controls = {
                ControlChoice("paper", PaperLabels, paper) { paper = it }
                ControlSwitch("textured", textured) { textured = it }
            },
            code = {
                kotlinCall(
                    "FeldPaperBox",
                    "modifier" to "Modifier.fillMaxWidth()",
                    "seed" to "rememberSeed()",
                    "spec" to "FeldPaper.${PaperLabels[paper]}",
                    "textured" to "false".takeIf { !textured },
                )
            },
        )
    },
    Subject("Note card") { seed ->
        var title by remember { mutableStateOf("Field note") }
        var text by remember { mutableStateOf("A card with a coloured tab.") }
        var accent by remember { mutableIntStateOf(2) }
        val colors = listOf(FeldPalette.Cream, FeldPalette.Teal, FeldPalette.Orange)
        ShowcaseSpec(
            textured = true,
            preview = { FeldNoteCard(title = title, accent = colors[accent], text = text, index = 1, seed = seed) },
            controls = {
                ControlText("title", title) { title = it }
                ControlText("text", text) { text = it }
                ControlChoice("accent", AccentNames, accent) { accent = it }
            },
            code = {
                kotlinCall(
                    "FeldNoteCard",
                    "title" to title.quoted(),
                    "accent" to "FeldPalette.${AccentNames[accent]}",
                    "text" to text.quoted(),
                    "index" to "1",
                )
            },
        )
    },
    Subject("Divider") { seed ->
        var vertical by remember { mutableStateOf(false) }
        var thickness by remember { mutableFloatStateOf(1f) }
        ShowcaseSpec(
            textured = true,
            preview = {
                Box(Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                    FeldDivider(vertical = vertical, thickness = thickness.dp, seed = seed)
                }
            },
            controls = {
                ControlSwitch("vertical", vertical) { vertical = it }
                ControlSlider("thickness", thickness, 1f..6f, { thickness = it }) { "${it.toInt()} dp" }
            },
            code = {
                kotlinCall(
                    "FeldDivider",
                    "vertical" to "true".takeIf { vertical },
                    "thickness" to "${thickness.toInt()}.dp".takeIf { thickness.toInt() != 1 },
                )
            },
        )
    },
    Subject("Section header") { _ ->
        var title by remember { mutableStateOf("Section") }
        ShowcaseSpec(
            preview = { FeldSectionHeader(title = title) },
            controls = { ControlText("title", title) { title = it } },
            code = { kotlinCall("FeldSectionHeader", "title" to title.quoted()) },
        )
    },
    Subject("Toast") { _ ->
        var text by remember { mutableStateOf("Scene saved") }
        var accent by remember { mutableIntStateOf(2) }
        val colors = listOf(FeldPalette.Cream, FeldPalette.Teal, FeldPalette.Orange)
        val toast = rememberFeldToastState()
        ShowcaseSpec(
            preview = {
                Box(Modifier.fillMaxWidth().height(120.dp)) {
                    Box(Modifier.align(Alignment.TopCenter)) {
                        FeldGhostButton(text = "Show toast", onClick = { toast.show(text = text, accent = colors[accent]) })
                    }
                    FeldToastHost(state = toast, modifier = Modifier.align(Alignment.BottomCenter))
                }
            },
            controls = {
                ControlText("text", text) { text = it }
                ControlChoice("accent", AccentNames, accent) { accent = it }
            },
            code = {
                "val toast = rememberFeldToastState()\n\n" +
                        "toast.show(text = ${text.quoted()}, accent = FeldPalette.${AccentNames[accent]})\n\n" +
                        "FeldToastHost(state = toast)"
            },
        )
    },
)

@Composable
internal fun ContainersShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Containers", "COMPONENTS", onBack, ContainerSubjects)
