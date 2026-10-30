package com.feldelysium.designsystem.components.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.designsystem.components.button.FeldTextButton
import com.feldelysium.designsystem.components.content.FeldTitleTag
import com.feldelysium.designsystem.components.input.feldClickable
import com.feldelysium.designsystem.foundation.seed.rememberSeed
import com.feldelysium.designsystem.theme.FeldTheme

private val HeaderTagStyle: TextStyle
    @Composable get() = MaterialTheme.typography.labelLarge.copy(
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.6.sp,
    )

/** A section title on a tape tag sitting over a divider. */
@Composable
public fun FeldSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    seed: Int = rememberSeed(),
    tagDrop: Dp = 10.dp,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.feldClickable(onClick = onClick) else Modifier),
    ) {
        FeldDivider(
            modifier = Modifier.align(Alignment.BottomStart),
            seed = seed + 31,
        )
        FeldTitleTag(
            text = title,
            modifier = Modifier.offset(y = tagDrop),
            seed = seed,
            distressed = true,
            textStyle = HeaderTagStyle,
        )
        if (trailing != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(y = tagDrop / 2)
                    .padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = trailing,
            )
        }
    }
}

@Preview(
    name = "FeldSectionHeader",
    showBackground = true,
    backgroundColor = 0xFF111819,
    widthDp = 360,
)
@Composable
private fun FeldSectionHeaderPreview() {
    FeldTheme {
        Column(
            modifier = Modifier.padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(36.dp),
        ) {
            FeldSectionHeader(
                title = "SCENES",
                seed = 42,
            )

            FeldSectionHeader(
                title = "SPEAKERS",
                seed = 17,
                trailing = {
                    FeldTextButton(
                        text = "ADD NEW",
                        onClick = {},
                    )
                },
            )

            FeldSectionHeader(
                title = "ARCHIVE",
                seed = 73,
                onClick = {},
            )
        }
    }
}
