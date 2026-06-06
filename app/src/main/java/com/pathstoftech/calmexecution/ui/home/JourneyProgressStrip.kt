package com.pathstoftech.calmexecution.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pathstoftech.calmexecution.core.designsystem.component.CalmLabel
import com.pathstoftech.calmexecution.core.designsystem.component.CalmLabelTone
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun JourneyProgressStrip(
    progress: JourneyProgressUi,
    modifier: Modifier = Modifier
) {
    val safeTotalCount = progress.totalCount.coerceAtLeast(0)
    val safeCompletedCount = progress.completedCount.coerceAtLeast(0)
    val safeCompletedFraction = progress.completionFraction.coerceIn(0f, 1f)

    val statusLabel =
        when {
            safeTotalCount == 0 -> "No days"

            safeCompletedCount >= safeTotalCount -> "Complete"

            progress.currentDay != null -> {
                String.format(Locale.US, "Day %02d", progress.currentDay)
            }

            else -> "Not started"
        }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = CalmTheme.colorTokens.cardContainer,
        contentColor = CalmTheme.colorTokens.onCardContainer,
        tonalElevation = CalmTheme.elevationTokens.cardResting,
        shadowElevation = CalmTheme.elevationTokens.none
    ) {
        Column(
            modifier = Modifier.padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Journey progress",
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = CalmTheme.colorTokens.onCardContainer
                )

                CalmLabel(
                    text = statusLabel,
                    tone = CalmLabelTone.Accent
                )
            }

            LinearProgressIndicator(
                progress = { safeCompletedFraction },
                modifier = Modifier.fillMaxWidth(),
                color = CalmTheme.colorTokens.primaryAction,
                trackColor = CalmTheme.colorTokens.cardContainerVariant
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$safeCompletedCount of $safeTotalCount days complete",
                    style = CalmTheme.typographyTokens.cardSupportingText,
                    color = CalmTheme.colorTokens.onCardContainerVariant
                )

                Text(
                    text = "${(safeCompletedFraction * 100).roundToInt()}%",
                    style = CalmTheme.typographyTokens.metadataLabel,
                    color = CalmTheme.colorTokens.onCardContainerVariant
                )
            }
        }
    }
}