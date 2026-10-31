package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlField
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.designsystem.components.container.FeldPaperBox
import com.feldelysium.designsystem.components.input.FeldColorSwatches
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.foundation.material.PaperSpec
import com.feldelysium.designsystem.theme.FeldPalette


private val Palette = listOf(
    "Cream" to FeldPalette.Cream,
    "Muted" to FeldPalette.Muted,
    "Orange" to FeldPalette.Orange,
    "Ink" to FeldPalette.Ink,
    "Line" to FeldPalette.Line,
    "Teal" to FeldPalette.Teal,
)

private fun Color.hex() = "#%06X".format(toArgb() and 0xFFFFFF)

private val ColorSubjects = listOf(
    Subject("Palette") { seed ->
        val scheme = MaterialTheme.colorScheme
        val roles = listOf(
            "background" to scheme.background,
            "surface" to scheme.surface,
            "primary" to scheme.primary,
            "secondary" to scheme.secondary,
            "onSurface" to scheme.onSurface,
            "onSurfaceVariant" to scheme.onSurfaceVariant,
        )
        val all = Palette + roles
        var selected by remember { mutableIntStateOf(2) }

        ShowcaseSpec(
            textured = true,
            preview = {
                val (name, color) = all[selected]
                FeldPaperBox(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    seed = seed,
                    spec = swatchSpec(color),
                    contentAlignment = Alignment.BottomStart,
                ) {
                    val ink = if (color.luminance() > 0.5f) Color.Black else Color.White
                    Column(Modifier.padding(16.dp)) {
                        Text(name.uppercase(), style = CatalogMono, color = ink.copy(alpha = 0.7f))
                        Text(color.hex(), style = MaterialTheme.typography.titleLarge, color = ink)
                    }
                }
            },
            controls = {
                ControlField("pick a color") {
                    FeldColorSwatches(
                        colors = all.map { it.second },
                        selected = all[selected].second,
                        onSelect = { color ->
                            selected = all.indexOfFirst { it.second == color }.coerceAtLeast(0)
                        },
                        columns = 5,
                    )
                }
            },
            variants = {
                Specimen("FeldPalette") { TokenList(Palette) }
                Specimen("MaterialTheme.colorScheme") { TokenList(roles) }
            },
            variantsLabel = "Tokens",
            code = {
                val (name, color) = all[selected]
                val source = if (selected < Palette.size) "FeldPalette.$name" else "MaterialTheme.colorScheme.$name"
                "val color = $source // ${color.hex()}"
            },
            notes = listOf("Swatches are drawn with the same paper material as the components."),
        )
    },
)

@Composable
private fun TokenList(colors: List<Pair<String, Color>>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        colors.forEach { (name, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(28.dp).background(color).border(1.dp, Color.White.copy(alpha = 0.2f)))
                Spacer(Modifier.width(14.dp))
                Text(name, style = CatalogMono, color = FeldPalette.Cream, modifier = Modifier.weight(1f))
                Text(color.hex(), style = CatalogMono, color = FeldPalette.Muted)
            }
        }
    }
}

@Composable
internal fun ColorsShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Colors", "FOUNDATIONS", onBack, ColorSubjects)

private fun swatchSpec(c: Color): PaperSpec {
    return FeldPaper.DryButtonCream.copy(
        base = c,
        scuff = lerp(c, Color.Black, 0.35f),
        ink = FeldPalette.Cream,
        inkWidth = 0.dp,
        wear = 0.4f,
        edgeWear = 0.2f,
        innerWear = 0.3f,
        scuffTint = 0.35f,
        chipSize = 5.dp,
        chipCoverage = 0.5f,
        mottleStrength = 0.3f,
        grainStrength = 0.03f,
        toothStrength = 0.03f,
        speckleStrength = 0.03f,
    )
}
