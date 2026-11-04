package com.feldelysium.catalog.ui.readme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import com.feldelysium.catalog.ui.AccentColors
import com.feldelysium.catalog.ui.AccentNames
import com.feldelysium.designsystem.components.button.FeldButton
import com.feldelysium.designsystem.components.button.FeldButtonDefaults
import com.feldelysium.designsystem.components.button.FeldDialogActions
import com.feldelysium.designsystem.components.button.FeldFab
import com.feldelysium.designsystem.components.button.FeldFabVariant
import com.feldelysium.designsystem.components.button.FeldFieldButton
import com.feldelysium.designsystem.components.button.FeldFilmButton
import com.feldelysium.designsystem.components.button.FeldGhostButton
import com.feldelysium.designsystem.components.button.FeldIconButton
import com.feldelysium.designsystem.components.button.FeldTextButton
import com.feldelysium.designsystem.components.container.FeldCard
import com.feldelysium.designsystem.components.container.FeldNoteCard
import com.feldelysium.designsystem.components.container.FeldPaperBox
import com.feldelysium.designsystem.components.container.FeldSnackbar
import com.feldelysium.designsystem.components.content.FeldDiamond
import com.feldelysium.designsystem.components.content.FeldDistressedTitle
import com.feldelysium.designsystem.components.content.FeldDistressedTitles
import com.feldelysium.designsystem.components.content.FeldIcon
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.feedback.FeldBadge
import com.feldelysium.designsystem.components.feedback.FeldEmptyState
import com.feldelysium.designsystem.components.feedback.FeldErrorState
import com.feldelysium.designsystem.components.feedback.FeldLoadingState
import com.feldelysium.designsystem.components.feedback.FeldTooltip
import com.feldelysium.designsystem.components.input.FeldAccentColors
import com.feldelysium.designsystem.components.input.FeldChip
import com.feldelysium.designsystem.components.input.FeldSegmentedControl
import com.feldelysium.designsystem.components.input.FeldSlider
import com.feldelysium.designsystem.components.input.FeldSwitch
import com.feldelysium.designsystem.components.input.FeldTextField
import com.feldelysium.designsystem.components.input.FeldThumb
import com.feldelysium.designsystem.components.input.feldClickable
import com.feldelysium.designsystem.components.navigation.FeldActionBar
import com.feldelysium.designsystem.components.navigation.FeldScreenHeader
import com.feldelysium.designsystem.components.navigation.FeldToolbar
import com.feldelysium.designsystem.components.navigation.FeldToolbarItem
import com.feldelysium.designsystem.components.progress.FeldProgressIndicator
import com.feldelysium.designsystem.components.settings.FeldSettingRow
import com.feldelysium.designsystem.components.settings.FeldSettingSwitchRow
import com.feldelysium.designsystem.components.settings.FeldSettingValueRow
import com.feldelysium.designsystem.components.settings.FeldSettingsDivider
import com.feldelysium.designsystem.components.settings.FeldSettingsSection
import com.feldelysium.designsystem.foundation.decal.DecalSpec
import com.feldelysium.designsystem.foundation.decal.FeldDecalType
import com.feldelysium.designsystem.foundation.material.FeldMarker
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.foundation.material.FeldTape
import com.feldelysium.designsystem.foundation.material.accentBrush
import com.feldelysium.designsystem.foundation.material.markerFilmMaterial
import com.feldelysium.designsystem.foundation.material.stickerTapeMaterial
import com.feldelysium.designsystem.foundation.material.wornBrush
import com.feldelysium.designsystem.theme.Alegreya
import com.feldelysium.designsystem.theme.FeldContinueStyle
import com.feldelysium.designsystem.theme.FeldPalette
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

private object Sheet {
    val Gutter = 20.dp
    val SectionGap = 72.dp
    val Gap = 16.dp
    val Pad = 18.dp
}

internal fun mono(size: Float, tracking: Float = 1.6f): TextStyle =
    FeldDecalType.Base.copy(fontSize = size.sp, letterSpacing = tracking.sp)

