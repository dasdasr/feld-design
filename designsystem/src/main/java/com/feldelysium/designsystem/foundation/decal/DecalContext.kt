package com.feldelysium.designsystem.foundation.decal

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Immutable
public data class DecalContext(
    val groupName: String = "",
    val index: Int? = null,
    val count: Int? = null,
    val rangeFrom: Int? = null,
    val rangeTo: Int? = null,
    val title: String? = null,
    val appVersion: String? = null,
    val date: String = retroDate(),
)

internal fun retroDate(): String = SimpleDateFormat("MM/dd/yy", Locale.US).format(Date())

public val LocalFeldDecalContext: ProvidableCompositionLocal<DecalContext> = compositionLocalOf { DecalContext() }
