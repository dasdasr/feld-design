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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.feldelysium.catalog.ui.CatalogMono
import com.feldelysium.catalog.ui.ControlChips
import com.feldelysium.catalog.ui.ControlSlider
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ControlText
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.catalog.ui.kotlinCall
import com.feldelysium.catalog.ui.quoted
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.theme.FeldContinueStyle
import com.feldelysium.designsystem.theme.FeldPalette

private fun TextStyle.scaled(f: Float) = copy(
    fontSize = if (fontSize.isSpecified) fontSize * f else fontSize,
    lineHeight = if (lineHeight.isSpecified) lineHeight * f else lineHeight,
)

private val TypeSubjects = listOf(
    Subject("Type scale") { _ ->
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
        var index by remember { mutableIntStateOf(0) }
        var text by remember { mutableStateOf("Paper remembers every fold") }
        var upper by remember { mutableStateOf(false) }
        var scale by remember { mutableFloatStateOf(1f) }
        fun shown() = if (upper) text.uppercase() else text

        ShowcaseSpec(
            preview = {
                Text(
                    shown(),
                    style = styles[index].second.scaled(scale),
                    color = FeldPalette.Cream,
                    textAlign = TextAlign.Center,
                )
            },
            controls = {
                ControlChips("style", styles.map { it.first }, index) { index = it }
                ControlText("sample text", text) { text = it }
                ControlSwitch("uppercase", upper) { upper = it }
                ControlSlider("scale", scale, 0.6f..1.6f, { scale = it }) { "×%.1f".format(it) }
            },
            variants = {
                styles.forEachIndexed { i, (name, style) ->
                    Column(Modifier.fillMaxWidth().clickable { index = i }.padding(vertical = 16.dp)) {
                        Text(
                            "$name · ${style.fontSize.value.toInt()} sp",
                            style = CatalogMono,
                            color = if (i == index) FeldPalette.Orange else FeldPalette.Muted,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(shown(), style = style, color = FeldPalette.Cream)
                    }
                    FeldDivider()
                }
            },
            variantsLabel = "Scale",
            code = {
                val (name, style) = styles[index]
                val source = if (name == "FeldContinueStyle") name else "MaterialTheme.typography.$name"
                val size = (style.fontSize.value * scale).toInt()
                kotlinCall(
                    "Text",
                    "text" to shown().quoted(),
                    "style" to if (scale == 1f) source else "$source.copy(fontSize = $size.sp)",
                )
            },
            notes = listOf("Tap a style in the scale list to put it on the stage."),
        )
    },
)

@Composable
internal fun TypographyShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Typography", "FOUNDATIONS", onBack, TypeSubjects)
