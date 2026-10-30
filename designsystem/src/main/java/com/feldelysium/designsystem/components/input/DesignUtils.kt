package com.feldelysium.designsystem.components.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

@Composable
public fun Modifier.feldClickable(
    onClick: () -> Unit,
    enabled: Boolean = true,
    role: Role = Role.Button,
    interactionSource: MutableInteractionSource? = null,
): Modifier {
    val source = interactionSource ?: remember { MutableInteractionSource() }

    return clickable(
        interactionSource = source,
        indication = null,
        enabled = enabled,
        role = role,
        onClick = onClick,
    )
}
