
package com.feldelysium.catalog.ui.feedback

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi

internal class CatalogHaptics(context: Context) {

    private val vibrator: Vibrator =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(VibratorManager::class.java).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

    val supportsIntensity: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                vibrator.hasAmplitudeControl()

    fun play(preset: HapticPreset, p: HapticParams) {
        if (!vibrator.hasVibrator()) return

        val duration = p.durationMs.toLong().coerceAtLeast(1)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            playModern(preset, duration, p.intensity)
        } else {
            playLegacy(preset, duration)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun playModern(
        preset: HapticPreset,
        duration: Long,
        intensity: Float,
    ) {
        val amplitude = if (supportsIntensity) {
            (intensity * 255).toInt().coerceIn(1, 255)
        } else {
            VibrationEffect.DEFAULT_AMPLITUDE
        }

        val effect = when (preset) {
            HapticPreset.Double -> VibrationEffect.createWaveform(
                longArrayOf(0, duration, 70, duration),
                intArrayOf(0, amplitude, 0, amplitude),
                -1,
            )

            HapticPreset.Pulse -> VibrationEffect.createWaveform(
                longArrayOf(0, duration, 45, duration, 45, duration),
                intArrayOf(0, amplitude, 0, amplitude, 0, amplitude),
                -1,
            )

            else -> VibrationEffect.createOneShot(duration, amplitude)
        }

        vibrator.vibrate(effect)
    }

    @Suppress("DEPRECATION")
    private fun playLegacy(preset: HapticPreset, duration: Long) {
        when (preset) {
            HapticPreset.Double -> vibrator.vibrate(
                longArrayOf(0, duration, 70, duration),
                -1,
            )

            HapticPreset.Pulse -> vibrator.vibrate(
                longArrayOf(0, duration, 45, duration, 45, duration),
                -1,
            )

            else -> vibrator.vibrate(duration)
        }
    }
}
