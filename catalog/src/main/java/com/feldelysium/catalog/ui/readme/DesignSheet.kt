package com.feldelysium.catalog.ui.readme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
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
import com.feldelysium.designsystem.components.content.FeldDiamond
import com.feldelysium.designsystem.components.content.FeldDistressedTitle
import com.feldelysium.designsystem.components.content.FeldDistressedTitles
import com.feldelysium.designsystem.components.content.FeldIcon
import com.feldelysium.designsystem.components.content.FeldIconId
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
import com.feldelysium.designsystem.foundation.material.markerFilmMaterial
import com.feldelysium.designsystem.foundation.material.stickerTapeMaterial
import com.feldelysium.designsystem.theme.Alegreya
import com.feldelysium.designsystem.theme.FeldContinueStyle
import com.feldelysium.designsystem.theme.FeldPalette
import kotlin.random.Random


private fun mono(size: Float, tracking: Float = 1.6f): TextStyle =
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

@Composable
private fun TapeLabel(text: String, seed: Int, modifier: Modifier = Modifier, rotation: Float = -2f) {
    val tape = FeldTape.Cream
    Box(
        modifier
            .graphicsLayer { rotationZ = rotation }
            .stickerTapeMaterial(tape, seed)
            .padding(horizontal = tape.edgeInset + 14.dp, vertical = 8.dp),
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

/** One numbered sheet. Tap the number row to re-roll every texture in the section. */
@Composable
private fun Section(
    index: Int,
    eyebrow: String,
    title: String,
    caption: String,
    baseSeed: Int,
    content: @Composable (seed: Int) -> Unit,
) {
    var seed by rememberSaveable { mutableIntStateOf(baseSeed) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 72.dp)
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
        Spacer(Modifier.height(18.dp))
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
    }
}


@OptIn(ExperimentalLayoutApi::class)
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
            .padding(horizontal = 20.dp)
            .padding(top = 28.dp, bottom = 96.dp),
    ) {
        key(heroSeed) {
            FeldBanner(
                seed = heroSeed,
                modifier = Modifier.fillMaxWidth(),
                title = "Feld\nDesign",
                subtitle = "Compose design system",
                onClick = { heroSeed = Random.nextInt(1, 10_000) },
            )
        }
        Spacer(Modifier.height(20.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            TapeLabel("Paper", heroSeed + 1, rotation = -2f)
            TapeLabel("Tape", heroSeed + 2, rotation = 1.5f)
            TapeLabel("Ink", heroSeed + 3, rotation = -1f)
            TapeLabel("AGSL", heroSeed + 4, rotation = 2.5f)
        }

        Section(1, "Buttons", "Press, tilt\nand tear", "Seven kinds. Every one re-rolls its own paper.", 21) { seed ->
            ButtonsSheet(seed)
        }
        Section(
            2,
            "Everything at once",
            "One card,\nfull kit",
            "Fields, chips, sliders and switches on a single sheet.",
            31
        ) { seed ->
            KitCard(seed)
        }
        Section(3, "Icons", "Chalk on\nfilm", "Vector, drawn with a marker. Tinted by the theme.", 41) { seed ->
            IconsSheet(seed)
        }
        Section(
            4,
            "Palette",
            "Signal,\npaper, ink",
            "Eight tokens. Swatches use the same marker material as the UI.",
            51
        ) { seed ->
            PaletteSheet(seed)
        }
        Section(5, "Typography", "Four faces,\none voice", "Alegreya for voice, mono for the paperwork.", 61) { seed ->
            TypeSheet(seed)
        }
        Section(
            6,
            "Materials",
            "Worn\npaper",
            "Same seed, same paper. Different seed, a different sheet.",
            71
        ) { seed ->
            PaperCollage(seed)
        }
        Section(7, "Notes", "Field\nnotes", "Cards with a coloured tab.", 81) { seed ->
            NotesSheet(seed)
        }
        Section(8, "Navigation", "Headers,\nbars, tools", "Built to stack.", 91) { seed ->
            NavSheet(seed)
        }
        Section(
            9,
            "Settings",
            "Rows that\nread like forms",
            "Switch, value and plain rows in one section.",
            101
        ) { seed ->
            SettingsSheet(seed)
        }
        Section(10, "Dialogs", "Ask once,\nask clearly", "Normal and destructive actions.", 111) { seed ->
            DialogSheet(seed)
        }

        FinalSpec()
    }
}

