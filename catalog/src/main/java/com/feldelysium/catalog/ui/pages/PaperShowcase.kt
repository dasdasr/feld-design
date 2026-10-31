package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlChoice
import com.feldelysium.catalog.ui.ControlSlider
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.catalog.ui.fmt
import com.feldelysium.catalog.ui.kotlinCall
import com.feldelysium.designsystem.components.container.FeldPaperBox
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.foundation.shape.RoughStyle
import com.feldelysium.designsystem.theme.FeldPalette

private val PaperNames = listOf("Panel", "Field", "Note")
private val Papers = listOf(FeldPaper.Panel, FeldPaper.Field, FeldPaper.Note)

private val PaperSubjects = listOf(
    Subject("Surface") { seed ->
        var paper by remember { mutableIntStateOf(0) }
        var roughness by remember { mutableFloatStateOf(0.9f) }
        var corner by remember { mutableFloatStateOf(3f) }
        var torn by remember { mutableFloatStateOf(0.2f) }
        var textured by remember { mutableStateOf(true) }

        ShowcaseSpec(
            textured = true,
            preview = {
                FeldPaperBox(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    seed = seed,
                    spec = Papers[paper],
                    rough = RoughStyle(roughness = roughness.dp, step = 12.dp, corner = corner.dp, tornChance = torn),
                    textured = textured,
                    contentAlignment = Alignment.Center,
                ) {
                    Text(PaperNames[paper].uppercase(), style = CatalogMono, color = FeldPalette.Cream)
                }
            },
            controls = {
                ControlChoice("paper", PaperNames, paper) { paper = it }
                ControlSlider("roughness", roughness, 0f..3f, { roughness = it })
                ControlSlider("corner", corner, 0f..12f, { corner = it }) { "${it.toInt()} dp" }
                ControlSlider("torn edges", torn, 0f..0.6f, { torn = it }) { "%.2f".format(it) }
                ControlSwitch("texture", textured) { textured = it }
            },
            variants = {
                PaperNames.forEachIndexed { i, name ->
                    Specimen("FeldPaper.$name") {
                        FeldPaperBox(Modifier.fillMaxWidth().height(90.dp), seed = seed, spec = Papers[i])
                    }
                }
            },
            code = {
                kotlinCall(
                    "FeldPaperBox",
                    "modifier" to "Modifier.fillMaxWidth()",
                    "seed" to "rememberSeed()",
                    "spec" to "FeldPaper.${PaperNames[paper]}",
                    "rough" to "RoughStyle(roughness = ${roughness.fmt()}.dp, step = 12.dp, " +
                            "corner = ${corner.toInt()}.dp, tornChance = ${torn.fmt(2)})",
                    "textured" to "false".takeIf { !textured },
                )
            },
            notes = listOf(
                "The same seed always produces the same edge and grain.",
                "On API < 33 the AGSL paper falls back to a flat surface.",
            ),
        )
    },
)

@Composable
internal fun PaperShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Paper", "FOUNDATIONS", onBack, PaperSubjects)
