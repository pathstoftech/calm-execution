package com.example.a30daysofcalmexecution.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabelTone
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.core.model.TipId

fun LazyListScope.tipSectionFeed(
    sections: List<HomeFeedSectionUi>,
    selectedTipId: TipId?,
    onOpenTip: (TipId) -> Unit,
    onToggleBookmark: (TipId) -> Unit,
    onToggleCompleted: (TipId) -> Unit,
) {
    sections.forEach { section ->
        item(
            key = "section_header_${section.key}"
        ) {
            TipSectionHeader(
                title = section.title,
                itemCount = section.items.size
            )
        }

        items(
            count = section.items.size,
            key = { index -> section.items[index].id.value }
        ) { index ->
            val item = section.items[index]

            TipCard(
                item = item,
                isSelected = item.id == selectedTipId,
                onOpen = {
                    onOpenTip(item.id)
                },
                onToggleBookmark = {
                    onToggleBookmark(item.id)
                },
                onToggleCompleted = {
                    onToggleCompleted(item.id)
                },
            )
        }
    }
}

@Composable
private fun TipSectionHeader(
    title: String,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
    ) {
        Text(
            text = title,
            style = CalmTheme.typographyTokens.sectionTitle,
            color = CalmTheme.colorTokens.onScreenBackground
        )

        CalmLabel(
            text = "$itemCount tips",
            tone = CalmLabelTone.Neutral
        )
    }
}