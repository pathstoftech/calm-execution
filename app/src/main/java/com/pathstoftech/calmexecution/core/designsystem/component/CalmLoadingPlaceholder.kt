package com.pathstoftech.calmexecution.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme

@Composable
fun CalmLoadingPlaceholder(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.alpha(LoadingPlaceholderAlpha),
        shape = CalmTheme.shapeTokens.chipContainer,
        color = CalmTheme.colorTokens.cardContainerVariant,
        contentColor = CalmTheme.colorTokens.onCardContainerVariant,
        tonalElevation = CalmTheme.elevationTokens.none,
        shadowElevation = CalmTheme.elevationTokens.none,
        content = {}
    )
}

@Composable
fun CalmLoadingPanel(
    modifier: Modifier = Modifier
) {
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
            CalmLoadingPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CalmTheme.spacingTokens.large)
            )

            CalmLoadingPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CalmTheme.spacingTokens.medium)
            )

            CalmLoadingPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CalmTheme.spacingTokens.small)
            )

            CalmLoadingPlaceholder(
                modifier = Modifier
                    .width(CalmTheme.spacingTokens.extraLarge * LoadingShortLineMultiplier)
                    .height(CalmTheme.spacingTokens.large)
            )
        }
    }
}

private const val LoadingPlaceholderAlpha = 0.42f
private const val LoadingShortLineMultiplier = 4