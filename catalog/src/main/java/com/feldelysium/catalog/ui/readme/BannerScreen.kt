package com.feldelysium.catalog.ui.readme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.designsystem.components.container.FeldCard
import com.feldelysium.designsystem.components.content.FeldDistressedTitle
import com.feldelysium.designsystem.components.content.FeldDistressedTitles
import com.feldelysium.designsystem.components.feedback.FeldBadge
import com.feldelysium.designsystem.foundation.decal.DecalSpec
import com.feldelysium.designsystem.foundation.decal.FeldDecalType
import com.feldelysium.designsystem.foundation.material.FeldMarker
import com.feldelysium.designsystem.foundation.material.FeldTape
import com.feldelysium.designsystem.foundation.material.markerFilmMaterial
import com.feldelysium.designsystem.theme.FeldPalette


@Composable
fun FeldBanner(
    seed: Int,
    modifier: Modifier = Modifier,
    title: String = "Feld\nElysium",
    subtitle: String = "Dialogue Sandbox",
    onClick: (() -> Unit)? = null,
) {
    BoxWithConstraints(modifier.aspectRatio(1660f / 600f)) {
        val u = maxWidth.value * 0.01f
        val pad = maxWidth * 0.035f
        val meta = FeldDecalType.Base.copy(fontSize = (u * 0.85f).sp, letterSpacing = 1.6.sp)
        val tape = FeldTape.Cream

        FeldCard(
            modifier = Modifier.fillMaxSize(),
            seed = seed,
            ambient = 0.8f,
            decalSpec = DecalSpec.Panel,
            contentPadding = PaddingValues(0.dp),
            pressScale = 0f,
            onClick = onClick,
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = pad,
                        vertical = pad,
                    )
            ) {
                Text(
                    text = "REC. 0042-C",
                    style = meta,
                    color = FeldPalette.Muted.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.TopStart),
                )

                Text(
                    text = "SCENE 01 · 16:9",
                    style = meta,
                    color = FeldPalette.Muted.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.TopEnd),
                )

                Column(
                    Modifier
                        .align(Alignment.CenterStart)
                        .graphicsLayer {
                            translationY = -12.dp.toPx()
                        }
                ) {
                    FeldDistressedTitle(
                        text = title,
                        seed = seed + 2,
                        spec = FeldDistressedTitles.Large,
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            fontSize = (u * 9f).sp,
                            lineHeight = (u * 8f).sp,
                            letterSpacing = 1.2.sp,
                        ),
                    )
                    FeldBadge(
                        text = subtitle,
                        accent = FeldPalette.Orange,
                        seed = seed,
                        unit = u * 1.1f,
                        rotation = -3f,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Box(
                    Modifier
                        .align(Alignment.CenterEnd)
                        .width((u * 12f).dp)
                        .height(4.dp)
                        .markerFilmMaterial(
                            FeldMarker.Chalk.copy(color = FeldPalette.Orange, coverage = 0.95f),
                            seed + 4
                        ),
                )

                Row(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height((u * 2.2f).dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TickRule(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = (u * 1.5f).dp),
                        markAt = markerPosition(seed + 6),
                    )
                    Text(
                        text = "FRAME ${seed.mod(900) + 100}",
                        style = meta,
                        color = FeldPalette.Muted.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}
