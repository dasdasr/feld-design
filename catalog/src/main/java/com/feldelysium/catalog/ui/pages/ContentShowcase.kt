package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlField
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.designsystem.components.content.FeldDiamond
import com.feldelysium.designsystem.components.content.FeldDistressedTitle
import com.feldelysium.designsystem.components.content.FeldDistressedTitles
import com.feldelysium.designsystem.components.content.FeldIcon
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.content.FeldTitleTag
import com.feldelysium.designsystem.components.input.FeldSlider
import com.feldelysium.designsystem.theme.FeldPalette


@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ContentShowcase(onBack: () -> Unit) {
    var size by rememberSaveable { mutableFloatStateOf(28f) }
    var distressed by rememberSaveable { mutableStateOf(true) }

    ShowcaseScreen(
        title = "Content",
        eyebrow = "COMPONENTS",
        onBack = onBack,
        preview = {
            FeldDistressedTitle(text = "Worn title", spec = FeldDistressedTitles.Large)
        },
        variants = {
            Specimen("FeldIcon · all ids") {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FeldIconId.entries.forEach { id ->
                        Column(
                            modifier = Modifier.width(72.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            FeldIcon(id = id, size = size.dp)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                id.name,
                                style = CatalogMono.copy(fontSize = 9.sp),
                                color = FeldPalette.Muted,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
            Specimen("FeldDistressedTitle · Small / Large") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FeldDistressedTitle(text = "Small", spec = FeldDistressedTitles.Small)
                    FeldDistressedTitle(text = "Large", spec = FeldDistressedTitles.Large)
                }
            }
            Specimen("FeldTitleTag") { FeldTitleTag(text = "Field note", distressed = distressed) }
            Specimen("FeldDiamond") {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf(FeldPalette.Orange, FeldPalette.Cream, FeldPalette.Muted).forEach { FeldDiamond(it, size = 24.dp) }
                }
            }
        },
        controls = {
            ControlField("icon size") {
                FeldSlider(
                    value = size,
                    onValueChange = { size = it },
                    valueRange = 16f..48f,
                    label = "Size",
                    valueText = { "${it.toInt()} dp" },
                )
            }
            ControlSwitch("distressed tag", distressed) { distressed = it }
        },
        usage = """
            FeldIcon(id = FeldIconId.Add, size = 28.dp)
            FeldTitleTag(text = "Field note")
        """.trimIndent(),
        notes = listOf("The icon set is a FeldIconId enum, so it is listed automatically."),
    )
}
