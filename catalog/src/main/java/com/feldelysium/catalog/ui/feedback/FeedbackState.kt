package com.feldelysium.catalog.ui.feedback

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

internal enum class SoundPreset(val title: String, val asset: String?) {
    Mechanical("Mechanical", "sounds/mechanical_click.ogg"),
    Radio("Radio", "sounds/radio_wave.mp3"),
    Tape("Tape", "sounds/tape_flip.ogg"),
    Soft("Soft", "sounds/soft_touch.ogg"),
    Industrial("Industrial", "sounds/metal_impact.ogg"),
    Vintage("Vintage", "sounds/leather_handle.ogg"),
    Coin("Coin Rattle", "sounds/coin_rattle.ogg"),
    SwitchClose("Switch Close", "sounds/switch_close.ogg"),
    Custom("Custom", null),
}

internal enum class HapticPreset(val title: String) {
    Light("Light"), Medium("Medium"), Heavy("Heavy"), Double("Double"), Pulse(
        "Pulse"
    )
}

internal data class SoundParams(val volume: Float = 1f, val pitch: Float = 1f)
internal data class HapticParams(val durationMs: Float, val intensity: Float)

internal fun HapticPreset.defaults() = when (this) {
    HapticPreset.Light -> HapticParams(15f, 0.5f)
    HapticPreset.Medium -> HapticParams(30f, 0.75f)
    HapticPreset.Heavy -> HapticParams(60f, 1f)
    HapticPreset.Double -> HapticParams(25f, 0.8f)
    HapticPreset.Pulse -> HapticParams(20f, 0.7f)
}

internal enum class FeedbackComponent(val title: String, val events: List<String>) {
    Button("Button", listOf("Press")),
    Switch("FeldSwitch", listOf("Turn On", "Turn Off")),
    Checkbox("Checkbox", listOf("Check", "Uncheck")),
    Chip("Chip", listOf("Select")),
    Tabs("Segmented", listOf("Switch Tab")),
    Slider("Slider", listOf("Tick")),
    Dialog("Dialog", listOf("Confirm", "Cancel", "Delete")),
    Dropdown("Dropdown", listOf("Open", "Select", "Close")),
}

internal data class EventKey(val component: FeedbackComponent, val event: String)

private fun defaultSound(k: EventKey): SoundPreset = when (k.component) {
    FeedbackComponent.Button -> SoundPreset.Mechanical

    FeedbackComponent.Switch ->
        if (k.event == "Turn On") SoundPreset.Mechanical else SoundPreset.SwitchClose

    FeedbackComponent.Dialog -> when (k.event) {
        "Confirm" -> SoundPreset.Radio
        "Delete" -> SoundPreset.Industrial
        else -> SoundPreset.Soft
    }

    FeedbackComponent.Dropdown -> when (k.event) {
        "Open" -> SoundPreset.Soft
        "Select" -> SoundPreset.Mechanical
        else -> SoundPreset.Soft
    }

    else -> SoundPreset.Soft
}

private fun defaultHaptic(k: EventKey): HapticPreset = when {
    k.component == FeedbackComponent.Switch && k.event == "Turn On" -> HapticPreset.Medium
    k.component == FeedbackComponent.Dialog && k.event == "Confirm" -> HapticPreset.Heavy
    k.component == FeedbackComponent.Dialog && k.event == "Delete" -> HapticPreset.Heavy
    else -> HapticPreset.Light
}

internal class FeedbackState {
    var mode by mutableIntStateOf(0) // 0 = Sound, 1 = Haptic

    var soundEnabled by mutableStateOf(true)
    var hapticEnabled by mutableStateOf(true)

    var soundPreset by mutableStateOf(SoundPreset.Mechanical)
    var hapticPreset by mutableStateOf(HapticPreset.Light)

    val soundParams = mutableStateMapOf<SoundPreset, SoundParams>()
    val hapticParams = mutableStateMapOf<HapticPreset, HapticParams>()

    var customUri by mutableStateOf<Uri?>(null)
    var customName by mutableStateOf<String?>(null)

    var component by mutableStateOf(FeedbackComponent.Button)
    var eventIndex by mutableIntStateOf(0)

    // Only overrides live here; a null value means "None" was chosen explicitly.
    val soundAssign = mutableStateMapOf<EventKey, SoundPreset?>()
    val hapticAssign = mutableStateMapOf<EventKey, HapticPreset?>()

    val currentKey get() = EventKey(component, component.events[eventIndex.coerceIn(component.events.indices)])

    fun soundParams(p: SoundPreset) = soundParams[p] ?: SoundParams()
    fun hapticParams(p: HapticPreset) = hapticParams[p] ?: p.defaults()

    fun soundFor(k: EventKey): SoundPreset? = if (soundAssign.containsKey(k)) soundAssign[k] else defaultSound(k)
    fun hapticFor(k: EventKey): HapticPreset? = if (hapticAssign.containsKey(k)) hapticAssign[k] else defaultHaptic(k)

    fun reset() {
        soundEnabled = true
        hapticEnabled = true
        soundPreset = SoundPreset.Mechanical
        hapticPreset = HapticPreset.Light
        soundParams.clear()
        hapticParams.clear()
        soundAssign.clear()
        hapticAssign.clear()
        customUri = null
        customName = null
        component = FeedbackComponent.Button
        eventIndex = 0
    }
}
