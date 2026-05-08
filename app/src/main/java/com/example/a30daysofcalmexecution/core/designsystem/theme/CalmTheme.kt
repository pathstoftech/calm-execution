package com.example.a30daysofcalmexecution.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

object CalmTheme {

    val colorScheme: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val colorTokens: CalmColorTokens
        @Composable
        get() = MaterialTheme.colorScheme.toCalmColorTokens()

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val typographyTokens: CalmTypographyTokens
        @Composable
        get() = MaterialTheme.typography.toCalmTypographyTokens()

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val shapeTokens: CalmShapeTokens
        @Composable
        get() = MaterialTheme.shapes.toCalmShapeTokens()

    val spacingTokens: CalmSpacingTokens
        get() = DefaultCalmSpacingTokens

    val elevationTokens: CalmElevationTokens
        get() = DefaultCalmElevationTokens

    val motionTokens: CalmMotionTokens
        get() = DefaultCalmMotionTokens
}