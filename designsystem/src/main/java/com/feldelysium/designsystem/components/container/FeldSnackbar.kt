package com.feldelysium.designsystem.components.container

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.feldelysium.designsystem.components.button.FeldTextButton
import com.feldelysium.designsystem.components.content.FlatDiamond
import com.feldelysium.designsystem.foundation.seed.rememberSeed
import com.feldelysium.designsystem.foundation.shape.RoughStyle
import com.feldelysium.designsystem.theme.FeldPalette
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

private val SnackbarRough = RoughStyle(roughness = 1.dp, corner = 3.dp, tornChance = 0.15f)

/** How long a snackbar stays on screen. */
public enum class FeldSnackbarDuration(internal val millis: Long) {
    Short(4_000L),
    Long(10_000L),
    Indefinite(-1L),
}

/** How a snackbar ended. */
public enum class FeldSnackbarResult { Dismissed, ActionPerformed }

/** A snackbar currently shown by [FeldSnackbarHost]. */
@Stable
public class FeldSnackbarData internal constructor(
    public val id: Long,
    public val message: String,
    public val actionLabel: String?,
    public val accent: Color?,
    public val duration: FeldSnackbarDuration,
    private val continuation: CancellableContinuation<FeldSnackbarResult>,
) {
    public fun performAction() {
        if (continuation.isActive) continuation.resume(FeldSnackbarResult.ActionPerformed)
    }

    public fun dismiss() {
        if (continuation.isActive) continuation.resume(FeldSnackbarResult.Dismissed)
    }
}

/** Queues snackbars one at a time; [show] suspends until the snackbar ends. */
@Stable
public class FeldSnackbarState {
    private val mutex = Mutex()
    private var counter = 0L

    public var current: FeldSnackbarData? by mutableStateOf<FeldSnackbarData?>(null)
        private set

    public suspend fun show(
        message: String,
        actionLabel: String? = null,
        accent: Color? = null,
        duration: FeldSnackbarDuration =
            if (actionLabel == null) FeldSnackbarDuration.Short else FeldSnackbarDuration.Long,
    ): FeldSnackbarResult = mutex.withLock {
        try {
            suspendCancellableCoroutine { continuation ->
                current = FeldSnackbarData(++counter, message, actionLabel, accent, duration, continuation)
            }
        } finally {
            current = null
        }
    }
}

@Composable
public fun rememberFeldSnackbarState(): FeldSnackbarState = remember { FeldSnackbarState() }

/** A toast-styled message with an optional text action. */
@Composable
public fun FeldSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    accent: Color = FeldPalette.Orange,
    seed: Int = rememberSeed(),
) {
    FeldCard(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        seed = seed,
        rough = SnackbarRough,
        contentPadding = PaddingValues(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FlatDiamond(accent, size = 12.dp)
            Spacer(Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
            )
            if (actionLabel != null && onAction != null) {
                FeldTextButton(text = actionLabel, onClick = onAction)
            }
        }
    }
}

/** Shows the current snackbar of [state] with auto-dismiss; place it at the bottom of a Box. */
@Composable
public fun FeldSnackbarHost(state: FeldSnackbarState, modifier: Modifier = Modifier) {
    val data = state.current
    var last by remember { mutableStateOf<FeldSnackbarData?>(null) }

    LaunchedEffect(data) {
        data ?: return@LaunchedEffect
        last = data
        if (data.duration != FeldSnackbarDuration.Indefinite) {
            delay(data.duration.millis)
            data.dismiss()
        }
    }

    AnimatedVisibility(
        visible = data != null,
        modifier = modifier.padding(horizontal = 24.dp, vertical = 24.dp),
        enter = slideInVertically { it / 2 } + fadeIn(),
        exit = slideOutVertically { it / 2 } + fadeOut(),
    ) {
        (data ?: last)?.let {
            FeldSnackbar(
                message = it.message,
                actionLabel = it.actionLabel,
                onAction = it::performAction,
                accent = it.accent ?: FeldPalette.Orange,
                seed = rememberSeed(it.id),
            )
        }
    }
}
