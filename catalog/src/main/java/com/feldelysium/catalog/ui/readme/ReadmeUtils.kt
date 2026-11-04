package com.feldelysium.catalog.ui.readme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feldelysium.designsystem.R
import com.feldelysium.designsystem.foundation.decal.DecalSpec
import com.feldelysium.designsystem.theme.FeldPalette
import kotlin.random.Random


private val ManyDecals: DecalSpec = DecalSpec.Panel.let { base ->
    base.copy(
        maxDecals = 30,
        gap = 3.dp,
        alphaCap = base.alphaCap.mapValues { (_, v) -> (v * 1.8f).coerceAtMost(0.4f) },
        leakChance = 0.3f,
    )
}

private val SectionTitles = listOf(
    "See it" to FeldPalette.Orange,
    "What it does" to FeldPalette.Muted,
    "Spec sheet" to FeldPalette.Orange,
    "Under the hood" to FeldPalette.Orange,
)

private val ChakraPetch = FontFamily(Font(R.font.feld_chakra_petch_bold, FontWeight.Bold))
private val PlexMono = FontFamily(
    Font(R.font.feld_plex_mono_regular, FontWeight.Normal),
    Font(R.font.feld_plex_mono_semibold, FontWeight.SemiBold),
)
private val RobotoCond = FontFamily(
    Font(R.font.feld_roboto_condensed_regular, FontWeight.Normal),
    Font(R.font.feld_roboto_condensed_bold, FontWeight.Bold),
    Font(R.font.feld_roboto_condensed_black, FontWeight.Black),
)
private val AlegreyaF = FontFamily(Font(R.font.feld_alegreya_bold, FontWeight.Bold))

private class TechVariant(
    val text: String,
    val family: FontFamily,
    val weight: FontWeight,
    val size: Int,
    val tracking: Float,
    val color: Color,
)

private val TechVariants = listOf(
    TechVariant("DIALOGUE SANDBOX", ChakraPetch, FontWeight.Bold, 14, 4f, FeldPalette.Muted),
    TechVariant("DIALOGUE SANDBOX", PlexMono, FontWeight.Normal, 13, 3f, FeldPalette.Muted),
    TechVariant("DIALOGUE SANDBOX", PlexMono, FontWeight.SemiBold, 12, 6f, FeldPalette.Cream),
    TechVariant("DIALOGUE SANDBOX", RobotoCond, FontWeight.Black, 15, 5f, FeldPalette.Muted),
    TechVariant("DIALOGUE SANDBOX", RobotoCond, FontWeight.Normal, 14, 8f, FeldPalette.Orange),
    TechVariant("Dialogue Sandbox", AlegreyaF, FontWeight.Bold, 15, 1.5f, FeldPalette.Muted),
    TechVariant("dialogue sandbox", PlexMono, FontWeight.Normal, 12, 1f, FeldPalette.Muted),
)

@Composable
private fun TitleScreenshotScreen() {
    var seed by remember { mutableIntStateOf(1) }
    Box(
        Modifier
            .fillMaxSize()
            .padding(12.dp), contentAlignment = Alignment.Center
    ) {
        key(seed) {
            FeldBanner(seed = seed, modifier = Modifier.fillMaxHeight(), onClick = { seed = Random.nextInt() })
        }
    }
}
