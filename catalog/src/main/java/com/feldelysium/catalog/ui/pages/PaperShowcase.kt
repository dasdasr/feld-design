package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlChoice
import com.feldelysium.catalog.ui.ControlField
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.designsystem.components.container.FeldPaperBox
import com.feldelysium.designsystem.components.input.FeldSlider
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.foundation.shape.RoughStyle

@Composable
internal fun PaperShowcase(onBack: () -> Unit) {
    val papers = listOf("Panel" to FeldPaper.Panel, "Field" to FeldPaper.Field, "Note" to FeldPaper.Note)
    var index by rememberSaveable { mutableIntStateOf(0) }
    var roughness by rememberSaveable { mutableFloatStateOf(0.9f) }
    var torn by rememberSaveable { mutableFloatStateOf(0.2f) }
    val rough = RoughStyle(roughness = roughness.dp, step = 12.dp, corner = 3.dp, tornChance = torn)

    ShowcaseScreen(
        title = "Paper",
        eyebrow = "FOUNDATIONS",
        onBack = onBack,
        preview = { seed ->
            FeldPaperBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                seed = seed,
                spec = papers[index].second,
                rough = rough,
                contentAlignment = Alignment.Center,
            ) {
                Text(papers[index].first.uppercase(), style = CatalogMono, color = Color.Black.copy(alpha = 0.6f))
            }
        },
        variants = { seed ->
            papers.forEach { (name, spec) ->
                Specimen("FeldPaper.$name") {
                    FeldPaperBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp),
                        seed = seed,
                        spec = spec,
                    )
                }
            }
        },
        controls = {
            ControlChoice("paper", papers.map { it.first }, index) { index = it }
            ControlField("roughness") {
                FeldSlider(
                    value = roughness,
                    onValueChange = { roughness = it },
                    valueRange = 0f..3f,
                    label = "Roughness",
                    valueText = { "%.1f".format(it) },
                )
            }
            ControlField("torn edges") {
                FeldSlider(
                    value = torn,
                    onValueChange = { torn = it },
                    valueRange = 0f..0.6f,
                    label = "Chance",
                    valueText = { "%.2f".format(it) },
                )
            }
        },
        usage = """
            FeldPaperBox(
                modifier = Modifier.fillMaxWidth(),
                seed = rememberSeed(),
                spec = FeldPaper.Panel,
                rough = RoughStyle(roughness = 0.9.dp, step = 12.dp, corner = 3.dp, tornChance = 0.2f),
            )
        """.trimIndent(),
        notes = listOf(
            "The same seed always gives the same edge and texture.",
            "On API < 33 the AGSL paper falls back to a flat surface.",
        ),
    )
}
