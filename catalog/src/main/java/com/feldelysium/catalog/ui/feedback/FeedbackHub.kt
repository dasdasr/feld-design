package com.feldelysium.catalog.ui.feedback

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.staticCompositionLocalOf
import com.feldelysium.designsystem.foundation.sound.FeldSounds

internal class FeedbackHub(context: Context) {
    val state = FeedbackState()
    private val player = CatalogSoundPlayer(context)
    private val haptics = CatalogHaptics(context)

    val supportsIntensity get() = haptics.supportsIntensity

    fun playSound(preset: SoundPreset) {
        if (!state.soundEnabled) return
        val p = state.soundParams(preset)
        player.play(preset, p.volume, p.pitch)
    }

    fun playHaptic(preset: HapticPreset) {
        if (!state.hapticEnabled) return
        haptics.play(preset, state.hapticParams(preset))
    }

    fun fire(key: EventKey) {
        state.soundFor(key)?.let(::playSound)
        state.hapticFor(key)?.let(::playHaptic)
    }

    fun setCustom(uri: Uri?): Boolean {
        val ok = player.setCustom(uri)
        state.customUri = if (ok) uri else null
        state.customName = if (ok) uri?.lastPathSegment?.substringAfterLast('/') else null
        return ok
    }

    fun release() = player.release()

    /** What the rest of the catalog sees; maps FeldSounds calls onto component events. */
    val sounds: FeldSounds = object : FeldSounds {
        private fun f(c: FeedbackComponent, e: String) = fire(EventKey(c, e))
        override fun click(seed: Int) = f(FeedbackComponent.Button, "Press")
        override fun select() = f(FeedbackComponent.Chip, "Select")
        override fun toggle(seed: Int) = f(FeedbackComponent.Switch, "Turn On") // legacy fallback
        override fun cancel() = f(FeedbackComponent.Dialog, "Cancel")
        override fun save() = f(FeedbackComponent.Dialog, "Confirm")
        override fun delete() = f(FeedbackComponent.Dialog, "Delete")
        override fun tab(seed: Int) = f(FeedbackComponent.Tabs, "Switch Tab")
        override fun switchOn(seed: Int) = f(FeedbackComponent.Switch, "Turn On")
        override fun switchOff(seed: Int) = f(FeedbackComponent.Switch, "Turn Off")
        override fun menuOpen() = f(FeedbackComponent.Dropdown, "Open")
        override fun menuClose() = f(FeedbackComponent.Dropdown, "Close")
        override fun menuSelect() = f(FeedbackComponent.Dropdown, "Select")
        override fun tick() = f(FeedbackComponent.Slider, "Tick")
    }
}

internal val LocalFeedbackHub = staticCompositionLocalOf<FeedbackHub> { error("FeedbackHub not provided") }
