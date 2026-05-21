package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import com.example.a30daysofcalmexecution.R
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmChip
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabelTone
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmTipImage
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun TipDetailMetaBlock(
    tip: TipDetailUi,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = CalmTheme.colorTokens.cardContainer,
        contentColor = CalmTheme.colorTokens.onCardContainer,
        tonalElevation = CalmTheme.elevationTokens.cardResting,
        shadowElevation = CalmTheme.elevationTokens.none,
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
                verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
            ) {
                TipDetailMetadataRow(
                    tip = tip,
                    onToggleBookmark = onToggleBookmark,
                )

                Text(
                    text = tip.title,
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = CalmTheme.colorTokens.onCardContainer,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun TipDetailMetadataRow(
    tip: TipDetailUi,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalmLabel(
            text = tip.dayLabel,
            tone = CalmLabelTone.Accent,
        )

        CalmChip(
            label = tip.categoryLabel,
        )

        Spacer(
            modifier = Modifier.weight(1f),
        )

        IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier
                .semantics {
                    stateDescription = if (tip.isBookmarked) {
                        "Bookmarked"
                    } else {
                        "Not bookmarked"
                    }
                }
                .testTag("tip_detail_bookmark_${tip.id.value}"),
        ) {
            Icon(
                painter = painterResource(
                    id = if (tip.isBookmarked) {
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