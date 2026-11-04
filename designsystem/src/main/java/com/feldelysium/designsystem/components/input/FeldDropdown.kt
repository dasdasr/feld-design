package com.feldelysium.designsystem.components.input

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feldelysium.designsystem.components.content.FeldIcon
import com.feldelysium.designsystem.components.content.FeldIconId
import com.feldelysium.designsystem.foundation.material.FeldPaper
import com.feldelysium.designsystem.foundation.material.paperMaterial
import com.feldelysium.designsystem.foundation.seed.rememberSeed
import com.feldelysium.designsystem.foundation.shape.RoughShape
import com.feldelysium.designsystem.foundation.shape.RoughStyle
import com.feldelysium.designsystem.theme.FeldPalette

private val DropdownRough = RoughStyle(
    roughness = 0.6.dp,
    step = 40.dp,
    corner = 3.dp,
    cornerVariation = 0.2f,
    cornerJitter = 0.2f,
    tornChance = 0.03f,
    jerkChance = 0.04f,
)

/** A field that opens a [FeldMenu] to pick one of [options]; the chosen option is marked with a check. */
@Composable
public fun <T> FeldDropdown(
    options: List<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    optionLabel: (T) -> String = { it.toString() },
    label: String? = null,
    placeholder: String = "",
    enabled: Boolean = true,
    seed: Int = rememberSeed(),
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = remember(seed) { RoughShape(seed, DropdownRough) }
    val chevron by animateFloatAsState(if (expanded) 270f else 90f, tween(160), label = "chevron")

    Column(modifier) {
        if (label != null) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.2.sp),
                color = FeldPalette.Muted,
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }
        Box {
            Row(
                Modifier
                    .fillMaxWidth()
                    .alpha(if (enabled) 1f else 0.45f)
                    .paperMaterial(shape, FeldPaper.Field)
                    .feldClickable(
                        enabled = enabled,
                        role = Role.DropdownList,
                        onClick = { expanded = true },
                    )
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = (selected?.let(optionLabel) ?: placeholder).uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.2.sp),
                    color = if (selected != null) FeldPalette.Cream else FeldPalette.Muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                FeldIcon(
                    id = FeldIconId.Chevron,
                    size = 16.dp,
                    film = false,
                    modifier = Modifier.graphicsLayer { rotationZ = chevron },
                )
            }
            FeldMenu(
                expanded = expanded,
                onDismiss = { expanded = false },
                items = options.map { option ->
                    FeldMenuItem(
                        label = optionLabel(option),
                        icon = if (option == selected) FeldIconId.Accept else null,
                        onClick = { onSelect(option) },
                    )
                },
                seed = seed,
                offset = DpOffset(0.dp, 4.dp),
            )
        }
    }
}