private fun Modifier.blueprintGrid(): Modifier = drawBehind {
    val step = 24.dp.toPx()
    var i = 0
    var x = 0f
    while (x <= size.width) {
        val a = if (i % 5 == 0) 0.07f else 0.03f
        drawLine(FeldPalette.Cream.copy(alpha = a), Offset(x, 0f), Offset(x, size.height), 1f)
        x += step; i++
    }
    i = 0
    var y = 0f
    while (y <= size.height) {
        val a = if (i % 5 == 0) 0.07f else 0.03f
        drawLine(FeldPalette.Cream.copy(alpha = a), Offset(0f, y), Offset(size.width, y), 1f)
        y += step; i++
    }
}

/** Thin hairline on the right and/or bottom edge of a grid cell. */
private fun Modifier.cellLines(right: Boolean, bottom: Boolean): Modifier = drawBehind {
    val c = FeldPalette.Cream.copy(alpha = 0.10f)
    if (right) drawLine(c, Offset(size.width, 0f), Offset(size.width, size.height), 1f)
    if (bottom) drawLine(c, Offset(0f, size.height), Offset(size.width, size.height), 1f)
}

@Composable
internal fun Rule() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(FeldPalette.Cream.copy(alpha = 0.10f))
    )
}

@Composable
private fun TapeLabel(text: String, seed: Int, modifier: Modifier = Modifier, rotation: Float = -2f) {
    val tape = FeldTape.Cream
    Box(
        modifier
            .graphicsLayer { rotationZ = rotation }
            .stickerTapeMaterial(tape, seed)
            .padding(horizontal = tape.edgeInset + 8.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        FeldDistressedTitle(
            text = text,
            seed = seed + 1,
            spec = FeldDistressedTitles.Small,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 16.sp, lineHeight = 18.sp, letterSpacing = 1.sp,
            ),
        )
    }
}

/** The one frame every specimen sits in. */
@Composable
internal fun Board(
    seed: Int,
    figure: String,
    decal: DecalSpec = DecalSpec.Panel,
    content: @Composable ColumnScope.() -> Unit,
) {
    FeldCard(
        modifier = Modifier.fillMaxWidth(),
        seed = seed,
        decalSpec = decal,
        contentPadding = PaddingValues(Sheet.Pad),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Sheet.Gap)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FeldDiamond(FeldPalette.Orange, size = 10.dp)
                Spacer(Modifier.width(8.dp))
                Text(figure.uppercase(), style = mono(9f), color = FeldPalette.Muted)
            }
            Rule()
            content()
        }
    }
}

private class Spec(val label: String, val token: String?, val content: @Composable () -> Unit)

@Composable
private fun SpecList(specs: List<Spec>) {
    Column(verticalArrangement = Arrangement.spacedBy(Sheet.Gap)) {
        specs.forEachIndexed { i, s ->
            if (i > 0) Rule()
            Column(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        s.label.uppercase(), style = mono(9f), color = FeldPalette.Muted,
                        modifier = Modifier.weight(1f),
                    )
                    if (s.token != null) {
                        Text(s.token, style = mono(9f, 0.6f), color = FeldPalette.Orange.copy(alpha = 0.85f))
                    }
                }
                Spacer(Modifier.height(10.dp))
                s.content()
            }
        }
    }
}

