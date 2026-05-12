package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmChip
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabelTone
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun TipDetailMetaBlock(
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
        Column {
            TipDetailImagePlaceholder(
                imageKey = tip.imageKey,
                imageContentDescription = tip.imageContentDescription
            )

            Column(
                modifier = Modifier.padding(CalmTheme.spacingTokens.cardPadding),
                verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
            ) {
                CalmLabel(
                    text = tip.dayLabel,
                    tone = CalmLabelTone.Accent
                )

                Text(
                    text = tip.title,
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = CalmTheme.colorTokens.onCardContainer,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                CalmChip(
                    label = tip.categoryLabel
                )
            }
        }
    }
}

@Composable
private fun TipDetailImagePlaceholder(
    imageKey: String,
    imageContentDescription: String?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
        color = CalmTheme.colorTokens.cardContainerVariant,
        contentColor = CalmTheme.colorTokens.onCardContainerVariant,
        tonalElevation = CalmTheme.elevationTokens.none,
        shadowElevation = CalmTheme.elevationTokens.none
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(CalmTheme.spacingTokens.cardPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = imageContentDescription ?: imageKey,
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}