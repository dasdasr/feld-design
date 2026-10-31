package com.feldelysium.catalog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.feldelysium.designsystem.components.input.FeldChip
import com.feldelysium.designsystem.components.input.FeldSegmentedControl
import com.feldelysium.designsystem.components.input.FeldSwitch
import com.feldelysium.designsystem.components.navigation.FeldScreenHeader
import com.feldelysium.designsystem.theme.FeldPalette

internal val CatalogMono = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontSize = 11.sp,
    letterSpacing = 1.4.sp,
)

internal class ShowcaseSpec(
    val preview: @Composable () -> Unit,
    val controls: @Composable ColumnScope.() -> Unit,
    val code: () -> String,
    val variants: (@Composable ColumnScope.() -> Unit)? = null,
    val variantsLabel: String = "Variants",
    val notes: List<String> = emptyList(),
    val textured: Boolean = false,
)

/** A component showcased within a group page. */
internal class Subject(
    val name: String,
    val build: @Composable (seed: Int) -> ShowcaseSpec,
)

private enum class Tab { Controls, Variants, Code }

@Composable
internal fun ShowcaseScreen(
    title: String,
    eyebrow: String,
    onBack: () -> Unit,
    subjects: List<Subject>,
) {
    var subjectIndex by rememberSaveable { mutableIntStateOf(0) }
    var seed by rememberSaveable { mutableIntStateOf(1) }
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        FeldScreenHeader(title = title, eyebrow = eyebrow, navIcon = FeldIconId.Back, onNav = onBack)

        if (subjects.size > 1) {
            SubjectPicker(subjects.map { it.name }, subjectIndex) { subjectIndex = it }
        }

        key(subjectIndex) {
            val spec = subjects[subjectIndex].build(seed)
            val tabs = buildList {
                add(Tab.Controls)
                if (spec.variants != null) add(Tab.Variants)
                add(Tab.Code)
            }
            val tab = tabs[tabIndex.coerceIn(tabs.indices)]

            Stage(spec.textured, seed, onReroll = { seed += 1 }, content = spec.preview)

            Box(Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                FeldSegmentedControl(
                    options = tabs.map { if (it == Tab.Variants) spec.variantsLabel else it.name },
                    selectedIndex = tabs.indexOf(tab),
                    onSelect = { tabIndex = it },
                )
            }

            key(tab) {
                Column(
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 40.dp),
                ) {
                    when (tab) {
                        Tab.Controls -> spec.controls(this)
                        Tab.Variants -> spec.variants?.invoke(this)
                        Tab.Code -> CodeTab(spec.code, spec.notes)
                    }
                }
            }
        }
    }
}

@Composable
private fun SubjectPicker(names: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        names.forEachIndexed { i, name ->
            FeldChip(text = name, selected = i == selected, onClick = { onSelect(i) })
        }
    }
}

@Composable
private fun Stage(textured: Boolean, seed: Int, onReroll: () -> Unit, content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.04f)),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("LIVE PREVIEW", style = CatalogMono, color = FeldPalette.Orange, modifier = Modifier.weight(1f))
            if (textured) {
                Text(
                    "↻ NEW TEXTURE · $seed",
                    style = CatalogMono,
                    color = FeldPalette.Cream,
                    modifier = Modifier
                        .clickable(onClick = onReroll)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                )
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 260.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) { content() }
        FeldDivider()
    }
}

@Composable
private fun CodeTab(code: () -> String, notes: List<String>) {
    SectionLabel("Generated from the controls")
    CodeBlock(code())
    if (notes.isNotEmpty()) {
        Spacer(Modifier.height(28.dp))
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

internal fun kotlinCall(name: String, vararg args: Pair<String, String?>): String = buildString {
    append(name).append("(\n")
    args.forEach { (key, value) -> if (value != null) append("    $key = $value,\n") }
    append(")")
}

internal fun String.quoted(): String = "\"" + replace("\\", "\\\\").replace("\"", "\\\"") + "\""

internal fun Float.fmt(digits: Int = 1): String = "%.${digits}f".format(this) + "f"

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

