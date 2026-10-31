package com.feldelysium.catalog.ui.pages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlChips
import com.feldelysium.catalog.ui.ControlField
import com.feldelysium.catalog.ui.ControlSlider
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.SectionLabel
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.catalog.ui.feedback.EventKey
import com.feldelysium.catalog.ui.feedback.FeedbackComponent
import com.feldelysium.catalog.ui.feedback.FeedbackHub
import com.feldelysium.catalog.ui.feedback.FeedbackState
import com.feldelysium.catalog.ui.feedback.HapticPreset
import com.feldelysium.catalog.ui.feedback.LocalFeedbackHub
import com.feldelysium.catalog.ui.feedback.SoundPreset
import com.feldelysium.designsystem.components.button.FeldButton
import com.feldelysium.designsystem.components.button.FeldDialogActions
import com.feldelysium.designsystem.components.button.FeldTextButton
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.input.FeldChip
import com.feldelysium.designsystem.components.input.FeldMenu
import com.feldelysium.designsystem.components.input.FeldMenuItem
import com.feldelysium.designsystem.components.input.FeldSegmentedControl
import com.feldelysium.designsystem.components.input.FeldSlider
import com.feldelysium.designsystem.components.input.FeldSwitch
import com.feldelysium.designsystem.foundation.sound.FeldSounds
import com.feldelysium.designsystem.foundation.sound.LocalFeldSounds
import com.feldelysium.designsystem.theme.FeldPalette

/** Components below report events themselves, so their built-in sounds are muted here. */
private object SilentSounds : FeldSounds {
    override fun click(seed: Int) = Unit
    override fun select() = Unit
    override fun toggle(seed: Int) = Unit
    override fun cancel() = Unit
    override fun save() = Unit
    override fun delete() = Unit
    override fun tab(seed: Int) = Unit
}

@Composable
internal fun SoundHapticsShowcase(onBack: () -> Unit) {
    val hub = LocalFeedbackHub.current
    val s = hub.state

    CompositionLocalProvider(LocalFeldSounds provides SilentSounds) {
        ShowcaseScreen(
            title = "Sound & Haptics",
            eyebrow = "FEEDBACK",
            onBack = onBack,
            subjects = listOf(
                Subject("Feedback") { seed ->
                    ShowcaseSpec(
                        preview = { FeedbackPreview(hub, seed) },
                        controls = { SoundControls(hub) },
                        variants = { HapticControls(hub) },
                        controlsLabel = "Sound",
                        variantsLabel = "Haptic",
                        code = { feedbackCode(s) },
                        notes = listOf(
                            "Choose an effect and assign it to a component event.",
                            "The app provides the sounds. Components only trigger them.",
                        ),
                        onTab = { s.mode = it },
                    )
                },
            ),
        )
    }
}

@Composable
private fun FeedbackPreview(hub: FeedbackHub, seed: Int) {
    val s = hub.state
    val soundMode = s.mode != 1
    val key = s.currentKey

    // Real components, real FeldSounds path: component -> hub.sounds -> preset.
    CompositionLocalProvider(LocalFeldSounds provides hub.sounds) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FeldButton(
                text = if (soundMode) "Play Sound" else "Test Haptic",
                onClick = { if (soundMode) hub.playSound(s.soundPreset) else hub.playHaptic(s.hapticPreset) },
                enabled = if (soundMode) s.soundEnabled else s.hapticEnabled,
                sound = false, // otherwise it also fires Button.Press
                seed = seed,
            )
            Text(
                "${key.component.title} · ${key.event}".uppercase(),
                style = CatalogMono,
                color = FeldPalette.Muted,
            )
            ComponentPreview(hub, key.component, seed)
        }
    }
}

