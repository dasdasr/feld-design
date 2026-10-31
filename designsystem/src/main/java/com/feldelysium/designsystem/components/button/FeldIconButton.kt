package com.feldelysium.designsystem.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feldelysium.designsystem.components.content.FeldIcon
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.input.feldClickable
import com.feldelysium.designsystem.foundation.material.FeldMarker
import com.feldelysium.designsystem.foundation.material.MarkerFilmSpec
import com.feldelysium.designsystem.foundation.sound.feldSounds
import com.feldelysium.designsystem.theme.FeldPalette
import com.feldelysium.designsystem.theme.FeldTheme

/** An icon button with a 40 dp touch target. */
@Composable
public fun FeldIconButton(
    id: FeldIconId,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 24.dp,
    enabled: Boolean = true,
    markerFilmSpec: MarkerFilmSpec = FeldMarker.Chalk,
    sound: Boolean = true,
) {
    val sounds = feldSounds

    Box(
        modifier
            .sizeIn(minWidth = 40.dp, minHeight = 40.dp)
            .alpha(if (enabled) 1f else 0.6f)
            .feldClickable(
                enabled = enabled,
                onClick = {
                    if (sound) sounds.click()
                    onClick()
                },
                role = Role.Button,
            ),
        contentAlignment = Alignment.Center,
    ) {
        FeldIcon(
            id,
            markerFilmSpec = markerFilmSpec,
            contentDescription = contentDescription,
            size = size,
        )
    }
}

@Preview(
    name = "FeldIconButton",
    widthDp = 360,
    heightDp = 220,
    showBackground = true,
    backgroundColor = 0xFF0B1011,
)
@Composable
private fun FeldIconButtonPreview() {
    FeldTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "DEFAULT",
                    color = FeldPalette.Cream,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    FeldIconButton(
                        id = FeldIconId.entries[0],
                        onClick = {},
                        contentDescription = "Default",
                    )
                    FeldIconButton(
                        id = FeldIconId.entries[1],
                        onClick = {},
                        contentDescription = "Second icon",
                        size = 28.dp,
                    )
                    FeldIconButton(
                        id = FeldIconId.entries[3],
                        onClick = {},
                        contentDescription = "Forth icon",
                        markerFilmSpec = FeldMarker.Chalk,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "DISABLED",
                    color = FeldPalette.Cream,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    FeldIconButton(
                        id = FeldIconId.entries[0],
                        onClick = {},
                        enabled = false,
                        contentDescription = "Disabled",
                    )
                    FeldIconButton(
                        id = FeldIconId.entries[1],
                        onClick = {},
                        enabled = false,
                        size = 28.dp,
                        contentDescription = "Disabled",
                    )
                }
            }
        }
    }
}
