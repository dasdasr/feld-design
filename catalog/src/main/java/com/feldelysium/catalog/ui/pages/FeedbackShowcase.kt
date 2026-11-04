package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.feldelysium.catalog.ui.fmt
import com.feldelysium.catalog.ui.kotlinCall
import com.feldelysium.catalog.ui.quoted
import com.feldelysium.designsystem.components.button.FeldGhostButton
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.feedback.FeldBadge
import com.feldelysium.designsystem.components.feedback.FeldEmptyState
import com.feldelysium.designsystem.components.feedback.FeldErrorState
import com.feldelysium.designsystem.components.feedback.FeldLoadingState
import com.feldelysium.designsystem.components.feedback.FeldTooltip
import com.feldelysium.designsystem.components.progress.FeldProgressIndicator
import com.feldelysium.designsystem.theme.FeldPalette

private val BadgeColors = listOf(FeldPalette.Cream, FeldPalette.Teal, FeldPalette.Orange)
private val StateNames = listOf("Empty", "Loading", "Error")

private val FeedbackSubjects = listOf(
    Subject("Badge") { _ ->
        var text by remember { mutableStateOf("New") }
        var accent by remember { mutableIntStateOf(2) }
        var content by remember { mutableIntStateOf(0) }
        var unit by remember { mutableFloatStateOf(6f) }
        var rotation by remember { mutableFloatStateOf(-5f) }

        ShowcaseSpec(
            preview = {
                FeldBadge(
                    text = text,
                    accent = BadgeColors[accent],
                    contentColor = AccentColors[content],
                    unit = unit,
                    rotation = rotation,
                )
            },
            controls = {
                ControlText("text", text) { text = it }

                ControlChoice("accent", AccentNames, accent) {
                    accent = it
                }

                ControlSlider("size", unit, 4f..12f, { unit = it }) {
                    "${it.fmt(1)} dp"
                }

                ControlSlider("tilt", rotation, -20f..20f, { rotation = it }) {
                    "${it.toInt()}°"
                }
            },
            variants = {
                Specimen("Accents") {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AccentNames.forEachIndexed { i, name ->
                            FeldBadge(
                                text = name,
                                accent = BadgeColors[i],
                                contentColor = if (i == 0) FeldPalette.Ink else androidx.compose.ui.graphics.Color.White,
                            )
                        }
                    }
                }
            },
            code = {
                kotlinCall(
                    "FeldBadge",
                    "text" to text.quoted(),
                    "accent" to "FeldPalette.${AccentNames[accent]}".takeIf {
                        accent != 2
                    },
                    "unit" to "${unit.fmt(1)}f".takeIf {
                        unit != 6f
                    },
                    "rotation" to "${rotation.fmt(1)}f".takeIf {
                        rotation != -5f
                    },
                )
            },
            notes = listOf(
                "A dry-marker stamp for statuses and short labels.",
                "Unit scales typography, padding and outline together.",
                "Rotation is expressed in degrees.",
            ),
        )
    },
    Subject("Progress") { _ ->
        var indeterminate by remember { mutableStateOf(false) }
        var value by remember { mutableFloatStateOf(0.4f) }
        var accent by remember { mutableIntStateOf(0) }
        var track by remember { mutableFloatStateOf(8f) }
        ShowcaseSpec(
            preview = {
                if (indeterminate) {
                    FeldProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        accent = AccentColors[accent],
                        trackHeight = track.dp,
                    )
                } else {
                    FeldProgressIndicator(
                        progress = { value },
                        modifier = Modifier.fillMaxWidth(),
                        accent = AccentColors[accent],
                        trackHeight = track.dp,
                    )
                }
            },
            controls = {
                ControlSwitch("indeterminate", indeterminate) { indeterminate = it }
                if (!indeterminate) {
                    ControlSlider("progress", value, 0f..1f, { value = it }) { "${(it * 100).toInt()}%" }
                }
                ControlChoice("accent", AccentNames, accent) { accent = it }
                ControlSlider("track height", track, 4f..20f, { track = it }) { "${it.toInt()} dp" }
            },
            code = {
                kotlinCall(
                    "FeldProgressIndicator",
                    "progress" to "{ ${value.fmt(2)} }".takeIf { !indeterminate },
                    "accent" to "FeldAccentColors.${AccentNames[accent]}",
                    "trackHeight" to "${track.toInt()}.dp".takeIf { track != 8f },
                )
            },
            notes = listOf("Determinate progress is a lambda: it is read in the draw phase, so updates skip recomposition."),
        )
    },
    Subject("States") { _ ->
        var state by remember { mutableIntStateOf(0) }
        var title by remember { mutableStateOf("Nothing here yet") }
        var description by remember { mutableStateOf("Scenes you create will show up here.") }
        var hasIcon by remember { mutableStateOf(true) }
        var hasAction by remember { mutableStateOf(true) }
        var retries by remember { mutableIntStateOf(0) }
        ShowcaseSpec(
            preview = {
                Box(Modifier.fillMaxWidth().height(220.dp)) {
                    when (state) {
                        0 -> FeldEmptyState(
                            title = title,
                            description = description.ifBlank { null },
                            icon = if (hasIcon) FeldIconId.Search else null,
                            action = if (hasAction) {
                                { FeldGhostButton(text = "Create scene", onClick = {}) }
                            } else {
                                null
                            },
                        )
                        1 -> FeldLoadingState(label = title.ifBlank { null })
                        else -> FeldErrorState(
                            title = title,
                            message = description.ifBlank { null },
                            onRetry = if (hasAction) ({ retries += 1 }) else null,
                        )
                    }
                }
            },
            controls = {
                ControlChoice("state", StateNames, state) { state = it }
                ControlText(if (state == 1) "label" else "title", title) { title = it }
                if (state != 1) ControlText("description", description) { description = it }
                if (state == 0) ControlSwitch("icon", hasIcon) { hasIcon = it }
                if (state != 1) ControlSwitch(if (state == 0) "action" else "retry button", hasAction) { hasAction = it }
            },
            code = {
                when (state) {
                    0 -> kotlinCall(
                        "FeldEmptyState",
                        "title" to title.quoted(),
                        "description" to description.ifBlank { null }?.quoted(),
                        "icon" to "FeldIconId.Search".takeIf { hasIcon },
                        "action" to "{ FeldGhostButton(text = \"Create scene\", onClick = { }) }".takeIf { hasAction },
                    )
                    1 -> kotlinCall("FeldLoadingState", "label" to title.ifBlank { null }?.quoted())
                    else -> kotlinCall(
                        "FeldErrorState",
                        "title" to title.quoted(),
                        "message" to description.ifBlank { null }?.quoted(),
                        "onRetry" to "{ }".takeIf { hasAction },
                    )
                }
            },
            notes = listOf(
                "Full-block placeholders: they centre themselves in whatever space they get.",
                "Error retry presses so far: $retries.",
            ),
        )
    },
    Subject("Tooltip") { _ ->
        var text by remember { mutableStateOf("Saves the current scene") }
        ShowcaseSpec(
            preview = {
                FeldTooltip(text = text) {
                    FeldGhostButton(text = "Hold me", onClick = {})
                }
            },
            controls = { ControlText("text", text) { text = it } },
            code = {
                "FeldTooltip(text = ${text.quoted()}) {\n" +
                        "    FeldGhostButton(text = \"Hold me\", onClick = { })\n" +
                        "}"
            },
            notes = listOf("Long-press or hover. The tooltip never swallows the content's own taps."),
        )
    },
)

@Composable
internal fun FeedbackShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Feedback", "COMPONENTS", onBack, FeedbackSubjects)
