package com.feldelysium.catalog.ui.readme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.designsystem.components.container.FeldCard
import com.feldelysium.designsystem.components.content.FeldDistressedTitle
import com.feldelysium.designsystem.components.content.FeldDistressedTitles
import com.feldelysium.designsystem.foundation.decal.DecalSpec
import com.feldelysium.designsystem.foundation.decal.FeldDecalType
import com.feldelysium.designsystem.foundation.material.FeldMarker
import com.feldelysium.designsystem.foundation.material.FeldTape
import com.feldelysium.designsystem.foundation.material.markerFilmMaterial
import com.feldelysium.designsystem.foundation.material.stickerTapeMaterial
import com.feldelysium.designsystem.theme.Alegreya
import com.feldelysium.designsystem.theme.FeldPalette
import kotlin.random.Random

private fun DrawScope.wobbleLine(a: Offset, b: Offset, rng: Random, color: Color, width: Float, amp: Float) {
    val d = b - a
    val len = d.getDistance().coerceAtLeast(1f)
    val nx = -d.y / len
    val ny = d.x / len
    val n = (len / 26.dp.toPx()).toInt().coerceAtLeast(2)
    val start = a - d / len * (rng.nextFloat() * 1.5.dp.toPx())
    val end = b + d / len * (rng.nextFloat() * 1.5.dp.toPx())
    val path = Path().apply {
        moveTo(start.x, start.y)
        for (i in 1..n) {
            val t = i / n.toFloat()
            val p = start + (end - start) * t
            val j = (rng.nextFloat() - 0.5f) * 2f * amp
            lineTo(p.x + nx * j, p.y + ny * j)
        }
    }
    drawPath(path, color, style = Stroke(width, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun Modifier.roughEdges(
    seed: Int,
    bottom: Boolean = false,
    end: Boolean = false,
    alpha: Float = 0.5f,
    stroke: Dp = 1.dp,
): Modifier = drawBehind {
    val rng = Random(seed * 31 + 5)
    val c = FeldPalette.Cream.copy(alpha = alpha)
    val w = stroke.toPx()
    val amp = 0.4.dp.toPx()
    if (bottom) wobbleLine(Offset(0f, size.height), Offset(size.width, size.height), rng, c, w, amp)
    if (end) wobbleLine(Offset(size.width, 0f), Offset(size.width, size.height), rng, c, w, amp)
}

@Composable
fun Stamp(text: String, color: Color, seed: Int, u: Float, modifier: Modifier = Modifier, rotation: Float = -5f) {
    Box(
        modifier
            .graphicsLayer { rotationZ = rotation }
            .markerFilmMaterial(FeldMarker.Dry.copy(color = color, coverage = 0.85f), seed)
            .border((u * 0.12f).dp, Color.White)
            .padding(horizontal = (u * 1.1f).dp, vertical = (u * 0.4f).dp),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = (u * 1.7f).sp,
                lineHeight = (u * 1.8f).sp,
                letterSpacing = 1.sp,
                color = Color.White,
            ),
        )
    }
}

@Composable
private fun SpecCell(label: String, value: String, u: Float, modifier: Modifier = Modifier) {
    Column(
        modifier.padding(horizontal = (u * 1.3f).dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = label.uppercase(),
            style = FeldDecalType.Base.copy(fontSize = (u * 0.95f).sp, letterSpacing = 1.4.sp),
            color = FeldPalette.Muted,
            maxLines = 1,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = Alegreya,
                fontSize = (u * 1.9f).sp,
                lineHeight = (u * 2.3f).sp,
                letterSpacing = 0.sp,
            ),
            color = FeldPalette.Cream,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SpecColumn(
    items: List<Pair<String, String>>,
    seed: Int,
    u: Float,
    divider: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier.then(if (divider) Modifier.roughEdges(seed + 99, end = true, alpha = 0.35f) else Modifier)) {
        items.forEachIndexed { i, (k, v) ->
            SpecCell(
                label = k,
                value = v,
                u = u,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .roughEdges(seed + i, bottom = i < items.lastIndex, alpha = 0.3f),
            )
        }
    }
}

@Composable
fun TickRule(modifier: Modifier = Modifier, markAt: Float) {
    Canvas(modifier) {
        val step = 6.dp.toPx()
        val n = (size.width / step).toInt()
        val cy = size.height / 2f
        for (i in 0..n) {
            val major = i % 5 == 0
            val h = if (major) 10.dp.toPx() else 5.dp.toPx()
            val x = i * step
            drawLine(
                color = FeldPalette.Cream.copy(alpha = if (major) 0.45f else 0.22f),
                start = Offset(x, cy - h / 2f),
                end = Offset(x, cy + h / 2f),
                strokeWidth = 1.dp.toPx(),
            )
        }
        drawLine(
            color = FeldPalette.Teal,
            start = Offset(size.width * markAt, cy - 8.dp.toPx()),
            end = Offset(size.width * markAt, cy + 8.dp.toPx()),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
        )
    }
}

private fun Modifier.roughFrame(seed: Int, alpha: Float = 0.4f): Modifier = drawBehind {
    val rng = Random(seed * 31 + 5)
    val c = FeldPalette.Cream.copy(alpha = alpha)
    val w = 1.dp.toPx()
    val amp = 0.4.dp.toPx()
    val tl = Offset(0f, 0f)
    val tr = Offset(size.width, 0f)
    val br = Offset(size.width, size.height)
    val bl = Offset(0f, size.height)
    wobbleLine(tl, tr, rng, c, w, amp)
    wobbleLine(tr, br, rng, c, w, amp)
    wobbleLine(br, bl, rng, c, w, amp)
    wobbleLine(bl, tl, rng, c, w, amp)
}

@Composable
private fun TapeTag(text: String, seed: Int, u: Float, modifier: Modifier = Modifier) {
    val tape = FeldTape.Cream
    Box(
        modifier
            .graphicsLayer { rotationZ = -2f }
            .stickerTapeMaterial(tape, seed)
            .padding(horizontal = tape.edgeInset + (u * 1.2f).dp, vertical = (u * 0.7f).dp),
    ) {
        FeldDistressedTitle(
            text = text,
            seed = seed + 1,
            spec = FeldDistressedTitles.Small,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = (u * 1.9f).sp,
                lineHeight = (u * 2f).sp,
                letterSpacing = 1.sp,
            ),
        )
    }
}

@Composable
fun FeldSpecPlate(
    rows: List<Pair<String, String>>,
    seed: Int,
    modifier: Modifier = Modifier,
    title: String = "Feld\nElysium",
    eyebrow: String = "Technical specification",
    note: String = "Feld Design\n// UI system",
    tag: String = "Tech specs",
    figCaption: String = "Fig. 01",
    footerStart: String = "REV 1.0",
    footerEnd: String = "Nº 0001",
    art: @Composable (Dp) -> Unit = { PlaceholderMark() },
    onClick: (() -> Unit)? = null,
) {
    BoxWithConstraints(modifier.aspectRatio(2120f / 742f)) {
        val u = maxWidth.value * 0.01f
        val pad = maxWidth * 0.028f
        val artSize = (u * 10f).dp

        FeldCard(
            modifier = Modifier.fillMaxSize(),
            seed = seed,
            ambient = 0.6f,
            decalSpec = DecalSpec.Dialog,
            contentPadding = PaddingValues(0.dp),
            pressScale = 0f,
            onClick = onClick,
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = pad)
            ) {
                Spacer(Modifier.weight(0.06f))

                Row(
                    Modifier
                        .weight(0.14f)
                        .fillMaxWidth()
                        .roughEdges(seed + 1, bottom = true, alpha = 0.6f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        eyebrow.uppercase(),
                        style = FeldDecalType.Base.copy(fontSize = (u * 1.25f).sp, letterSpacing = 1.6.sp),
                        color = FeldPalette.Cream,
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        note.uppercase(),
                        style = FeldDecalType.Base.copy(
                            fontSize = (u * 0.75f).sp,
                            lineHeight = (u * 1.05f).sp,
                            letterSpacing = 1.2.sp,
                            textAlign = TextAlign.End,
                        ),
                        color = FeldPalette.Muted.copy(alpha = 0.55f),
                    )
                }

                Row(
                    Modifier
                        .weight(0.60f)
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .weight(0.26f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        FeldDistressedTitle(
                            text = title,
                            seed = seed + 2,
                            spec = FeldDistressedTitles.Large,
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                fontSize = (u * 6.4f).sp,
                                lineHeight = (u * 5.6f).sp,
                                letterSpacing = 1.sp,
                            ),
                        )
                        Spacer(Modifier.height((u * 0.4f).dp))
                        Stamp(
                            text = tag,
                            color = FeldPalette.Orange,
                            seed = seed + 3,
                            u = u,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    val half = (rows.size + 1) / 2
                    SpecColumn(
                        rows.take(half),
                        seed + 10,
                        u,
                        divider = true,
                        modifier = Modifier
                            .weight(0.28f)
                            .fillMaxHeight()
                    )
                    SpecColumn(
                        rows.drop(half),
                        seed + 20,
                        u,
                        divider = false,
                        modifier = Modifier
                            .weight(0.28f)
                            .fillMaxHeight()
                    )

                    Column(
                        Modifier
                            .weight(0.18f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            Modifier
                                .size(artSize * 1.25f)
                                .roughFrame(seed + 30),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                Modifier
                                    .size(artSize)
                                    .markerFilmMaterial(FeldMarker.Chalk, seed + 31),
                                contentAlignment = Alignment.Center,
                            ) { art(artSize) }
                        }
                        Spacer(Modifier.height((u * 0.6f).dp))
                        Text(
                            figCaption.uppercase(),
                            style = FeldDecalType.Base.copy(fontSize = (u * 0.75f).sp, letterSpacing = 1.6.sp),
                            color = FeldPalette.Orange,
                        )
                    }
                }

                Spacer(Modifier.weight(0.04f))

                Row(
                    Modifier
                        .weight(0.10f)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        footerStart.uppercase(),
                        style = FeldDecalType.Base.copy(fontSize = (u * 0.85f).sp, letterSpacing = 1.4.sp),
                        color = FeldPalette.Muted,
                    )
                    TickRule(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = (u * 1.5f).dp),
                        markAt = markerPosition(seed + 41),
                    )
                    Text(
                        footerEnd.uppercase(),
                        style = FeldDecalType.Base.copy(fontSize = (u * 0.85f).sp, letterSpacing = 1.4.sp),
                        color = FeldPalette.Orange,
                    )
                }
                Spacer(Modifier.weight(0.06f))
            }
        }
    }
}

@Composable
private fun PlaceholderMark() {
    Box(
        Modifier
            .fillMaxSize()
            .drawBehind {
                val p = Path().apply {
                    moveTo(size.width * 0.5f, 0f)
                    lineTo(size.width, size.height * 0.5f)
                    lineTo(size.width * 0.5f, size.height)
                    lineTo(0f, size.height * 0.5f)
                    close()
                }
                drawPath(p, Color.White, style = Stroke(1.6.dp.toPx(), join = StrokeJoin.Round))
            },
    )
}

fun markerPosition(seed: Int): Float {
    return Random(seed).nextFloat().coerceIn(0.05f, 0.95f)
}


