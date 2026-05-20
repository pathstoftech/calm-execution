package com.example.a30daysofcalmexecution.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmChip
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabelTone
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmTipImage
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun TipCard(
    item: TipCardUi,
    onOpen: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                onClick = onOpen
            )
            .testTag("tip_card_${item.id.value}"),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = CalmTheme.colorTokens.cardContainer,
        contentColor = CalmTheme.colorTokens.onCardContainer,
        tonalElevation = CalmTheme.elevationTokens.cardResting,
        shadowElevation = CalmTheme.elevationTokens.none
    ) {
        Column {
            CalmTipImage(
                imageResId = item.imageResId,
                contentDescription = if (item.imageDecorative) {
                    null
                } else {
                    item.imageContentDescription
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
                    text = item.dayLabel,
                    tone = CalmLabelTone.Primary
                )

                Text(
                    text = item.title,
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = CalmTheme.colorTokens.onCardContainer,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = item.previewText,
                    style = CalmTheme.typographyTokens.cardBody,
                    color = CalmTheme.colorTokens.onCardContainerVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                TipCardMetadataRow(
                    item = item,
                    onToggleBookmark = onToggleBookmark,
                    onToggleCompleted = onToggleCompleted
                )
            }
        }
    }
}

@Composable
private fun TipCardMetadataRow(
    item: TipCardUi,
    onToggleBookmark: () -> Unit,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
    ) {
        CalmChip(
            label = item.categoryLabel
        )

        TextButton(
            onClick = onToggleBookmark,
            modifier = Modifier
                .defaultMinSize(minWidth = 112.dp)
                .semantics {
                    stateDescription = if (item.isBookmarked) {
                        "Bookmarked"
                    } else {
                        "Not bookmarked"
                    }
                },
        ) {
            Text(
                text = if (item.isBookmarked) {
                    "Bookmarked"
                } else {
                    "Bookmark"
                },
            )
        }

        TextButton(
            onClick = onToggleCompleted,
            modifier = Modifier
                .defaultMinSize(minWidth = 112.dp)
                .semantics {
                    stateDescription = if (item.isCompleted) {
                        "Completed"
                    } else {
                        "Not completed"
                    }
                },
        ) {
            Text(
                text = if (item.isCompleted) {
                    "Completed"
                } else {
                    "Complete"
                },
            )
        }
    }
}