/** One numbered sheet. Tap the header row to re-roll every texture in the section. */
@Composable
private fun Section(
    index: Int,
    eyebrow: String,
    title: String,
    caption: String,
    baseSeed: Int,
    meta: String,
    content: @Composable (seed: Int) -> Unit,
) {
    var seed by rememberSaveable { mutableIntStateOf(baseSeed) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = Sheet.SectionGap)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .feldClickable(onClick = { seed = Random.nextInt(1, 10_000) }),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("%02d".format(index), style = mono(12f), color = FeldPalette.Orange)
            Spacer(Modifier.width(10.dp))
            Text(eyebrow.uppercase(), style = mono(11f), color = FeldPalette.Cream)
            TickRule(
                Modifier
                    .weight(1f)
                    .height(16.dp)
                    .padding(horizontal = 12.dp),
                markAt = markerPosition(seed),
            )
            Text("FELD·DESIGN", style = mono(9f), color = FeldPalette.Muted)
        }
        Spacer(Modifier.height(20.dp))
        FeldDistressedTitle(
            text = title,
            seed = seed + 2,
            spec = FeldDistressedTitles.Large,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 40.sp, lineHeight = 42.sp, letterSpacing = 1.sp,
            ),
        )
        Text(
            caption,
            style = MaterialTheme.typography.bodyMedium,
            color = FeldPalette.Muted,
            modifier = Modifier.padding(top = 8.dp),
        )
        Spacer(Modifier.height(24.dp))
        content(seed)
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            Text(meta.uppercase(), style = mono(9f), color = FeldPalette.Muted, modifier = Modifier.weight(1f))
            Text("SEED $seed", style = mono(9f), color = FeldPalette.Muted.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun FeldDesignSheetScreen(onBack: () -> Unit = {}) {
    BackHandler(onBack = onBack)
    var heroSeed by rememberSaveable { mutableIntStateOf(11) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .blueprintGrid()
            .padding(horizontal = Sheet.Gutter)
            .padding(top = 28.dp, bottom = 96.dp),
    ) {
        key(heroSeed) { Hero(heroSeed) { heroSeed = Random.nextInt(1, 10_000) } }

        Section(
            1,
            "Buttons",
            "Buttons",
            "Seven button types. Tap the header to get a different paper.",
            21,
            "Fig. 01 · 7 components"
        ) {
            ButtonsSheet(it)
        }
        Section(
            2,
            "Example",
            "Scene editor",
            "A form built from the parts on this page: field, chips, slider, switches, buttons.",
            31,
            "Fig. 02 · example"
        ) {
            KitCard(it)
        }
        Section(
            3,
            "Feedback",
            "Feedback",
            "Badges, progress bars, snackbars, tooltips, and the empty, loading and error screens.",
            121,
            "Fig. 03 · 8 components"
        ) {
            FeedbackSheet(it)
        }
        Section(
            4,
            "Icons",
            "Icons",
            "Drawn by hand, stored as vectors. The theme sets the color.",
            41,
            "Fig. 04 · feld_ic_*"
        ) {
            IconsSheet(it)
        }
        Section(
            5,
            "Colors",
            "Colors",
            "Eight colors. Everything in the app uses only these.",
            51,
            "Fig. 05 · FeldPalette"
        ) {
            PaletteSheet(it)
        }
        Section(
            6,
            "Fonts",
            "Fonts",
            "Alegreya for titles and text, monospace for labels.",
            61,
            "Fig. 06 · FeldType"
        ) {
            TypeSheet(it)
        }
        Section(
            7,
            "Paper",
            "Paper",
            "Cards are drawn by a shader, no image files. Same seed, same sheet.",
            71,
            "Fig. 07 · FeldPaper"
        ) {
            PaperCollage(it)
        }
        Section(
            8,
            "Notes",
            "Note cards",
            "A card with a colored tab.",
            81,
            "Fig. 08 · FeldNoteCard"
        ) {
            NotesSheet(it)
        }
        Section(
            9,
            "Navigation",
            "Navigation",
            "Screen header, toolbar and bottom action bar.",
            91,
            "Fig. 09 · 3 components"
        ) {
            NavSheet(it)
        }
        Section(
            10,
            "Settings",
            "Settings",
            "Rows with a switch, a value or a link.",
            101,
            "Fig. 10 · 4 components"
        ) {
            SettingsSheet()
        }
        Section(
            11,
            "Dialogs",
            "Dialogs",
            "A normal dialog and a delete confirmation.",
            111,
            "Fig. 11 · FeldDialog"
        ) {
            DialogSheet(it)
        }

        FinalSpec()
    }
}

@Composable
private fun Hero(seed: Int, onReroll: () -> Unit) {
    FeldCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        seed = seed,
        ambient = 0.8f,
        decalSpec = DecalSpec.Panel,
        contentPadding = PaddingValues(0.dp),
        pressScale = 0f,
        onClick = onReroll,
    ) {
        val context = LocalContext.current

        val version = remember {
            context.packageManager
                .getPackageInfo(context.packageName, 0)
                .versionName ?: "UNKNOWN"
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(22.dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    "COMP. / 0042",
                    style = mono(10f),
                    color = FeldPalette.Muted.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f),
                )
                Text(
                    "V$version",
                    style = mono(10f),
                    color = FeldPalette.Muted.copy(alpha = 0.7f),
                )
            }
            Spacer(Modifier.weight(1f))
            FeldDistressedTitle(
                text = "Feld\nDesign",
                seed = seed + 2,
                spec = FeldDistressedTitles.Large,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 64.sp, lineHeight = 62.sp, letterSpacing = 1.2.sp,
                ),
            )
            Spacer(Modifier.height(8.dp))
            FeldBadge(
                text = "Compose design system",
                accent = FeldPalette.Orange,
                seed = seed,
                unit = 7f,
                rotation = -3f,
                modifier = Modifier.padding(start = 6.dp),
            )
            Spacer(Modifier.weight(1f))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(20.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                TickRule(
                    Modifier
                        .weight(1f)
                        .height(20.dp)
                        .padding(end = 12.dp), markAt = markerPosition(seed + 6)
                )
                Text(
                    "FRAME ${seed.mod(900) + 100}", style = mono(10f),
                    color = FeldPalette.Muted.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
private fun ButtonsSheet(seed: Int) {
    Board(seed, "Fig. 01 · Buttons") {
        SpecList(
            listOf(
                Spec("Primary", "Orange") {
                    FeldButton(
                        "Save scene", {}, Modifier.fillMaxWidth(),
                        seed = seed, spec = FeldButtonDefaults.Orange,
                    )
                },
                Spec("Secondary", "Cream · OrangeFresh") {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FeldButton("Cream", {}, Modifier.weight(1f), seed = seed + 1, spec = FeldButtonDefaults.Cream)
                        FeldButton(
                            "Fresh",
                            {},
                            Modifier.weight(1f),
                            seed = seed + 2,
                            spec = FeldButtonDefaults.OrangeFresh
                        )
                    }
                },
                Spec("Film · Ghost", "FeldFilmButton · FeldGhostButton") {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.weight(1f)) {
                            FeldFilmButton(text = "Export", onClick = {}, icon = FeldIconId.Export, seed = seed + 3)
                        }
                        Box(Modifier.weight(1f)) {
                            FeldGhostButton(text = "Cancel", onClick = {}, icon = FeldIconId.Close, seed = seed + 4)
                        }
                    }
                },
                Spec("Icon", "FeldIconButton") {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf(
                            FeldIconId.Add, FeldIconId.Edit, FeldIconId.Delete,
                            FeldIconId.Settings, FeldIconId.Search, FeldIconId.Play,
                        ).forEach { FeldIconButton(id = it, onClick = {}) }
                    }
                },
                Spec("Field", "FeldFieldButton") {
                    FeldFieldButton(text = null, onClick = {}, placeholder = "Choose language")
                },
                Spec("Floating", "FeldFab") {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        FeldFabVariant.entries.forEach {
                            FeldFab(onClick = {}, contentDescription = it.name, variant = it)
                        }
                    }
                },
                Spec("Text", "FeldTextButton") {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        FeldTextButton(text = "Skip", onClick = {})
                        FeldTextButton(text = "Cancel", onClick = {})
                        FeldTextButton(text = "Continue", onClick = {})
                    }
                },
            ),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun KitCard(seed: Int) {
    var name by remember { mutableStateOf("") }
    var chip by remember { mutableIntStateOf(1) }
    var volume by remember { mutableStateOf(0.6f) }
    var tab by remember { mutableIntStateOf(1) }
    var sound by remember { mutableStateOf(true) }
    var auto by remember { mutableStateOf(false) }

    Board(seed, "Fig. 02 · Scene editor · draft 07", decal = DecalSpec.Dialog) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            FeldTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                seed = seed,
                label = "Scene title",
                placeholder = "Type something",
                singleLine = true,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AccentNames.forEachIndexed { i, label ->
                    FeldChip(text = label, selected = i == chip, onClick = { chip = i }, accent = AccentColors[i])
                }
            }
            FeldSlider(
                value = volume,
                onValueChange = { volume = it },
                accent = AccentColors[0],
                thumb = FeldThumb.Diamond,
                label = "Volume",
                valueText = { "${(it * 100).toInt()}%" },
                thumbShadow = 0.7f,
                trackCutout = 2.5.dp,
                seed = seed,
                modifier = Modifier.fillMaxWidth(),
            )
            FeldSegmentedControl(
                options = listOf("Day", "Week", "Month"),
                selectedIndex = tab,
                onSelect = { tab = it },
                seed = seed,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                FeldSwitch(checked = sound, onCheckedChange = { sound = it }, seed = seed)
                Spacer(Modifier.width(10.dp))
                Text("SOUND", style = mono(11f), color = FeldPalette.Cream, modifier = Modifier.weight(1f))
                Text("AUTOPLAY", style = mono(11f), color = FeldPalette.Cream)
                Spacer(Modifier.width(10.dp))
                FeldSwitch(checked = auto, onCheckedChange = { auto = it }, seed = seed + 1)
            }
            Rule()
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) { FeldGhostButton(text = "Cancel", onClick = {}, seed = seed + 2) }
                FeldButton("Save", {}, Modifier.weight(1f), seed = seed + 3, spec = FeldButtonDefaults.Orange)
            }
        }
    }
}

