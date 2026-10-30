package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlChoice
import com.feldelysium.catalog.ui.ControlField
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.Specimen
import com.feldelysium.designsystem.components.button.FeldButton
import com.feldelysium.designsystem.components.button.FeldButtonDefaults
import com.feldelysium.designsystem.components.button.FeldFab
import com.feldelysium.designsystem.components.button.FeldFabVariant
import com.feldelysium.designsystem.components.button.FeldFieldButton
import com.feldelysium.designsystem.components.button.FeldFilmButton
import com.feldelysium.designsystem.components.button.FeldGhostButton
import com.feldelysium.designsystem.components.button.FeldIconButton
import com.feldelysium.designsystem.components.button.FeldTextButton
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.input.FeldTextField
import com.feldelysium.designsystem.theme.FeldContinueStyle
import com.feldelysium.designsystem.theme.FeldPalette


@Composable
internal fun ButtonsShowcase(onBack: () -> Unit) {
    var enabled by rememberSaveable { mutableStateOf(true) }
    var label by rememberSaveable { mutableStateOf("Save scene") }
    var specIndex by rememberSaveable { mutableIntStateOf(0) }
    val specs = listOf(
        "Cream" to FeldButtonDefaults.Cream,
        "Orange" to FeldButtonDefaults.Orange,
        "Fresh" to FeldButtonDefaults.OrangeFresh,
    )

    ShowcaseScreen(
        title = "Buttons",
        eyebrow = "COMPONENTS",
        onBack = onBack,
        preview = { seed ->
            FeldButton(text = label, onClick = {}, seed = seed, enabled = enabled, spec = specs[specIndex].second)
        },
        variants = { seed ->
            Specimen("FeldButton") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    specs.forEach { (name, spec) ->
                        FeldButton(text = name, onClick = {}, seed = seed, spec = spec)
                    }
                }
            }
            Specimen("FeldTextButton") { FeldTextButton(text = "Text button", onClick = {}) }
            Specimen("FeldIconButton") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(FeldIconId.Add, FeldIconId.Edit, FeldIconId.Delete, FeldIconId.Settings).forEach {
                        FeldIconButton(id = it, onClick = {})
                    }
                }
            }
            Specimen("FeldGhostButton") {
                FeldGhostButton(text = "Ghost", onClick = {}, icon = FeldIconId.Close, seed = seed)
            }
            Specimen("FeldFilmButton") {
                FeldFilmButton(text = "Film", onClick = {}, icon = FeldIconId.Save, seed = seed)
            }
            Specimen("FeldFieldButton") {
                FeldFieldButton(text = null, onClick = {}, placeholder = "Choose language")
            }
            // TODO: показать варианты Film / Marker / Rec, когда уточним имя типа variant
            Specimen("FeldFab") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FeldFab(onClick = {}, contentDescription = "Add")
                    FeldFab(
                        variant = FeldFabVariant.Rec,
                        onClick = {},
                        contentDescription = "Add",
                    )
                    FeldFab(
                        variant = FeldFabVariant.Marker,
                        onClick = {},
                        contentDescription = "Add",
                    )
                }
            }
        },
        controls = {
            ControlSwitch("enabled", enabled) { enabled = it }
            ControlChoice("spec", specs.map { it.first }, specIndex) { specIndex = it }
            ControlField("label") {
                FeldTextField(value = label, onValueChange = { label = it }, singleLine = true)
            }
        },
        usage = """
            FeldButton(
                text = "Save",
                onClick = { },
                spec = FeldButtonDefaults.OrangeFresh,
            )
        """.trimIndent(),
        notes = listOf(
            "Text is uppercase; the paper texture is stable per seed.",
            "Disabled lowers alpha to 50% and blocks clicks.",
        ),
    )
}
