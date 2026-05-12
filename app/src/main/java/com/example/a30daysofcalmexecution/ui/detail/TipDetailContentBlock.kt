package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun TipDetailContentBlock(
    tip: TipDetailUi,
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
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.cardGap)
        ) {
            TipDetailTextSection(
                section = tip.problem
            )

            TipDetailSectionDivider()

            TipDetailTextSection(
                section = tip.recommendation
            )

            TipDetailSectionDivider()

            TipDetailTextSection(
                section = tip.whyItHelps
            )

            TipDetailSectionDivider()

            TipDetailTextSection(
                section = tip.tryToday
            )
        }
    }
}

@Composable
private fun TipDetailTextSection(
    section: TipDetailTextSectionUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
    ) {
        Text(
            text = section.title,
            style = CalmTheme.typographyTokens.cardTitle,
            color = CalmTheme.colorTokens.onCardContainer
        )
        Text(
            text = section.body,
            style = CalmTheme.typographyTokens.cardBody,
            color = CalmTheme.colorTokens.onCardContainerVariant
        )
    }
}

@Composable
private fun TipDetailSectionDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        color = CalmTheme.colorScheme.outlineVariant
    )
}