@Composable
private fun ButtonsSheet(seed: Int) {
    FeldCard(
        modifier = Modifier.fillMaxWidth(),
        seed = seed,
        decalSpec = DecalSpec.Panel,
        contentPadding = PaddingValues(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            FeldButton(
                text = "Save scene",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                seed = seed,
                spec = FeldButtonDefaults.Orange,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FeldButton("Cream", {}, Modifier.weight(1f), seed = seed + 1, spec = FeldButtonDefaults.Cream)
                FeldButton("Fresh", {}, Modifier.weight(1f), seed = seed + 2, spec = FeldButtonDefaults.OrangeFresh)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FeldFilmButton(text = "Export", onClick = {}, icon = FeldIconId.Export, seed = seed + 3)
                FeldGhostButton(text = "Cancel", onClick = {}, icon = FeldIconId.Close, seed = seed + 4)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    FeldIconId.Add, FeldIconId.Edit, FeldIconId.Delete,
                    FeldIconId.Settings, FeldIconId.Search, FeldIconId.Play,
                ).forEach { FeldIconButton(id = it, onClick = {}) }
            }
            FeldFieldButton(text = null, onClick = {}, placeholder = "Choose language")
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FeldFabVariant.entries.forEach {
                    FeldFab(onClick = {}, contentDescription = it.name, variant = it)
                }
                FeldTextButton(text = "Skip", onClick = {})
            }
        }
    }
}

@Composable
private fun KitCard(seed: Int) {
    var name by remember { mutableStateOf("") }
    var chip by remember { mutableIntStateOf(1) }
    var volume by remember { mutableStateOf(0.6f) }
    var tab by remember { mutableIntStateOf(1) }
    var sound by remember { mutableStateOf(true) }
    var auto by remember { mutableStateOf(false) }

    FeldCard(
        modifier = Modifier.fillMaxWidth(),
        seed = seed,
        decalSpec = DecalSpec.Dialog,
        contentPadding = PaddingValues(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FeldDiamond(FeldPalette.Orange, size = 14.dp)
                Spacer(Modifier.width(10.dp))
                Text("SCENE EDITOR · DRAFT 07", style = mono(10f), color = FeldPalette.Muted)
            }
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
                Spacer(Modifier.width(12.dp))
                Text("SOUND", style = mono(11f), color = FeldPalette.Cream, modifier = Modifier.weight(1f))
                Text("AUTOPLAY", style = mono(11f), color = FeldPalette.Cream)
                Spacer(Modifier.width(12.dp))
                FeldSwitch(checked = auto, onCheckedChange = { auto = it }, seed = seed + 1)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FeldGhostButton(text = "Cancel", onClick = {}, seed = seed + 2)
                FeldButton("Save", {}, Modifier.weight(1f), seed = seed + 3, spec = FeldButtonDefaults.Orange)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IconsSheet(seed: Int) {
    FeldCard(
        modifier = Modifier.fillMaxWidth(),
        seed = seed,
        decalSpec = DecalSpec.Panel,
        contentPadding = PaddingValues(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            FlowRow(
                maxItemsInEachRow = 6,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                FeldIconId.entries.forEach { id ->
                    Box(Modifier.size(52.dp), contentAlignment = Alignment.Center) {
                        FeldIcon(id = id, size = 28.dp)
                    }
                }
            }
            Text(
                "${FeldIconId.entries.size} CHALK FILM ICONS · feld_ic_*",
                style = mono(10f),
                color = FeldPalette.Muted,
            )
        }
    }
}

private val Swatches = listOf(
    "Orange" to FeldPalette.Orange, "Teal" to FeldPalette.Teal,
    "Cream" to FeldPalette.Cream, "Ink" to FeldPalette.Ink,
    "Muted" to FeldPalette.Muted, "Line" to FeldPalette.Line,
    "Success" to FeldPalette.Success, "Error" to FeldPalette.Error,
)

private fun Color.hex() = "#%06X".format(toArgb() and 0xFFFFFF)

@Composable
private fun PaletteSheet(seed: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Swatches.chunked(2).forEachIndexed { row, pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                pair.forEachIndexed { col, (label, color) ->
                    Column(Modifier.weight(1f)) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(88.dp)
                                .markerFilmMaterial(
                                    FeldMarker.Dry.copy(color = color, coverage = 0.95f),
                                    seed + row * 2 + col,
                                )
                                .border(1.dp, FeldPalette.Cream.copy(alpha = 0.22f)),
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(label.uppercase(), style = mono(11f), color = FeldPalette.Cream)
                        Text(color.hex(), style = mono(10f, 1f), color = FeldPalette.Muted)
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
        Triple("titleLarge", t.titleLarge, "Paper remembers every fold"),
        Triple("titleMedium", t.titleMedium, "Paper remembers every fold"),
        Triple("labelLarge", t.labelLarge, "Paper remembers every fold"),
        Triple("bodyMedium", t.bodyMedium, "Paper remembers every fold, and every stain."),
        Triple("bodySmall", t.bodySmall, "Paper remembers every fold, and every stain."),
        Triple("FeldContinueStyle", FeldContinueStyle, "Continue"),
    )
    FeldCard(
        modifier = Modifier.fillMaxWidth(),
        seed = seed,
        decalSpec = DecalSpec.Panel,
        contentPadding = PaddingValues(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "Aa",
                    style = t.titleLarge.copy(fontFamily = Alegreya, fontSize = 96.sp, lineHeight = 96.sp),
                    color = FeldPalette.Cream,
                )
                Spacer(Modifier.width(16.dp))
                Column(Modifier.padding(bottom = 14.dp)) {
                    Text("ALEGREYA BOLD", style = mono(11f), color = FeldPalette.Orange)
                    Text("CHAKRA · PLEX MONO · ROBOTO COND.", style = mono(9f), color = FeldPalette.Muted)
                }
            }
            rows.forEach { (name, style, sample) ->
                Column {
                    val size = if (style.fontSize.isSpecified) " · ${style.fontSize.value.toInt()} sp" else ""
                    Text("$name$size".uppercase(), style = mono(9f), color = FeldPalette.Muted)
                    Spacer(Modifier.height(4.dp))
                    Text(sample, style = style, color = FeldPalette.Cream)
                }
            }
        }
    }
}

@Composable
private fun PaperCollage(seed: Int) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        FeldPaperBox(
            modifier = Modifier
                .offset(0.dp, 10.dp)
                .size(230.dp, 160.dp)
                .graphicsLayer { rotationZ = -3f },
            seed = seed, spec = FeldPaper.Panel, contentAlignment = Alignment.Center,
        ) { Text("PANEL", style = mono(12f), color = FeldPalette.Cream) }

        FeldPaperBox(
            modifier = Modifier
                .offset(110.dp, 100.dp)
                .size(230.dp, 160.dp)
                .graphicsLayer { rotationZ = 2.5f },
            seed = seed + 1, spec = FeldPaper.Field, contentAlignment = Alignment.Center,
        ) { Text("FIELD", style = mono(12f), color = FeldPalette.Cream) }

        FeldPaperBox(
            modifier = Modifier
                .offset(30.dp, 220.dp)
                .size(240.dp, 150.dp)
                .graphicsLayer { rotationZ = -1.5f },
            seed = seed + 2, spec = FeldPaper.Note, contentAlignment = Alignment.Center,
        ) { Text("NOTE", style = mono(12f), color = FeldPalette.Cream) }

        TapeLabel("Panel", seed + 3, Modifier.offset(70.dp, 0.dp), rotation = -6f)
        TapeLabel("Field", seed + 4, Modifier.offset(190.dp, 90.dp), rotation = 4f)
        Stamp(
            text = "Approved",
            color = FeldPalette.Orange,
            seed = seed + 5,
            u = 8f,
            modifier = Modifier.offset(200.dp, 300.dp),
            rotation = -8f,
        )
    }
}

