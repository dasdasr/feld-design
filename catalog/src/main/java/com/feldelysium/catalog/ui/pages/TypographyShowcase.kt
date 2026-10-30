package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlField
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.components.input.FeldTextField
import com.feldelysium.designsystem.theme.FeldContinueStyle
import com.feldelysium.designsystem.theme.FeldPalette

@Composable
internal fun TypographyShowcase(onBack: () -> Unit) {
    val t = MaterialTheme.typography
    val styles = listOf(
        "titleLarge" to t.titleLarge,
        "titleMedium" to t.titleMedium,
        "labelLarge" to t.labelLarge,
        "labelMedium" to t.labelMedium,
        "bodyMedium" to t.bodyMedium,
        "bodySmall" to t.bodySmall,
        "labelSmall" to t.labelSmall,
        "FeldContinueStyle" to FeldContinueStyle,
    )
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var sample by rememberSaveable { mutableStateOf("Paper remembers every fold") }
    var upper by rememberSaveable { mutableStateOf(false) }
    val text = if (upper) sample.uppercase() else sample

    ShowcaseScreen(
        title = "Typography",
        eyebrow = "FOUNDATIONS",
        onBack = onBack,
        preview = {
            Text(text, style = styles[selected].second, color = FeldPalette.Cream, textAlign = TextAlign.Center)
        },
        variants = {
            styles.forEachIndexed { i, (name, style) ->
                val line = if (style.lineHeight.isSpecified) "${style.lineHeight.value.toInt()}" else "auto"
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable { selected = i }
                        .padding(vertical = 16.dp),
                ) {
                    Text(
                        "$name · ${style.fontSize.value.toInt()} / $line sp",
                        style = CatalogMono,
                        color = if (i == selected) FeldPalette.Orange else FeldPalette.Muted,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text, style = style, color = FeldPalette.Cream)
                }
                FeldDivider()
            }
        },
        controls = {
            ControlField("sample text") {
                FeldTextField(value = sample, onValueChange = { sample = it }, singleLine = true)
            }
            ControlSwitch("uppercase", upper) { upper = it }
        },
        usage = """
            Text(
                text = "Scene title",
                style = MaterialTheme.typography.titleLarge,
            )
        """.trimIndent(),
        notes = listOf("Tap a style in Variants to preview it on the stage."),
    )
}
