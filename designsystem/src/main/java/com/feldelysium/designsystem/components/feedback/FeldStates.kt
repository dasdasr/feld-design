package com.feldelysium.designsystem.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.designsystem.components.button.FeldButton
import com.feldelysium.designsystem.components.button.FeldButtonDefaults
import com.feldelysium.designsystem.components.content.FeldIcon
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.components.content.FlatDiamond
import com.feldelysium.designsystem.components.progress.FeldCircularProgressIndicator
import com.feldelysium.designsystem.theme.FeldPalette

/** A centered placeholder for empty lists and missing results, with an optional icon and [action]. */
@Composable
public fun FeldEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: FeldIconId? = null,
    action: (@Composable () -> Unit)? = null,
) {
    StateColumn(modifier) {
        if (icon != null) FeldIcon(id = icon, size = 48.dp)
        StateTitle(title)
        if (description != null) StateDescription(description)
        action?.invoke()
    }
}

/** A centered loading placeholder for a whole block or screen, with an optional [label]. */
@Composable
public fun FeldLoadingState(modifier: Modifier = Modifier, label: String? = null) {
    Box(modifier, contentAlignment = Alignment.Center) {
        StateColumn(Modifier) {
            FeldCircularProgressIndicator(size = 40.dp)
            if (label != null) {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.6.sp),
                    color = FeldPalette.Muted,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

/** A centered error placeholder with a message and an optional retry button. */
@Composable
public fun FeldErrorState(
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    onRetry: (() -> Unit)? = null,
    retryLabel: String = "Try again",
) {
    StateColumn(modifier) {
        FlatDiamond(FeldPalette.Orange, size = 20.dp)
        StateTitle(title)
        if (message != null) StateDescription(message)
        if (onRetry != null) {
            FeldButton(
                text = retryLabel,
                onClick = onRetry,
                spec = FeldButtonDefaults.OrangeFresh,
                minHeight = 48.dp,
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp),
            )
        }
    }
}

@Composable
private fun StateColumn(modifier: Modifier, content: @Composable () -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) { content() }
}

@Composable
private fun StateTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.titleMedium,
        color = FeldPalette.Cream,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun StateDescription(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = FeldPalette.Muted,
        textAlign = TextAlign.Center,
    )
}