@Composable
private fun NotesSheet(seed: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
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
        contentPadding = PaddingValues(0.dp),
    ) {
        Column {
            FeldScreenHeader(
                title = "Scenes", eyebrow = "Library", subtitle = "12 entries",
                navIcon = FeldIconId.Back, onNav = {}, divider = true,
            )
            Box(Modifier.padding(vertical = 16.dp)) {
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
private fun SettingsSheet(seed: Int) {
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
            contentPadding = PaddingValues(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
            contentPadding = PaddingValues(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("DANGER", style = mono(10f), color = FeldPalette.Error)
                Text("Delete scene?", style = MaterialTheme.typography.titleLarge, color = FeldPalette.Cream)
                Text(
                    "This can't be undone.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = FeldPalette.Muted,
                )
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
        Row(Modifier.fillMaxWidth()) {
            listOf(
                "30+" to "COMPONENTS",
                "${FeldIconId.entries.size}" to "ICONS",
                "0" to "TEXTURE FILES"
            ).forEach { (n, l) ->
                Column(Modifier.weight(1f)) {
                    Text(
                        n,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = Alegreya, fontSize = 44.sp, lineHeight = 46.sp,
                        ),
                        color = FeldPalette.Orange,
                    )
                    Text(l, style = mono(9f), color = FeldPalette.Muted)
                }
            }
        }
        Spacer(Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Stamp("MIT licensed", FeldPalette.Orange, seed = 3, u = 8f, rotation = -4f)
            Spacer(Modifier.weight(1f))
            Text("github.com/dasdasr/feld-design", style = mono(10f, 1f), color = FeldPalette.Muted)
        }
    }
}
