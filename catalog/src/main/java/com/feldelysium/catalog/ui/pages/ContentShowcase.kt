package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.catalog.ui.AccentNames
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlChoice
import com.feldelysium.catalog.ui.ControlField
import com.feldelysium.catalog.ui.ControlSlider
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ControlText
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.catalog.ui.fmt
import com.feldelysium.catalog.ui.kotlinCall
import com.feldelysium.catalog.ui.quoted
import com.feldelysium.designsystem.components.content.FeldDiamond
import com.feldelysium.designsystem.components.content.FeldDistressedTitle
import com.feldelysium.designsystem.components.content.FeldDistressedTitles
import com.feldelysium.designsystem.components.content.FeldIcon
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.content.FeldTitleTag
import com.feldelysium.designsystem.components.input.FeldSlider
import com.feldelysium.designsystem.theme.FeldPalette
import kotlin.collections.copy
import kotlin.collections.get


@OptIn(ExperimentalLayoutApi::class)
private val ContentSubjects = listOf(
    Subject("Icon") { _ ->
        var icon by remember { mutableStateOf(FeldIconId.Add) }
        var size by remember { mutableFloatStateOf(36f) }
        var film by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = { FeldIcon(id = icon, size = size.dp, film = film) },
            controls = {
                ControlSlider("size", size, 16f..64f, { size = it }) { "${it.toInt()} dp" }
                ControlSwitch("chalk film", film) { film = it }
                Text("Pick an icon in the “All icons” tab.", style = CatalogMono, color = FeldPalette.Muted, modifier = Modifier.padding(top = 12.dp))
            },
            variants = {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FeldIconId.entries.forEach { id ->
                        Column(
                            Modifier.width(72.dp).clickable { icon = id },
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            FeldIcon(id = id, size = 28.dp)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                id.name,
                                style = CatalogMono.copy(fontSize = 9.sp),
                                color = if (id == icon) FeldPalette.Orange else FeldPalette.Muted,
                                maxLines = 1,
                            )
                        }
                    }
                }
            },
            variantsLabel = "All icons",
            code = {
                kotlinCall(
                    "FeldIcon",
                    "id" to "FeldIconId.${icon.name}",
                    "size" to "${size.toInt()}.dp",
                    "film" to "false".takeIf { !film },
                )
            },
        )
    },
    Subject("Distressed title") { _ ->
        var text by remember { mutableStateOf("Worn title") }
        var large by remember { mutableStateOf(true) }
        ShowcaseSpec(
            preview = {
                FeldDistressedTitle(
                    text = text,
                    spec = if (large) FeldDistressedTitles.Large else FeldDistressedTitles.Small,
                )
            },
            controls = {
                ControlText("text", text) { text = it }
                ControlSwitch("large (cream)", large) { large = it }
            },
            code = {
                kotlinCall(
                    "FeldDistressedTitle",
                    "text" to text.quoted(),
                    "spec" to "FeldDistressedTitles.${if (large) "Large" else "Small"}",
                )
            },
        )
    },
    Subject("Title tag") { _ ->
        var text by remember { mutableStateOf("Field note") }
        var distressed by remember { mutableStateOf(true) }
        var rotation by remember { mutableFloatStateOf(-2f) }
        ShowcaseSpec(
            preview = { FeldTitleTag(text = text, rotation = rotation, distressed = distressed) },
            controls = {
                ControlText("text", text) { text = it }
                ControlSwitch("distressed text", distressed) { distressed = it }
                ControlSlider("rotation", rotation, -8f..8f, { rotation = it }) { "%.0f°".format(it) }
            },
            code = {
                kotlinCall(
                    "FeldTitleTag",
                    "text" to text.quoted(),
                    "rotation" to rotation.fmt(0),
                    "distressed" to "false".takeIf { !distressed },
                )
            },
        )
    },
    Subject("Diamond") { _ ->
        var accent by remember { mutableIntStateOf(2) }
        var size by remember { mutableFloatStateOf(32f) }
        val colors = listOf(FeldPalette.Cream, FeldPalette.Teal, FeldPalette.Orange)
        ShowcaseSpec(
            preview = { FeldDiamond(colors[accent], size = size.dp) },
            controls = {
                ControlChoice("color", AccentNames, accent) { accent = it }
                ControlSlider("size", size, 12f..64f, { size = it }) { "${it.toInt()} dp" }
            },
            code = {
                kotlinCall(
                    "FeldDiamond",
                    "color" to "FeldPalette.${AccentNames[accent]}",
                    "size" to "${size.toInt()}.dp",
                )
            },
        )
    },
)

@Composable
internal fun ContentShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Content", "COMPONENTS", onBack, ContentSubjects)
