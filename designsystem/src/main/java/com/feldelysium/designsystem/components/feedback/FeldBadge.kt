package com.feldelysium.designsystem.components.feedback

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.designsystem.foundation.material.FeldMarker
import com.feldelysium.designsystem.foundation.material.markerFilmMaterial
import com.feldelysium.designsystem.foundation.seed.rememberSeed
import com.feldelysium.designsystem.theme.FeldPalette

/**
 * A rubber-stamp style label for statuses and short text: dry marker fill, thin outline, uppercase text.
 *
 * [unit] scales everything at once (font, padding, outline), so the same badge works as a 10sp status tag
 * or as a big hero stamp. [rotation] is in degrees; pass 0f for a straight one.
 * On light accents (Cream) pass a dark [contentColor].
 */
@Composable
public fun FeldBadge(
    text: String,
    modifier: Modifier = Modifier,
    accent: Color = FeldPalette.Orange,
    contentColor: Color = Color.White,
    seed: Int = rememberSeed(),
    unit: Float = 6f,
    rotation: Float = -5f,
) {
    Box(
        modifier
            .graphicsLayer { rotationZ = rotation }
            .markerFilmMaterial(FeldMarker.Dry.copy(color = accent, coverage = 0.85f), seed)
            .border((unit * 0.12f).dp, contentColor)
            .padding(horizontal = (unit * 1.1f).dp, vertical = (unit * 0.4f).dp),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = (unit * 1.7f).sp,
                lineHeight = (unit * 1.8f).sp,
                letterSpacing = 1.sp,
                color = contentColor,
            ),
        )
    }
}
