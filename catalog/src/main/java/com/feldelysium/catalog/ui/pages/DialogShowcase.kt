package com.feldelysium.catalog.ui.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.feldelysium.catalog.ui.ControlSwitch
import com.feldelysium.catalog.ui.ControlText
import com.feldelysium.catalog.ui.ShowcaseScreen
import com.feldelysium.catalog.ui.ShowcaseSpec
import com.feldelysium.catalog.ui.Subject
import com.feldelysium.catalog.ui.quoted
import com.feldelysium.designsystem.components.button.FeldButton
import com.feldelysium.designsystem.components.button.FeldDialogActions
import com.feldelysium.designsystem.components.container.FeldDialog
import com.feldelysium.designsystem.components.input.FeldTextField



private val DialogSubjects = listOf(
    Subject("Dialog") { _ ->
        var open by remember { mutableStateOf(false) }
        var title by remember { mutableStateOf("Rename scene") }
        var eyebrow by remember { mutableStateOf("Scenes") }
        var confirmLabel by remember { mutableStateOf("Save") }
        var confirmEnabled by remember { mutableStateOf(true) }
        var name by remember { mutableStateOf("") }
        ShowcaseSpec(
            preview = {
                FeldButton(text = "Open dialog", onClick = { open = true })
                if (open) {
                    FeldDialog(
                        title = title,
                        eyebrow = eyebrow,
                        onDismiss = { open = false },
                        footer = {
                            FeldDialogActions(
                                confirmLabel = confirmLabel,
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
            },
            controls = {
                ControlText("title", title) { title = it }
                ControlText("eyebrow", eyebrow) { eyebrow = it }
                ControlText("confirm label", confirmLabel) { confirmLabel = it }
                ControlSwitch("confirm enabled", confirmEnabled) { confirmEnabled = it }
            },
            code = {
                "FeldDialog(\n" +
                        "    title = ${title.quoted()},\n" +
                        "    eyebrow = ${eyebrow.quoted()},\n" +
                        "    onDismiss = { },\n" +
                        "    footer = {\n" +
                        "        FeldDialogActions(\n" +
                        "            confirmLabel = ${confirmLabel.quoted()},\n" +
                        "            onConfirm = { },\n" +
                        (if (confirmEnabled) "" else "            confirmEnabled = false,\n") +
                        "            cancelLabel = \"Cancel\",\n" +
                        "            onCancel = { },\n" +
                        "        )\n" +
                        "    },\n" +
                        ") {\n" +
                        "    // content\n" +
                        "}"
            },
            notes = listOf("Tap “Open dialog” on the stage."),
        )
    },
)

@Composable
internal fun DialogShowcase(onBack: () -> Unit) =
    ShowcaseScreen("Dialog", "COMPONENTS", onBack, DialogSubjects)