@Composable
private fun FeedbackSheet(seed: Int) {
    Board(seed, "Fig. 03 · Feedback") {
        SpecList(
            listOf(
                Spec("Badge", "FeldBadge") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FeldBadge(text = "New", accent = FeldPalette.Orange)
                        FeldBadge(text = "Synced", accent = FeldPalette.Teal)
                        FeldBadge(text = "Draft", accent = FeldAccentColors.Cream)
                    }
                },
                Spec("Progress", "Determinate · indeterminate") {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FeldProgressIndicator(progress = { 0.62f }, modifier = Modifier.fillMaxWidth())
                        FeldProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            accent = FeldAccentColors.Teal,
                        )
                    }
                },
                Spec("Snackbar", "FeldSnackbar") {
                    FeldSnackbar(
                        message = "Scene deleted",
                        modifier = Modifier.fillMaxWidth(),
                        actionLabel = "Undo",
                        onAction = {},
                        seed = seed,
                    )
                },
                Spec("Tooltip", "Long-press") {
                    FeldTooltip(text = "Saves the current scene") {
                        FeldGhostButton(text = "Hold me", onClick = {}, seed = seed + 1)
                    }
                },
                Spec("Empty", "FeldEmptyState") {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .heightIn()
                    ) {
                        FeldEmptyState(
                            title = "Nothing here yet",
                            description = "Scenes you create will show up here.",
                            icon = FeldIconId.Search,
                        )
                    }
                },
                Spec("Loading", "FeldLoadingState") {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .heightIn()
                    ) { FeldLoadingState(label = "Loading scenes") }
                },
                Spec("Error", "FeldErrorState") {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .heightIn()
                    ) {
                        FeldErrorState(title = "Couldn't load", message = "Check the connection.", onRetry = {})
                    }
                },
            ),
        )
    }
}

