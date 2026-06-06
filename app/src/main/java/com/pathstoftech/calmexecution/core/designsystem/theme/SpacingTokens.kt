package com.pathstoftech.calmexecution.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class CalmSpacingTokens(
    val none: Dp,

    val extraSmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val extraLarge: Dp,

    val screenPadding: Dp,
    val sectionGap: Dp,
    val cardPadding: Dp,
    val cardGap: Dp,
    val inlineGap: Dp
)

internal val DefaultCalmSpacingTokens = CalmSpacingTokens(
    none = 0.dp,

    extraSmall = 4.dp,
    small = 8.dp,
    medium = 12.dp,
    large = 16.dp,
    extraLarge = 24.dp,

    screenPadding = 24.dp,
    sectionGap = 24.dp,
    cardPadding = 16.dp,
    cardGap = 12.dp,
    inlineGap = 8.dp
)