@Composable
private fun ComponentPreview(hub: FeedbackHub, component: FeedbackComponent, seed: Int) {
    when (component) {
        FeedbackComponent.Button -> FeldButton(text = "Press me", onClick = {}, seed = seed)

        FeedbackComponent.Switch -> {
            var on by remember { mutableStateOf(false) }
            FeldSwitch(checked = on, onCheckedChange = { on = it }, seed = seed)
        }

        // No checkbox component in the design system yet, so this one fires manually.
        FeedbackComponent.Checkbox -> {
            var checked by remember { mutableStateOf(false) }
            FeldChip(
                text = if (checked) "Checked" else "Unchecked",
                selected = checked,
                onClick = {
                    checked = !checked
                    hub.fire(EventKey(component, if (checked) "Check" else "Uncheck"))
                },
            )
        }

        FeedbackComponent.Chip -> {
            var selected by remember { mutableStateOf(0) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("One", "Two", "Three").forEachIndexed { i, name ->
                    FeldChip(text = name, selected = i == selected, onClick = { selected = i })
                }
            }
        }

        FeedbackComponent.Tabs -> {
            var selected by remember { mutableStateOf(0) }
            FeldSegmentedControl(listOf("Day", "Week", "Month"), selected, { selected = it })
        }

        FeedbackComponent.Slider -> {
            var value by remember { mutableFloatStateOf(3f) }
            FeldSlider(
                value = value,
                onValueChange = { value = it },
                valueRange = 0f..10f,
                valueText = { "%.0f".format(it) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        FeedbackComponent.Dialog -> Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            FeldDialogActions(
                confirmLabel = "Save entry",
                cancelLabel = "Cancel",
                onConfirm = {},
                onCancel = {},
                seed = seed,
            )
            FeldDialogActions(
                confirmLabel = "Delete",
                cancelLabel = "Keep",
                onConfirm = {},
                onCancel = {},
                destructive = true,
                seed = seed + 1,
            )
        }

        FeedbackComponent.Dropdown -> {
            var open by remember { mutableStateOf(false) }
            Box {
                FeldTextButton(text = "Open menu", onClick = { open = true })
                FeldMenu(
                    expanded = open,
                    onDismiss = { open = false },
                    seed = seed,
                    items = listOf(
                        FeldMenuItem("Edit", FeldIconId.Edit, onClick = {}),
                        FeldMenuItem("Delete", FeldIconId.Delete, destructive = true, onClick = {}),
                    ),
                )
            }
        }
    }
}

// Sound tab

@Composable
private fun ColumnScope.SoundControls(hub: FeedbackHub) {
    val s = hub.state
    val params = s.soundParams(s.soundPreset)

    SectionLabel("Effect library")
    ControlChips(
        "Effect",
        SoundPreset.entries.map { it.title },
        s.soundPreset.ordinal,
    ) {
        s.soundPreset = SoundPreset.entries[it]
        hub.playSound(s.soundPreset)
    }

    if (s.soundPreset == SoundPreset.Custom) CustomSoundSection(hub)

    SectionLabel("Controls")
    ControlSwitch("Sound enabled", s.soundEnabled) { s.soundEnabled = it }
    ControlSlider("Volume", params.volume, 0f..1f, { s.soundParams[s.soundPreset] = params.copy(volume = it) }) {
        "%.0f%%".format(it * 100)
    }
    ControlSlider("Pitch", params.pitch, 0.5f..2f, { s.soundParams[s.soundPreset] = params.copy(pitch = it) }) {
        "%.2fx".format(it)
    }

    ComponentSection(hub, soundMode = true)
}

@Composable
private fun ColumnScope.CustomSoundSection(hub: FeedbackHub) {
    val s = hub.state
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null && hub.setCustom(uri)) hub.playSound(SoundPreset.Custom)
    }

    ControlField("Custom sound") {
        Text(s.customName ?: "No file selected", style = CatalogMono, color = FeldPalette.Cream)
        Row(
            Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            FeldTextButton("Choose file", onClick = {
                picker.launch(arrayOf("audio/wav", "audio/x-wav", "audio/ogg", "application/ogg"))
            })
            if (s.customUri != null) {
                FeldTextButton("Listen", onClick = { hub.playSound(SoundPreset.Custom) })
                FeldTextButton("Use default", onClick = {
                    hub.setCustom(null)
                    s.soundPreset = SoundPreset.Mechanical
                })
            }
        }
    }
}

// Haptic tab

@Composable
private fun ColumnScope.HapticControls(hub: FeedbackHub) {
    val s = hub.state
    val params = s.hapticParams(s.hapticPreset)

    SectionLabel("Effect library")
    ControlChips("Effect", HapticPreset.entries.map { it.title }, s.hapticPreset.ordinal) {
        s.hapticPreset = HapticPreset.entries[it]
        hub.playHaptic(s.hapticPreset)
    }

    SectionLabel("Controls")
    ControlSwitch("Haptic enabled", s.hapticEnabled) { s.hapticEnabled = it }
    ControlSlider(
        "Duration",
        params.durationMs,
        10f..200f,
        { s.hapticParams[s.hapticPreset] = params.copy(durationMs = it) }) {
        "%.0f ms".format(it)
    }
    if (hub.supportsIntensity) {
        ControlSlider(
            "Intensity",
            params.intensity,
            0.1f..1f,
            { s.hapticParams[s.hapticPreset] = params.copy(intensity = it) }) {
            "%.0f%%".format(it * 100)
        }
    } else {
        ControlField("Intensity") {
            Text("Not supported on this device", style = CatalogMono, color = FeldPalette.Muted)
        }
    }

    ComponentSection(hub, soundMode = false)
}