@Composable
private fun IconsSheet(seed: Int) {
    val ids = FeldIconId.entries
    val rows = ids.chunked(5)
    Board(seed, "Fig. 03 · ${ids.size} icons") {
        Column(
            Modifier
                .fillMaxWidth()
                .border(1.dp, FeldPalette.Cream.copy(alpha = 0.10f))
        ) {
            rows.forEachIndexed { r, row ->
                Row(Modifier.fillMaxWidth()) {
                    repeat(5) { c ->
                        val id = row.getOrNull(c)
                        Column(
                            Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .cellLines(right = c < 4, bottom = r < rows.lastIndex),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            if (id != null) {
                                FeldIcon(id = id, size = 26.dp)
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    "%02d".format(r * 5 + c + 1),
                                    style = mono(8f, 1f),
                                    color = FeldPalette.Muted.copy(alpha = 0.7f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private class Swatch(val name: String, val role: String, val color: Color)

private val Swatches = listOf(
    Swatch("Orange", "Primary · actions", FeldPalette.Orange),
    Swatch("Teal", "Accent · selection", FeldPalette.Teal),
    Swatch("Cream", "Paper · headings", FeldPalette.Cream),
    Swatch("Ink", "Text on paper", FeldPalette.Ink),
    Swatch("Muted", "Secondary text", FeldPalette.Muted),
    Swatch("Line", "Outlines · dividers", FeldPalette.Line),
    Swatch("Success", "Confirmation", FeldPalette.Success),
    Swatch("Error", "Destructive", FeldPalette.Error),
)

private fun Color.hex() = "#%06X".format(toArgb() and 0xFFFFFF)

@Composable
private fun PaletteSheet(seed: Int) {
    Board(seed, "Fig. 04 · 8 tokens") {
        val density = LocalDensity.current.density
        var seedMod = 0
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Swatches.chunked(2).forEachIndexed { row, pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    pair.forEachIndexed { col, s ->
                        Column(Modifier.weight(1f)) {
                            val fill = remember(s.color, seed + seedMod, row, col, density) {
                                accentBrush(
                                    color = s.color,
                                    seed = seed + seedMod++,
                                    density = density,
                                    fade = 0.35f,
                                    glow = 0.2f,
                                    blotch = 0.025f,
                                    grain = 0.025f,
                                )
                            }
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)
                                    .background(fill)
                                    .border(1.dp, FeldPalette.Cream.copy(alpha = 0.30f)),
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(s.name.uppercase(), style = mono(11f), color = FeldPalette.Cream)
                            Text(s.color.hex(), style = mono(10f, 1f), color = FeldPalette.Orange.copy(alpha = 0.85f))
                            Text(s.role, style = mono(9f, 0.6f), color = FeldPalette.Muted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeSheet(seed: Int) {
    val t = MaterialTheme.typography
    val rows = listOf(
        Triple("titleLarge", t.titleLarge, "Paper remembers"),
        Triple("titleMedium", t.titleMedium, "Paper remembers every fold"),
        Triple("labelLarge", t.labelLarge, "Paper remembers every fold"),
        Triple("bodyMedium", t.bodyMedium, "Paper remembers every fold, and every stain."),
        Triple("bodySmall", t.bodySmall, "Paper remembers every fold, and every stain."),
        Triple("FeldContinueStyle", FeldContinueStyle, "Continue"),
    )
    Board(seed, "Fig. 05 · Type scale") {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                "Aa",
                style = t.titleLarge.copy(fontFamily = Alegreya, fontSize = 88.sp, lineHeight = 88.sp),
                color = FeldPalette.Cream,
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.padding(bottom = 12.dp)) {
                Text("ALEGREYA BOLD", style = mono(11f), color = FeldPalette.Orange)
                Text("CHAKRA · PLEX MONO", style = mono(9f), color = FeldPalette.Muted)
                Text("ROBOTO CONDENSED", style = mono(9f), color = FeldPalette.Muted)
            }
        }
        Rule()
        SpecList(
            rows.map { (name, style, sample) ->
                val size = if (style.fontSize.isSpecified) "${style.fontSize.value.toInt()} sp" else null
                Spec(name, size) { Text(sample, style = style, color = FeldPalette.Cream) }
            },
        )
    }
}

@Composable
private fun PaperCollage(seed: Int) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val w = maxWidth
        val pw = w * 0.68f
        val ph = w * 0.44f
        Box(
            Modifier
                .fillMaxWidth()
                .height(w * 1.3f)
        ) {
            FeldPaperBox(
                modifier = Modifier
                    .offset(0.dp, w * 0.03f)
                    .size(pw, ph)
                    .graphicsLayer { rotationZ = -2.5f },
                seed = seed, spec = FeldPaper.Panel, contentAlignment = Alignment.Center,
            ) { Text("PANEL", style = mono(12f), color = FeldPalette.Cream) }

            FeldPaperBox(
                modifier = Modifier
                    .offset(w * 0.30f, w * 0.40f)
                    .size(pw, ph)
                    .graphicsLayer { rotationZ = 2f },
                seed = seed + 1, spec = FeldPaper.Field, contentAlignment = Alignment.Center,
            ) { Text("FIELD", style = mono(12f), color = FeldPalette.Cream) }

            FeldPaperBox(
                modifier = Modifier
                    .offset(w * 0.06f, w * 0.78f)
                    .size(pw, ph)
                    .graphicsLayer { rotationZ = -1.5f },
                seed = seed + 2, spec = FeldPaper.Note, contentAlignment = Alignment.Center,
            ) { Text("NOTE", style = mono(12f), color = FeldPalette.Cream) }

            TapeLabel("Panel", seed + 3, Modifier.offset(w * 0.22f, 0.dp), rotation = -5f)
            TapeLabel("Field", seed + 4, Modifier.offset(w * 0.56f, w * 0.36f), rotation = 4f)
            FeldBadge(
                text = "Approved",
                accent = FeldPalette.Orange,
                seed = seed + 5,
                unit = 8f,
                modifier = Modifier.offset(w * 0.52f, w * 1.0f),
                rotation = -8f,
            )
        }
    }
}

@Composable
private fun NotesSheet(seed: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(Sheet.Gap)) {
        FeldNoteCard(
            title = "Orange",
            accent = FeldPalette.Orange,
            text = "Signal. Primary actions.",
            index = 1,
            seed = seed
        )
        FeldNoteCard(
            title = "Teal",
            accent = FeldPalette.Teal,
            text = "Selection and focus.",
            index = 2,
            seed = seed + 1
        )
        FeldNoteCard(
            title = "Cream",
            accent = FeldPalette.Cream,
            text = "Paper and headings.",
            index = 3,
            seed = seed + 2
        )
    }
}

@Composable
private fun NavSheet(seed: Int) {
    FeldCard(
        modifier = Modifier.fillMaxWidth(),
        seed = seed,
        decalSpec = DecalSpec.Panel,
        contentPadding = PaddingValues(6.dp),
    ) {
        Column {
            FeldScreenHeader(
                title = "Scenes", eyebrow = "Library", subtitle = "12 entries",
                navIcon = FeldIconId.ArrowBack, onNav = {}, divider = true,
            )
            Box(Modifier.padding(vertical = 20.dp)) {
                FeldToolbar(
                    items = listOf(
                        FeldToolbarItem(
                            icon = FeldIconId.Edit,
                            label = "Edit",
                            enabled = true,
                            accent = null,
                            onClick = {}),
                        FeldToolbarItem(
                            icon = FeldIconId.Add,
                            label = "Add",
                            enabled = true,
                            accent = FeldPalette.Orange,
                            onClick = {}),
                        FeldToolbarItem(
                            icon = FeldIconId.Delete,
                            label = "Delete",
                            enabled = true,
                            accent = null,
                            onClick = {}),
                    ),
                )
            }
            FeldActionBar(
                cancelText = "Cancel", onCancel = {},
                confirmText = "Apply", onConfirm = {},
                confirmEnabled = true, divider = true,
            )
        }
    }
}

@Composable
private fun SettingsSheet() {
    var sound by remember { mutableStateOf(true) }
    FeldSettingsSection(title = "Playback") {
        FeldSettingSwitchRow(
            icon = FeldIconId.Sound, title = "Sound", subtitle = "Page-turn effects",
            checked = sound, onCheckedChange = { sound = it },
        )
        FeldSettingsDivider()
        FeldSettingValueRow(icon = FeldIconId.Speed, title = "Speed", value = "2x", onClick = {})
        FeldSettingsDivider()
        FeldSettingRow(
            icon = FeldIconId.Settings, title = "Advanced", subtitle = "Open more options",
            showChevron = true, onClick = {},
        )
    }
}

@Composable
private fun DialogSheet(seed: Int) {
    var name by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        FeldCard(
            modifier = Modifier.fillMaxWidth(),
            seed = seed,
            decalSpec = DecalSpec.Dialog,
            contentPadding = PaddingValues(Sheet.Pad + 2.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Sheet.Gap)) {
                Text("SCENES", style = mono(10f), color = FeldPalette.Orange)
                Text("Rename scene", style = MaterialTheme.typography.titleLarge, color = FeldPalette.Cream)
                FeldTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    seed = seed,
                    placeholder = "Scene title",
                    singleLine = true,
                )
                FeldDialogActions(
                    confirmLabel = "Save", onConfirm = {}, cancelLabel = "Cancel", onCancel = {}, seed = seed,
                )
            }
        }
        FeldCard(
            modifier = Modifier.fillMaxWidth(),
            seed = seed + 7,
            decalSpec = DecalSpec.Dialog,
            contentPadding = PaddingValues(Sheet.Pad + 2.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Sheet.Gap)) {
                Text("DANGER", style = mono(10f), color = FeldPalette.Error)
                Text("Delete scene?", style = MaterialTheme.typography.titleLarge, color = FeldPalette.Cream)
                Text("This can't be undone.", style = MaterialTheme.typography.bodyMedium, color = FeldPalette.Muted)
                FeldDialogActions(
                    confirmLabel = "Delete", onConfirm = {}, cancelLabel = "Keep", onCancel = {},
                    destructive = true, seed = seed + 1,
                )
            }
        }
    }
}

@Composable
private fun FinalSpec() {
    val stats = listOf(
        "40+" to "Components",
        "${FeldIconId.entries.size}" to "Icons",
        "8" to "Materials",
        "24" to "Min SDK",
        "0" to "Texture files",
        "MIT" to "License",
    )
    val rows = stats.chunked(3)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 88.dp)
    ) {
        TickRule(
            Modifier
                .fillMaxWidth()
                .height(20.dp), markAt = markerPosition(7)
        )
        Spacer(Modifier.height(24.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .border(1.dp, FeldPalette.Cream.copy(alpha = 0.10f))
        ) {
            rows.forEachIndexed { r, row ->
                Row(Modifier.fillMaxWidth()) {
                    row.forEachIndexed { c, (n, label) ->
                        Column(
                            Modifier
                                .weight(1f)
                                .cellLines(right = c < row.lastIndex, bottom = r < rows.lastIndex)
                                .padding(16.dp),
                        ) {
                            Text(
                                n,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = Alegreya, fontSize = 36.sp, lineHeight = 38.sp,
                                ),
                                color = FeldPalette.Orange,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(label.uppercase(), style = mono(9f), color = FeldPalette.Muted)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(28.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            FeldBadge(
                "Open source",
                accent = FeldPalette.Orange,
                seed = 3,
                unit = 8f,
                rotation = -4f,
            )
            Spacer(Modifier.weight(1f))
            Text("github.com/dasdasr/feld-design", style = mono(10f, 1f), color = FeldPalette.Muted)
        }
    }
}
