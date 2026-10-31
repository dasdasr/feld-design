package com.feldelysium.catalog.ui.pages

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.SectionLabel
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.designsystem.components.button.FeldTextButton
import com.feldelysium.designsystem.components.input.feldClickable
import com.feldelysium.designsystem.theme.FeldPalette
import kotlinx.coroutines.delay

private class ColorToken(
    val name: String,
    val role: String,
    val source: String,
    val color: Color,
)

private class ColorGroup(
    val title: String,
    val caption: String,
    val tokens: List<ColorToken>,
)

private fun Color.hex() = "#%06X".format(toArgb() and 0xFFFFFF)

/** WCAG 2.x contrast ratio. */
private fun contrast(a: Color, b: Color): Float {
    val l1 = a.luminance()
    val l2 = b.luminance()
    return (maxOf(l1, l2) + 0.05f) / (minOf(l1, l2) + 0.05f)
}

/** Ink or Cream, whichever reads better on this color. */
private fun Color.readableOn(): Color =
    if (contrast(this, FeldPalette.Ink) >= contrast(this, FeldPalette.Cream)) FeldPalette.Ink else FeldPalette.Cream

private fun copyHex(context: Context, hex: String) {
    val manager = context.getSystemService(ClipboardManager::class.java)
    manager.setPrimaryClip(ClipData.newPlainText("Feld color", hex))
}

@Composable
private fun rememberColorGroups(): List<ColorGroup> {
    val scheme = MaterialTheme.colorScheme
    return remember(scheme) {
        listOf(
            ColorGroup(
                title = "Foundation",
                caption = "Structural colors: backgrounds, text and lines.",
                tokens = listOf(
                    ColorToken("Background", "Screen", "MaterialTheme.colorScheme.background", scheme.background),
                    ColorToken("Surface", "Cards and sheets", "MaterialTheme.colorScheme.surface", scheme.surface),
                    ColorToken("Text", "Primary text", "MaterialTheme.colorScheme.onSurface", scheme.onSurface),
                    ColorToken("Muted", "Secondary text", "FeldPalette.Muted", FeldPalette.Muted),
                    ColorToken("Line", "Outlines, dividers", "FeldPalette.Line", FeldPalette.Line),
                ),
            ),
            ColorGroup(
                title = "Identity",
                caption = "The character of Feld: signal, paper and ink.",
                tokens = listOf(
                    ColorToken("Orange", "Primary · actions", "FeldPalette.Orange", FeldPalette.Orange),
                    ColorToken("Teal", "Accent · selection", "FeldPalette.Teal", FeldPalette.Teal),
                    ColorToken("Cream", "Paper · headings", "FeldPalette.Cream", FeldPalette.Cream),
                    ColorToken("Ink", "Text on paper", "FeldPalette.Ink", FeldPalette.Ink),
                ),
            ),
            ColorGroup(
                title = "Feedback",
                caption = "Functional states. Not part of the brand palette.",
                tokens = listOf(
                    ColorToken("Success", "Confirmation", "FeldPalette.Success", FeldPalette.Success),
                    ColorToken("Error", "Errors, destructive", "FeldPalette.Error", FeldPalette.Error),
                ),
            ),
        )
    }
}

private val ColorSubjects = listOf(
    Subject("Palette") { _ ->
        val groups = rememberColorGroups()
        val tokens = groups.flatMap { it.tokens }
        val starts = groups.runningFold(0) { acc, group -> acc + group.tokens.size }
        var selected by remember { mutableIntStateOf(5) } // Orange

        ShowcaseSpec(
            controlsLabel = "Palette",
            preview = { ColorPreview(tokens[selected]) },
            controls = {
                groups.forEachIndexed { g, group ->
                    SectionLabel("%02d — %s".format(g + 1, group.title))
                    Text(
                        group.caption,
                        style = MaterialTheme.typography.bodySmall,
                        color = FeldPalette.Muted,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    group.tokens.forEachIndexed { i, token ->
                        val flat = starts[g] + i
                        TokenRow(token, selected = flat == selected, onClick = { selected = flat })
                    }
                    Spacer(Modifier.height(24.dp))
                }
            },
            code = {
                val token = tokens[selected]
                "val color = ${token.source} // ${token.color.hex()}\n\n" +
                        "Text(\"Label\", color = color)\n" +
                        "Box(Modifier.background(color))"
            },
            notes = listOf(
                "Background, surface and text are theme roles and follow MaterialTheme.",
                "Every other color is a fixed token in FeldPalette.",
                "Contrast is measured against the theme background (WCAG 2.x).",
            ),
        )
    },
)

@Composable
private fun ColorPreview(token: ColorToken) {
    val background = MaterialTheme.colorScheme.background
    val context = LocalContext.current
    var copied by remember(token.name) { mutableStateOf(false) }
    LaunchedEffect(copied) {
        if (copied) {
            delay(1500)
            copied = false
        }
    }
    val shape = RoundedCornerShape(4.dp)

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(104.dp)
                .background(token.color, shape)
                .border(1.dp, Color.White.copy(alpha = 0.18f), shape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Aa",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
                color = token.color.readableOn(),
            )
        }
        Spacer(Modifier.width(20.dp))
        Column(Modifier.weight(1f)) {
            Text(token.name.uppercase(), style = CatalogMono, color = FeldPalette.Muted)
            Text(token.color.hex(), style = MaterialTheme.typography.titleLarge, color = FeldPalette.Cream)
            Spacer(Modifier.height(4.dp))
            Text(
                token.source,
                style = CatalogMono.copy(fontSize = 10.sp, letterSpacing = 0.sp),
                color = FeldPalette.Muted,
            )
            Text(
                "%.1f : 1 on background".format(contrast(token.color, background)),
                style = CatalogMono.copy(fontSize = 10.sp, letterSpacing = 0.sp),
                color = FeldPalette.Muted,
                modifier = Modifier.padding(top = 2.dp),
            )
            FeldTextButton(
                text = if (copied) "Copied" else "Copy hex",
                onClick = {
                    copyHex(context, token.color.hex())
                    copied = true
                },
            )
        }
    }
}

@Composable
private fun TokenRow(token: ColorToken, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(if (selected) Color.White.copy(alpha = 0.06f) else Color.Transparent)
            .feldClickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .width(3.dp)
                .height(36.dp)
                .background(if (selected) FeldPalette.Orange else Color.Transparent),
        )
        Spacer(Modifier.width(12.dp))
        Box(
            Modifier
                .size(36.dp)
                .background(token.color)
                .border(1.dp, Color.White.copy(alpha = 0.18f)),
        )
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(token.name, style = MaterialTheme.typography.bodyMedium, color = FeldPalette.Cream)
            Text(token.role, style = CatalogMono, color = FeldPalette.Muted)
        }
        Text(
            token.color.hex(),
            style = CatalogMono,
            color = if (selected) FeldPalette.Cream else FeldPalette.Muted,
        )
        Spacer(Modifier.width(8.dp))
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.08f)),
    )
}

@Composable
internal fun ColorsShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Colors", "FOUNDATIONS", onBack, ColorSubjects)
