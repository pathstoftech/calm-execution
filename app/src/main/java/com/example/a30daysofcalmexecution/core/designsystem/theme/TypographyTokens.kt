package com.example.a30daysofcalmexecution.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
data class CalmTypographyTokens(
    val appTitle: TextStyle,
    val screenTitle: TextStyle,
    val screenSubtitle: TextStyle,

    val sectionTitle: TextStyle,
    val sectionSubtitle: TextStyle,

    val cardTitle: TextStyle,
    val cardBody: TextStyle,
    val cardSupportingText: TextStyle,

    val detailTitle: TextStyle,
    val detailBody: TextStyle,

    val chipLabel: TextStyle,
    val actionLabel: TextStyle,
    val metadataLabel: TextStyle
)

internal fun androidx.compose.material3.Typography.toCalmTypographyTokens(): CalmTypographyTokens {
    return CalmTypographyTokens(
        appTitle = headlineMedium,
        screenTitle = headlineSmall,
        screenSubtitle = bodyLarge,

        sectionTitle = titleLarge,
        sectionSubtitle = bodyMedium,

        cardTitle = titleMedium,
        cardBody = bodyMedium,
        cardSupportingText = bodySmall,

        detailTitle = headlineSmall,
        detailBody = bodyLarge,

        chipLabel = labelMedium,
        actionLabel = labelLarge,
        metadataLabel = labelSmall
    )
}