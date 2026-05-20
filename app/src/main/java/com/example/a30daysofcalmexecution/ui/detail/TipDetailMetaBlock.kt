package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmChip
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabelTone
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmTipImage
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
            CalmTipImage(
                imageResId = tip.imageResId,
                contentDescription = if (tip.imageDecorative) {
                    null
                } else {
                    tip.imageContentDescription
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
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