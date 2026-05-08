package com.example.a30daysofcalmexecution.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class CalmColorTokens(
    val screenBackground: Color,
    val onScreenBackground: Color,

    val cardContainer: Color,
    val onCardContainer: Color,

    val cardContainerVariant: Color,
    val onCardContainerVariant: Color,

    val primaryAction: Color,
    val onPrimaryAction: Color,

    val secondaryAction: Color,
    val onSecondaryAction: Color,

    val accent: Color,
    val onAccent: Color,

    val divider: Color,
    val focusRing: Color,

    val error: Color,
    val onError: Color
)

internal fun ColorScheme.toCalmColorTokens(): CalmColorTokens {
    return CalmColorTokens(
        screenBackground = background,
        onScreenBackground = onBackground,

        cardContainer = surface,
        onCardContainer = onSurface,

        cardContainerVariant = surfaceVariant,
        onCardContainerVariant = onSurfaceVariant,

        primaryAction = primary,
        onPrimaryAction = onPrimary,

        secondaryAction = secondary,
        onSecondaryAction = onSecondary,

        accent = tertiary,
        onAccent = onTertiary,

        divider = outlineVariant,
        focusRing = primary,

        error = error,
        onError = onError
    )
}
