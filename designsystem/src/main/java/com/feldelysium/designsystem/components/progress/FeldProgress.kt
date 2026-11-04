package com.feldelysium.designsystem.components.progress

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feldelysium.designsystem.components.input.FeldAccentColors
import com.feldelysium.designsystem.theme.FeldPalette

private val OffColor = Color(0xFF1F2322)
private const val SEGMENT_FRACTION = 0.35f

/** A determinate linear progress bar styled as perforated tape; [progress] is read in the draw phase. */
@Composable
public fun FeldProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    accent: Color = FeldAccentColors.Cream,
    trackHeight: Dp = 8.dp,
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(trackHeight)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(progress().coerceIn(0f, 1f), 0f..1f)
            }
            .tapeTrack(accent, start = { 0f }, end = { progress().coerceIn(0f, 1f) }),
    )
}

/** An indeterminate linear progress bar with a sliding tape segment. */
@Composable
public fun FeldProgressIndicator(
    modifier: Modifier = Modifier,
    accent: Color = FeldAccentColors.Cream,
    trackHeight: Dp = 8.dp,
) {
    val slide = rememberInfiniteTransition(label = "progress").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1300, easing = LinearEasing)),
        label = "slide",
    )
    Box(
        modifier
            .fillMaxWidth()
            .height(trackHeight)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate }
            .tapeTrack(
                accent,
                start = { slide.value * (1f + SEGMENT_FRACTION) - SEGMENT_FRACTION },
                end = { slide.value * (1f + SEGMENT_FRACTION) },
            ),
    )
}

/** A determinate circular progress ring; [progress] is read in the draw phase. */
@Composable
public fun FeldCircularProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    accent: Color = FeldAccentColors.Cream,
    size: Dp = 32.dp,
    strokeWidth: Dp = 4.dp,
) {
    Canvas(
        modifier
            .size(size)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(progress().coerceIn(0f, 1f), 0f..1f)
            },
    ) {
        drawRing(accent, strokeWidth.toPx(), startAngle = -90f, sweep = 360f * progress().coerceIn(0f, 1f))
    }
}

/** An indeterminate circular progress ring with a rotating, breathing arc. */
@Composable
public fun FeldCircularProgressIndicator(
    modifier: Modifier = Modifier,
    accent: Color = FeldAccentColors.Cream,
    size: Dp = 32.dp,
    strokeWidth: Dp = 4.dp,
) {
    val transition = rememberInfiniteTransition(label = "circular")
    val rotation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing)),
        label = "rotation",
    )
    val sweep = transition.animateFloat(
        initialValue = 40f,
        targetValue = 220f,
        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "sweep",
    )
    Canvas(
        modifier
            .size(size)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate },
    ) {
        drawRing(accent, strokeWidth.toPx(), startAngle = rotation.value, sweep = sweep.value)
    }
}

private fun DrawScope.drawRing(accent: Color, stroke: Float, startAngle: Float, sweep: Float) {
    val topLeft = Offset(stroke / 2f, stroke / 2f)
    val arcSize = Size(size.width - stroke, size.height - stroke)
    drawArc(OffColor, 0f, 360f, false, topLeft, arcSize, style = Stroke(stroke))
    if (sweep > 0.5f) {
        drawArc(accent, startAngle, sweep, false, topLeft, arcSize, style = Stroke(stroke, cap = StrokeCap.Butt))
    }
}

private fun Modifier.tapeTrack(fill: Color, start: () -> Float, end: () -> Float): Modifier = drawWithCache {
    val corner = CornerRadius(2.dp.toPx())
    val rim = Stroke(0.8.dp.toPx())
    val notches = PathEffect.dashPathEffect(floatArrayOf(1.5.dp.toPx(), 6.5.dp.toPx()))
    val notchColor = FeldPalette.Ink.copy(alpha = 0.28f)
    val rimColor = FeldPalette.Line.copy(alpha = 0.6f)

    onDrawBehind {
        drawRoundRect(OffColor, cornerRadius = corner)
        val l = start().coerceIn(0f, 1f) * size.width
        val r = end().coerceIn(0f, 1f) * size.width
        if (r - l > 0.5f) {
            clipRect(left = l, right = r) {
                drawRoundRect(fill, cornerRadius = corner)
                drawLine(
                    color = notchColor,
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width, size.height / 2f),
                    strokeWidth = size.height,
                    pathEffect = notches,
                )
            }
        }
        drawRoundRect(rimColor, cornerRadius = corner, style = rim)
    }
}
