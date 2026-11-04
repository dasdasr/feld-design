package com.feldelysium.designsystem.components.input

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.designsystem.foundation.haptics.rememberFeldHaptics
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.foundation.material.paperMaterial
import com.feldelysium.designsystem.foundation.seed.rememberSeed
import com.feldelysium.designsystem.foundation.shape.RoughShape
import com.feldelysium.designsystem.foundation.shape.RoughStyle
import com.feldelysium.designsystem.foundation.sound.feldSounds
import com.feldelysium.designsystem.theme.FeldPalette

private val RadioSpec = FeldPaper.Panel.copy(
    wear = 0.18f,
    ink = FeldPalette.Muted,
    inkWidth = 1.2.dp,
    inkMinAlpha = 0.75f,
)

/** A radio button with a hand-inked ring and a diamond mark; group items with Modifier.selectableGroup(). */
@Composable
public fun FeldRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    accent: Color = FeldAccentColors.Cream,
    size: Dp = 22.dp,
    sound: Boolean = true,
    seed: Int = rememberSeed(),
) {
    val sounds = feldSounds
    val haptics = rememberFeldHaptics()
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val mark = animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(180, easing = FastOutSlowInEasing),
        label = "mark",
    )
    val press = animateFloatAsState(if (pressed && enabled) 1f else 0f, tween(90), label = "press")

    val ring = remember(seed, size) {
        RoughShape(
            seed,
            RoughStyle(
                roughness = 0.35.dp,
                step = 12.dp,
                corner = size / 2,
                inset = 0.dp,
                cornerVariation = 0f,
                cornerJitter = 0f,
                tornChance = 0f,
                jerkChance = 0.03f,
            ),
        )
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 40.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .semantics(mergeDescendants = true) { this.selected = selected }
            .feldClickable(
                enabled = enabled,
                interactionSource = interaction,
                role = Role.RadioButton,
                onClick = {
                    if (!selected) {
                        if (sound) sounds.switchOn(seed)
                        haptics.tick()
                    }
                    onClick()
                },
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            Modifier
                .size(size)
                .graphicsLayer {
                    val s = 1f - 0.08f * press.value
                    scaleX = s
                    scaleY = s
                }
                .paperMaterial(ring, RadioSpec)
                .drawWithCache {
                    val w = this.size.width
                    val h = this.size.height
                    val r = w * 0.28f
                    val diamond = Path().apply {
                        moveTo(w / 2f, h / 2f - r)
                        lineTo(w / 2f + r, h / 2f)
                        lineTo(w / 2f, h / 2f + r)
                        lineTo(w / 2f - r, h / 2f)
                        close()
                    }
                    onDrawWithContent {
                        drawContent()
                        val t = mark.value
                        if (t > 0.001f) {
                            scale(t, pivot = center) { drawPath(diamond, accent) }
                        }
                    }
                },
        )
        if (label != null) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.2.sp),
                color = FeldPalette.Cream,
            )
        }
    }
}
