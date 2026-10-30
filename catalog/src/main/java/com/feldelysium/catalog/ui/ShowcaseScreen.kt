package com.feldelysium.catalog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.designsystem.components.button.FeldTextButton
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.input.FeldSegmentedControl
import com.feldelysium.designsystem.components.input.FeldSwitch
import com.feldelysium.designsystem.components.navigation.FeldScreenHeader
import com.feldelysium.designsystem.theme.FeldPalette

internal val CatalogMono = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontSize = 11.sp,
    letterSpacing = 1.4.sp,
)

private enum class ShowcaseTab(val label: String) {
    Variants("Variants"),
    Controls("Controls"),
    Usage("Usage"),
}

@Composable
internal fun ShowcaseScreen(
    title: String,
    eyebrow: String,
    onBack: () -> Unit,
    preview: @Composable ColumnScope.(seed: Int) -> Unit,
    variants: (@Composable ColumnScope.(seed: Int) -> Unit)? = null,
    controls: (@Composable ColumnScope.(seed: Int) -> Unit)? = null,
    usage: String? = null,
    notes: List<String> = emptyList(),
) {
    var seed by rememberSaveable { mutableIntStateOf(1) }
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    val tabs = buildList {
        if (variants != null) add(ShowcaseTab.Variants)
        if (controls != null) add(ShowcaseTab.Controls)
        if (usage != null || notes.isNotEmpty()) add(ShowcaseTab.Usage)
    }
    val tab = tabs.getOrNull(tabIndex) ?: tabs.firstOrNull()

    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        FeldScreenHeader(
            title = title,
            eyebrow = eyebrow,
            navIcon = FeldIconId.Back,
            onNav = onBack,
        )

        Stage(seed = seed, onReroll = { seed += 1 }, content = preview)

        if (tabs.size > 1) {
            Box(Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                FeldSegmentedControl(
                    options = tabs.map { it.label },
                    selectedIndex = tabs.indexOf(tab),
                    onSelect = { tabIndex = it },
                )
            }
        }

        key(tab) {
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 4.dp, bottom = 40.dp),
            ) {
                when (tab) {
                    ShowcaseTab.Variants -> variants?.invoke(this, seed)
                    ShowcaseTab.Controls -> controls?.invoke(this, seed)
                    ShowcaseTab.Usage -> UsageTab(usage, notes)
                    null -> Unit
                }
            }
        }
    }
}

@Composable
private fun Stage(
    seed: Int,
    onReroll: () -> Unit,
    content: @Composable ColumnScope.(seed: Int) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.04f)),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("PREVIEW", style = CatalogMono, color = FeldPalette.Muted, modifier = Modifier.weight(1f))
            FeldTextButton(text = "Reroll · $seed", onClick = onReroll)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 140.dp, max = 300.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            content(seed)
        }
        FeldDivider()
    }
}

@Composable
private fun UsageTab(usage: String?, notes: List<String>) {
    if (usage != null) {
        SectionLabel("Kotlin")
        CodeBlock(usage)
        Spacer(Modifier.height(28.dp))
    }
    if (notes.isNotEmpty()) {
        SectionLabel("Notes")
        notes.forEach {
            Text(
                "— $it",
                style = MaterialTheme.typography.bodyMedium,
                color = FeldPalette.Cream,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Composable
internal fun SectionLabel(text: String) {
    Text(text.uppercase(), style = CatalogMono, color = FeldPalette.Orange, modifier = Modifier.padding(top = 8.dp))
    FeldDivider(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
private fun CodeBlock(code: String) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.05f), shape)
            .border(1.dp, Color.White.copy(alpha = 0.14f), shape)
            .horizontalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            code,
            style = CatalogMono.copy(fontSize = 12.sp, lineHeight = 18.sp, letterSpacing = 0.sp),
            color = FeldPalette.Cream,
            softWrap = false,
        )
    }
}

@Composable
internal fun Specimen(name: String, content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Text(name, style = CatalogMono, color = FeldPalette.Muted)
        Spacer(Modifier.height(12.dp))
        content()
    }
    FeldDivider()
}

@Composable
internal fun ControlSwitch(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label.uppercase(), style = CatalogMono, color = FeldPalette.Cream, modifier = Modifier.weight(1f))
        FeldSwitch(checked = checked, onCheckedChange = onChange)
    }
    FeldDivider()
}

@Composable
internal fun ControlChoice(label: String, options: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit) {
    ControlField(label) {
        FeldSegmentedControl(options = options, selectedIndex = selectedIndex, onSelect = onSelect)
    }
}

@Composable
internal fun ControlField(label: String, content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
    ) {
        Text(label.uppercase(), style = CatalogMono, color = FeldPalette.Muted)
        Spacer(Modifier.height(8.dp))
        content()
    }
    FeldDivider()
}
