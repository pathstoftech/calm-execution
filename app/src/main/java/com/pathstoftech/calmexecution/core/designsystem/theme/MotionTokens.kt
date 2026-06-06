package com.pathstoftech.calmexecution.core.designsystem.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Immutable

@Immutable
data class CalmMotionTokens(
    val instantDurationMillis: Int,
    val shortDurationMillis: Int,
    val mediumDurationMillis: Int,
    val longDurationMillis: Int,

    val standardEasing: Easing,
    val enterEasing: Easing,
    val exitEasing: Easing
)

internal val DefaultCalmMotionTokens = CalmMotionTokens(
    instantDurationMillis = 0,
    shortDurationMillis = 150,
    mediumDurationMillis = 250,
    longDurationMillis = 400,

    standardEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f),
    enterEasing = CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f),
    exitEasing = CubicBezierEasing(0.3f, 0.0f, 1.0f, 1.0f)
)

internal val ReducedCalmMotionTokens = CalmMotionTokens(
    instantDurationMillis = 0,
    shortDurationMillis = 0,
    mediumDurationMillis = 0,
    longDurationMillis = 0,

    standardEasing = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f),
    enterEasing = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f),
    exitEasing = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f)
)