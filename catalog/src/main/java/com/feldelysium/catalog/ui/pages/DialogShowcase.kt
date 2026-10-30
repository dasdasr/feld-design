package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.designsystem.components.button.FeldButton
import com.feldelysium.designsystem.components.button.FeldDialogActions
import com.feldelysium.designsystem.components.container.FeldDialog
import com.feldelysium.designsystem.components.input.FeldTextField


@Composable
internal fun DialogShowcase(onBack: () -> Unit) {
    var open by rememberSaveable { mutableStateOf(false) }
    var confirmEnabled by rememberSaveable { mutableStateOf(true) }
    var name by rememberSaveable { mutableStateOf("") }

    if (open) {
        FeldDialog(
            title = "Rename scene",
            eyebrow = "SCENES",
            onDismiss = { open = false },
            footer = {
                FeldDialogActions(
                    confirmLabel = "Save",
                    onConfirm = { open = false },
                    confirmEnabled = confirmEnabled,
                    cancelLabel = "Cancel",
                    onCancel = { open = false },
                )
            },
        ) {
            FeldTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Scene title",
                singleLine = true,
            )
        }
    }

    ShowcaseScreen(
        title = "Dialog",
        eyebrow = "COMPONENTS",
        onBack = onBack,
        preview = { seed ->
            FeldButton(text = "Open dialog", onClick = { open = true }, seed = seed)
        },
        controls = {
            ControlSwitch("confirm enabled", confirmEnabled) { confirmEnabled = it }
        },
        usage = """
            FeldDialog(
                title = "Rename scene",
                eyebrow = "SCENES",
                onDismiss = { },
                footer = {
                    FeldDialogActions(
                        confirmLabel = "Save",
                        onConfirm = { },
                        cancelLabel = "Cancel",
                        onCancel = { },
                    )
                },
            ) {
                FeldTextField(value = name, onValueChange = { name = it })
            }
        """.trimIndent(),
        notes = listOf("The footer slot takes any content; FeldDialogActions is the standard pair."),
    )
}
