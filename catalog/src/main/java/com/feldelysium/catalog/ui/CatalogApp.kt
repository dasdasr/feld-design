package com.feldelysium.catalog.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.feldelysium.catalog.ui.feedback.FeedbackHub
import com.feldelysium.catalog.ui.feedback.LocalFeedbackHub
import com.feldelysium.designsystem.components.container.FeldCard
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.components.container.FeldSectionHeader
import com.feldelysium.designsystem.components.content.FeldDiamond
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.foundation.sound.LocalFeldSounds
import com.feldelysium.designsystem.theme.FeldPalette

private const val HOME = "home"

@Composable
internal fun CatalogApp() {
    val context = LocalContext.current
    val hub = remember { FeedbackHub(context.applicationContext) }
    DisposableEffect(hub) { onDispose { hub.release() } }

    CompositionLocalProvider(
        LocalFeedbackHub provides hub,
        LocalFeldSounds provides hub.sounds,
    ) {
        var route by rememberSaveable { mutableStateOf(HOME) }
        val homeScroll = rememberScrollState()
        val entry = ShowcaseRegistry.firstOrNull { it.id == route }

        BackHandler(enabled = entry != null) { route = HOME }

        if (entry == null) {
            CatalogHome(scroll = homeScroll, onOpen = { route = it })
        } else {
            entry.content { route = HOME }
        }
    }
}

@Composable
internal fun CatalogHome(scroll: ScrollState, onOpen: (String) -> Unit) {
    val groups = remember { ShowcaseRegistry.groupBy { it.group } }
    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(scroll)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(32.dp))
        Text("FELD / DESIGN SYSTEM", style = CatalogMono, color = FeldPalette.Orange)
        Spacer(Modifier.height(12.dp))
        Text(
            "A field guide to the interface",
            style = MaterialTheme.typography.titleMedium,
            color = FeldPalette.Cream,
        )
        Spacer(Modifier.height(8.dp))
        Text("${ShowcaseRegistry.size} PAGES · JETPACK COMPOSE", style = CatalogMono, color = FeldPalette.Muted)
        Spacer(Modifier.height(32.dp))

        groups.forEach { (group, entries) ->
            FeldSectionHeader(title = group.title)
            entries.forEach { entry ->
                FeldCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    onClick = { onOpen(entry.id) },
                    contentPadding = PaddingValues(16.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FeldDiamond(FeldPalette.Orange, size = 20.dp)
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                entry.title.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(entry.summary, style = CatalogMono, color = FeldPalette.Muted)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        Spacer(Modifier.height(32.dp))
    }
}
