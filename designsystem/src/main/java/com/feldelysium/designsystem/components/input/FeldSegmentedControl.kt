package com.feldelysium.designsystem.components.input

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.foundation.haptics.rememberFeldHaptics
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.foundation.material.paperMaterial
import com.feldelysium.designsystem.foundation.seed.rememberSeed
import com.feldelysium.designsystem.foundation.shape.RoughShape
import com.feldelysium.designsystem.foundation.shape.RoughStyle
import com.feldelysium.designsystem.foundation.sound.feldSounds
import com.feldelysium.designsystem.theme.FeldPalette
import com.feldelysium.designsystem.theme.FeldTheme
import kotlin.math.abs
import kotlin.random.Random
import androidx.compose.ui.unit.lerp as lerpDp
private val TrackRough = RoughStyle(
    roughness = 0.6.dp,
    step = 36.dp,
    corner = 16.dp,
    cornerVariation = 0.15f,
    tornChance = 0f,
    jerkChance = 0.05f,
)

private val TrackPaper = FeldPaper.Panel.copy(
    ink = Color(0xFFCBBFA5),
    inkWidth = 1.3.dp,
    inkMinAlpha = 0.5f,
    inkContrast = 1.6f,
)

/** How much wider the selected segment is than the others (0.5 = 1.5x). */
private const val SELECTED_GROW = 0.3f

/** Corner radius of the plate at the ends of the track, and between segments. */
private val OuterRadius = 13.dp
private val InnerRadius = 3.dp

private val OnText = Color(0xFF0E1816)
private val DividerColor = Color(0xFFB9AD92)
private val GrainColor = Color(0xFF7A6A4C)
private val EdgeColor = Color(0xFF8C7A55)

private class Speck(val x: Float, val y: Float, val radiusDp: Float, val alpha: Float)

private fun makeSpecks(seed: Int): List<Speck> {
    val random = Random(seed)
    return List(40) {
        Speck(
            x = random.nextFloat(),
            y = random.nextFloat(),
            radiusDp = 0.4f + random.nextFloat() * 0.7f,
            alpha = 0.10f + random.nextFloat() * 0.18f,
        )
    }
}

private fun selectedness(index: Int, position: Float): Float = (1f - abs(position - index)).coerceIn(0f, 1f)

/**
 * A full-width segmented control. The selected segment grows wider than the others,
 * and a cream paper plate follows it. The plate is rounded only at the ends of the track.
 */
