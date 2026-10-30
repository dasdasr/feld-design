package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.designsystem.components.button.FeldGhostButton
import com.feldelysium.designsystem.components.container.FeldCard
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.components.container.FeldNoteCard
import com.feldelysium.designsystem.components.container.FeldPaperBox
import com.feldelysium.designsystem.components.container.FeldSectionHeader
import com.feldelysium.designsystem.components.container.FeldToastHost
import com.feldelysium.designsystem.components.container.rememberFeldToastState
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.theme.FeldPalette


@Composable
internal fun ContainersShowcase(onBack: () -> Unit) {
    var textured by rememberSaveable { mutableStateOf(true) }
    var decals by rememberSaveable { mutableStateOf(true) }
    var taps by rememberSaveable { mutableIntStateOf(0) }
    val toast = rememberFeldToastState()

    ShowcaseScreen(
        title = "Containers",
        eyebrow = "COMPONENTS",
        onBack = onBack,
        preview = { seed ->
            FeldCard(
                modifier = Modifier.fillMaxWidth(),
                seed = seed,
                textured = textured,
                showDecals = decals,
                onClick = { taps += 1 },
            ) {
                Text("Card · tapped $taps", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            }
        },
        variants = { seed ->
            Specimen("FeldPaperBox") {
                FeldPaperBox(Modifier.fillMaxWidth().height(80.dp), seed = seed, spec = FeldPaper.Field)
            }
            Specimen("FeldNoteCard") {
                FeldNoteCard(title = "Field note", accent = FeldPalette.Orange, text = "A card with a coloured tab.", index = 1, seed = seed)
            }
            Specimen("FeldDivider") {
                Column {
                    FeldDivider()
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.height(40.dp)) { FeldDivider(vertical = true) }
                }
            }
            Specimen("FeldSectionHeader") { FeldSectionHeader(title = "Section") }
            Specimen("Toast · FeldToastHost") {
                Box(Modifier.fillMaxWidth().height(96.dp)) {
                    FeldGhostButton(
                        text = "Show toast",
                        onClick = { toast.show(text = "Scene saved", accent = FeldPalette.Orange) },
                    )
                    FeldToastHost(state = toast, modifier = Modifier.align(Alignment.BottomCenter))
                }
            }
        },
        controls = {
            ControlSwitch("textured", textured) { textured = it }
            ControlSwitch("decals", decals) { decals = it }
        },
        usage = """
            FeldCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { },
            ) {
                Text("Content")
            }
        """.trimIndent(),
        notes = listOf("Cards reveal on enter and press with a small scale."),
    )
}
