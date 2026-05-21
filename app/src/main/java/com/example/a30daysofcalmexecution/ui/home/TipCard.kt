package com.example.a30daysofcalmexecution.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.a30daysofcalmexecution.R
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
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        CalmTheme.colorTokens.cardContainer
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        CalmTheme.colorTokens.onCardContainer
    }

    val supportingContentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
    } else {
        CalmTheme.colorTokens.onCardContainerVariant
    }

    val selectedBorder = if (isSelected) {
        BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
        )
    } else {
        null
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                selected = isSelected
            }
            .clickable(
                role = Role.Button,
                onClick = onOpen,
            )
            .testTag("tip_card_${item.id.value}"),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = if (isSelected) {
            CalmTheme.elevationTokens.none
        } else {
            CalmTheme.elevationTokens.cardResting
        },
        shadowElevation = CalmTheme.elevationTokens.none,
        border = selectedBorder,
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
                verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
            ) {
                TipCardHeaderRow(
                    item = item,
                    onToggleBookmark = onToggleBookmark,
                )

                Text(
                    text = item.title,
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = contentColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = item.previewText,
                    style = CalmTheme.typographyTokens.cardBody,
                    color = supportingContentColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )

                TipCardCompletionRow(
                    item = item,
                    onToggleCompleted = onToggleCompleted,
                )
            }
        }
    }
}

@Composable
private fun TipCardHeaderRow(
    item: TipCardUi,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalmLabel(
            text = item.dayLabel,
            tone = CalmLabelTone.Primary,
        )

        CalmChip(
            label = item.categoryLabel,
        )

        Spacer(
            modifier = Modifier.weight(1f),
        )

        IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier
                .semantics {
                    stateDescription = if (item.isBookmarked) {
                        "Bookmarked"
                    } else {
                        "Not bookmarked"
                    }
                }
                .testTag("tip_card_bookmark_${item.id.value}"),
        ) {
            Icon(
                painter = painterResource(
                    id = if (item.isBookmarked) {
                        R.drawable.ic_bookmark_24
                    } else {
                        R.drawable.ic_bookmark_border_24
                    },
                ),
                contentDescription = "Bookmark",
                tint = CalmTheme.colorTokens.primaryAction
            )
        }
    }
}

@Composable
private fun TipCardCompletionRow(
    item: TipCardUi,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
                }
                .testTag("tip_card_complete_${item.id.value}"),
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