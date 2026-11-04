package com.feldelysium.designsystem.components.input

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
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

private val CheckSpec = FeldPaper.Panel.copy(
    wear = 0.18f,
    ink = FeldPalette.Muted,
    inkWidth = 1.2.dp,
    inkMinAlpha = 0.75f,
)

/** A checkbox with a hand-inked box and a check mark that draws itself; the whole row is tappable. */
@Composable
public fun FeldCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    accent: Color = FeldAccentColors.Cream,
    boxSize: Dp = 22.dp,
    sound: Boolean = true,
    seed: Int = rememberSeed(),
) {
    val sounds = feldSounds
    val haptics = rememberFeldHaptics()
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val check = animateFloatAsState(if (checked) 1f else 0f, tween(170), label = "check")
    val press = animateFloatAsState(if (pressed && enabled) 1f else 0f, tween(90), label = "press")

    val shape = remember(seed) {
        RoughShape(
            seed,
            RoughStyle(
                roughness = 0.35.dp,
                step = 12.dp,
                corner = 2.dp,
                inset = 0.dp,
                cornerVariation = 0.2f,
                cornerJitter = 0.1f,
                tornChance = 0f,
                jerkChance = 0.04f,
            ),
        )
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 40.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .semantics(mergeDescendants = true) { toggleableState = ToggleableState(checked) }
            .feldClickable(
                enabled = enabled,
                interactionSource = interaction,
                role = Role.Checkbox,
                onClick = {
                    if (sound) {
                        if (checked) sounds.switchOff(seed) else sounds.switchOn(seed)
                    }
                    haptics.tick()
                    onCheckedChange(!checked)
                },
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            Modifier
                .size(boxSize)
                .graphicsLayer {
                    val s = 1f - 0.08f * press.value
                    scaleX = s
                    scaleY = s
                }
                .paperMaterial(shape, CheckSpec)
                .drawWithCache {
                    val w = size.width
                    val h = size.height
                    val mark = Path().apply {
                        moveTo(w * 0.22f, h * 0.54f)
                        lineTo(w * 0.42f, h * 0.74f)
                        lineTo(w * 0.80f, h * 0.28f)
                    }
                    val measure = PathMeasure().apply { setPath(mark, false) }
                    val length = measure.length
                    val part = Path()
                    val stroke = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    val inset = 3.dp.toPx()
                    val innerSize = Size(w - inset * 2, h - inset * 2)

                    onDrawWithContent {
                        drawContent()
                        val t = check.value
                        if (t > 0.001f) {
                            drawRect(accent, Offset(inset, inset), innerSize, alpha = 0.16f * t)
                            part.rewind()
                            measure.getSegment(0f, length * t, part, true)
                            drawPath(part, accent, style = stroke)
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
