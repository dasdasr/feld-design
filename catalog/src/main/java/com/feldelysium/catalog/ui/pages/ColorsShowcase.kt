package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.designsystem.components.input.FeldColorSwatches
import com.feldelysium.designsystem.theme.FeldPalette


private val PaletteColors = listOf(
    "Cream" to FeldPalette.Cream,
    "Muted" to FeldPalette.Muted,
    "Orange" to FeldPalette.Orange,
    "Ink" to FeldPalette.Ink,
    "Line" to FeldPalette.Line,
    "Teal" to FeldPalette.Teal,
)

private fun Color.hex() = "#%06X".format(toArgb() and 0xFFFFFF)
private fun Color.argb() = toArgb().toLong() and 0xFFFFFFFFL

@Composable
internal fun ColorsShowcase(onBack: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val themeColors = listOf(
        "background" to scheme.background,
        "surface" to scheme.surface,
        "primary" to scheme.primary,
        "secondary" to scheme.secondary,
        "onSurface" to scheme.onSurface,
        "onSurfaceVariant" to scheme.onSurfaceVariant,
    )
    val all = PaletteColors + themeColors
    var selected by remember {
        mutableStateOf(all.first().second)
    }
    val current = all.firstOrNull { it.second == selected } ?: all.first()

    ShowcaseScreen(
        title = "Colors",
        eyebrow = "FOUNDATIONS",
        onBack = onBack,
        preview = {
            FeldColorSwatches(
                colors = all.map { it.second }.distinct(),
                selected = selected,
                onSelect = { selected = it },
                columns = 5,
            )
            Spacer(Modifier.height(12.dp))
            Text(current.first.uppercase(), style = CatalogMono, color = FeldPalette.Muted)
            Text(current.second.hex(), style = MaterialTheme.typography.titleMedium, color = FeldPalette.Cream)
        },
        variants = {
            Specimen("FeldPalette") { ColorList(PaletteColors) }
            Specimen("MaterialTheme.colorScheme") { ColorList(themeColors) }
        },
        usage = """
            val accent = FeldPalette.Orange
            val surface = MaterialTheme.colorScheme.surface
        """.trimIndent(),
        notes = listOf("Tap a swatch to inspect its hex value."),
    )
}

@Composable
private fun ColorList(colors: List<Pair<String, Color>>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        colors.forEach { (name, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(28.dp)
                        .background(color)
                        .border(1.dp, Color.White.copy(alpha = 0.2f)),
                )
                Spacer(Modifier.width(14.dp))
                Text(name, style = CatalogMono, color = FeldPalette.Cream, modifier = Modifier.weight(1f))
                Text(color.hex(), style = CatalogMono, color = FeldPalette.Muted)
            }
        }
    }
}
