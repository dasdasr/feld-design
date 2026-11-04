package com.feldelysium.designsystem.components.feedback

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.foundation.material.paperMaterial
import com.feldelysium.designsystem.foundation.shape.RoughShape
import com.feldelysium.designsystem.foundation.shape.RoughStyle
import com.feldelysium.designsystem.theme.FeldPalette
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull

private val TipRough = RoughStyle(
    roughness = 0.5.dp,
    step = 30.dp,
    corner = 2.dp,
    cornerVariation = 0.2f,
    cornerJitter = 0.2f,
    tornChance = 0.03f,
    jerkChance = 0.04f,
)

private class AboveAnchor(private val gapPx: Int) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
        val x = (anchorBounds.center.x - popupContentSize.width / 2).coerceIn(0, maxX)
        val above = anchorBounds.top - popupContentSize.height - gapPx
        val y = if (above >= 0) above else anchorBounds.bottom + gapPx
        return IntOffset(x, y)
    }
}

/** Shows a short hint on long-press or hover over [content]; it never consumes the content's own gestures. */
@Composable
public fun FeldTooltip(
    text: String,
    modifier: Modifier = Modifier,
    seed: Int = 21,
    showMillis: Long = 2000L,
    content: @Composable () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val shape = remember(seed) { RoughShape(seed, TipRough) }
    val gapPx = with(LocalDensity.current) { 6.dp.roundToPx() }
    val position = remember(gapPx) { AboveAnchor(gapPx) }

    LaunchedEffect(hovered) { visible = hovered }
    LaunchedEffect(visible, hovered) {
        if (visible && !hovered) {
            delay(showMillis)
            visible = false
        }
    }

    Box(
        modifier
            .hoverable(interaction)
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                    val released = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                        waitForUpOrCancellation(PointerEventPass.Initial)
                        true
                    }
                    if (released == null) {
                        visible = true
                        waitForUpOrCancellation(PointerEventPass.Initial)
                    }
                }
            },
    ) {
        content()
        if (visible) {
            Popup(
                popupPositionProvider = position,
                properties = PopupProperties(focusable = false),
            ) {
                Box(
                    Modifier
                        .paperMaterial(shape, FeldPaper.Field)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = text.uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.2.sp),
                        color = FeldPalette.Cream,
                    )
                }
            }
        }
    }
}