// Component assignment

@Composable
private fun ColumnScope.ComponentSection(hub: FeedbackHub, soundMode: Boolean) {
    val s = hub.state
    val key = s.currentKey

    SectionLabel("Component")
    ControlChips("Component", FeedbackComponent.entries.map { it.title }, s.component.ordinal) {
        s.component = FeedbackComponent.entries[it]
        s.eventIndex = 0
    }
    ControlChips("Event", s.component.events, s.eventIndex) { s.eventIndex = it }

    if (soundMode) {
        val current = s.soundFor(key)
        ControlChips(
            "Sound for this event",
            listOf("None") + SoundPreset.entries.map { it.title },
            (current?.ordinal ?: -1) + 1,
        ) {
            val picked = SoundPreset.entries.getOrNull(it - 1)
            s.soundAssign[key] = picked
            picked?.let(hub::playSound)
        }
    } else {
        val current = s.hapticFor(key)
        ControlChips(
            "Haptic for this event",
            listOf("None") + HapticPreset.entries.map { it.title },
            (current?.ordinal ?: -1) + 1,
        ) {
            val picked = HapticPreset.entries.getOrNull(it - 1)
            s.hapticAssign[key] = picked
            picked?.let(hub::playHaptic)
        }
    }

    FeldTextButton("Reset defaults", onClick = { s.reset(); hub.setCustom(null) })
}

/** Event -> FeldSounds intent. The catalog's mapping table, mirrored in the generated code. */
private val Intents = listOf(
    EventKey(FeedbackComponent.Button, "Press") to "click",
    EventKey(FeedbackComponent.Switch, "Turn On") to "switchOn",
    EventKey(FeedbackComponent.Switch, "Turn Off") to "switchOff",
    EventKey(FeedbackComponent.Chip, "Select") to "select",
    EventKey(FeedbackComponent.Tabs, "Switch Tab") to "tab",
    EventKey(FeedbackComponent.Slider, "Tick") to "tick",
    EventKey(FeedbackComponent.Dialog, "Confirm") to "save",
    EventKey(FeedbackComponent.Dialog, "Cancel") to "cancel",
    EventKey(FeedbackComponent.Dialog, "Delete") to "delete",
    EventKey(FeedbackComponent.Dropdown, "Open") to "menuOpen",
    EventKey(FeedbackComponent.Dropdown, "Select") to "menuSelect",
    EventKey(FeedbackComponent.Dropdown, "Close") to "menuClose",
)

private val withSeed = setOf(
    "click",
    "switchOn",
    "switchOff",
    "tab",
)

private fun feedbackCode(s: FeedbackState): String {
    val cues = Intents.joinToString("\n") { (key, intent) ->
        val preset = s.soundFor(key)
        val params = preset?.let(s::soundParams)

        val body = if (preset == null) {
            "Unit"
        } else {
            val asset = preset.asset ?: "custom"
            val volume = "%.2f".format(params!!.volume)
            val pitch = "%.2f".format(params.pitch)
            "play(\"$asset\", $volume, $pitch)"
        }

        val args = if (intent in withSeed) "seed: Int" else ""

        "    override fun $intent($args) = $body"
    }

    return """
        // 1. Load sounds in the app
        val sounds = SoundPlayer(
            context = context,
            assets = mapOf(
                "click" to R.raw.click,
                "switch" to R.raw.switch,
                "save" to R.raw.save,
            )
        )

        // 2. Map component events to sounds
        val feedback = object : FeldSounds {
        $cues
        }

        // 3. Provide feedback to the UI
        CompositionLocalProvider(
            LocalFeldSounds provides feedback
        ) {
            AppNavGraph()
        }

        // Components use the provided sounds automatically:
        @Composable
        fun SettingsRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
            FeldSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
    """.trimIndent()
}