@Composable
public fun FeldSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    seed: Int = rememberSeed(),
) {
    val haptics = rememberFeldHaptics()
    val sounds = feldSounds
    val count = options.size.coerceAtLeast(1)
    val selected = selectedIndex.coerceIn(0, count - 1)
    val trackShape = remember(seed) { RoughShape(seed, TrackRough) }
    val specks = remember(seed) { makeSpecks(seed) }

    val rawPosition by animateFloatAsState(
        targetValue = selected.toFloat(),
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 420f),
        label = "segmentPosition",
    )
    val position = rawPosition.coerceIn(0f, (count - 1).toFloat())

    var pressed by remember { mutableIntStateOf(-1) }
    val press by animateFloatAsState(
        targetValue = if (pressed == selected) 1f else 0f,
        animationSpec = tween(80),
        label = "segmentPress",
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .paperMaterial(trackShape, TrackPaper)
            .padding(3.dp),
    ) {
        // Geometry: every segment has weight 1, the selected one 1 + SelectedGrow.
        // The weights are interpolated by `position`, so the layout flows during the animation.
        val unit = maxWidth / (count + SELECTED_GROW)
        val sel = List(count) { selectedness(it, position) }
        val widths = List(count) { unit * (1f + SELECTED_GROW * sel[it]) }
        val lefts = buildList {
            var acc = 0.dp
            widths.forEach {
                add(acc)
                acc += it
            }
        }
        val plateX = lefts.indices.fold(0.dp) { acc, i -> acc + lefts[i] * sel[i] }
        val plateW = widths.indices.fold(0.dp) { acc, i -> acc + widths[i] * sel[i] }

        val leftRadius = lerpDp(InnerRadius, OuterRadius, (1f - plateX / unit).coerceIn(0f, 1f))
        val rightGap = maxWidth - plateX - plateW
        val rightRadius = lerpDp(InnerRadius, OuterRadius, (1f - rightGap / unit).coerceIn(0f, 1f))

        for (k in 1 until count) {
            FeldDivider(
                vertical = true,
                seed = seed + k,
                color = DividerColor,
                modifier = Modifier
                    .offset(x = lefts[k])
                    .padding(vertical = 8.dp)
                    .graphicsLayer { alpha = (1f - (sel[k - 1] + sel[k])).coerceIn(0f, 1f) },
            )
        }

        // The plate. The grain is fixed to the track, so the plate slides over it like a window.
        Box(
            Modifier
                .fillMaxSize()
                .drawBehind {
                    val inset = 1.2.dp.toPx() * press
                    val left = plateX.toPx() + inset
                    val right = (plateX + plateW).toPx() - inset
                    val top = inset
                    val bottom = size.height - inset
                    val l = CornerRadius(leftRadius.toPx())
                    val r = CornerRadius(rightRadius.toPx())
                    val plate = Path().apply { addRoundRect(RoundRect(left, top, right, bottom, l, r, r, l)) }

                    translate(top = 1.5.dp.toPx()) { drawPath(plate, Color.Black.copy(alpha = 0.3f)) }

                    clipPath(plate) {
                        drawRect(FeldPalette.Cream)
                        specks.forEach {
                            drawCircle(
                                color = GrainColor.copy(alpha = it.alpha),
                                radius = it.radiusDp.dp.toPx(),
                                center = Offset(it.x * size.width, it.y * size.height),
                            )
                        }
                        val lit = top + 0.5.dp.toPx()
                        drawLine(
                            Color.White.copy(alpha = 0.45f),
                            Offset(left + l.x, lit),
                            Offset(right - r.x, lit),
                            strokeWidth = 1.dp.toPx(),
                        )
                        val shade = bottom - 0.5.dp.toPx()
                        drawLine(
                            EdgeColor.copy(alpha = 0.35f),
                            Offset(left + l.x, shade),
                            Offset(right - r.x, shade),
                            strokeWidth = 1.dp.toPx(),
                        )
                    }
                },
        )

        options.forEachIndexed { i, label ->
            val isSelected = i == selected
            Box(
                modifier = Modifier
                    .offset(x = lefts[i])
                    .width(widths[i])
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) {
                        role = Role.Tab
                        this.selected = isSelected
                        onClick {
                            onSelect(i)
                            true
                        }
                    }
                    .pointerInput(i, selected) {
                        detectTapGestures(
                            onPress = {
                                pressed = i
                                tryAwaitRelease()
                                pressed = -1
                            },
                            onTap = {
                                if (i != selected) {
                                    haptics.tick()
                                    sounds.tab(seed + i)
                                }
                                onSelect(i)
                            },
                        )
                    }
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 1.2.sp,
                    ),
                    color = lerp(FeldPalette.Cream, OnText, sel[i]),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1011)
@Composable
private fun FeldSegmentedControlPreview() {
    FeldTheme {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            var a by remember { mutableIntStateOf(0) }
            var b by remember { mutableIntStateOf(1) }
            var c by remember { mutableIntStateOf(2) }
            FeldSegmentedControl(listOf("Tab 1", "Tab 2", "Tab 33333333333"), a, { a = it })
            FeldSegmentedControl(listOf("Media", "Color"), b, { b = it })
            FeldSegmentedControl(listOf("Day", "Week", "Month", "All"), c, { c = it })
        }
    }
}
