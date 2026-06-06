package com.pathstoftech.calmexecution.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class CalmElevationTokens(
    val none: Dp,

    val low: Dp,
    val medium: Dp,
    val high: Dp,

    val screen: Dp,
    val cardResting: Dp,
    val cardRaised: Dp,
    val topAppBar: Dp,
    val dialog: Dp
)

internal val DefaultCalmElevationTokens = CalmElevationTokens(
    none = 0.dp,

    low = 1.dp,
    medium = 3.dp,
    high = 6.dp,

    screen = 0.dp,
    cardResting = 1.dp,
    cardRaised = 3.dp,
    topAppBar = 0.dp,
    dialog = 6.dp
)