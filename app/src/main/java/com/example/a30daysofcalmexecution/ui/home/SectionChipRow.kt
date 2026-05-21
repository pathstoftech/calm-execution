package com.example.a30daysofcalmexecution.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmChip
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.core.model.SectionKey

@Composable
fun SectionChipRow(
    tabs: List<SectionTabUi>,
    bookmarkedOnly: Boolean,
    onSelectSection: (SectionKey?) -> Unit,
    onSetBookmarkedFilter: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAllSelected = tabs.none { tab -> tab.isSelected }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = CalmTheme.spacingTokens.inlineGap)
    ) {
        item(
            key = "all_sections"
        ) {
            CalmChip(
                label = "All",
                selected = isAllSelected,
                onClick = {
                    onSelectSection(null)
                }
            )
        }

        item(
            key = "bookmarked_tips"
        ) {
            CalmChip(
                label = "Bookmarked",
                selected = bookmarkedOnly,
                onClick = {
                    onSetBookmarkedFilter(!bookmarkedOnly)
                }
            )
        }

        items(
            count = tabs.size,
            key = { index -> tabs[index].key }
        ) { index ->
            val tab = tabs[index]

            CalmChip(
                label = tab.title,
                selected = tab.isSelected,
                onClick = {
                    onSelectSection(tab.key)
                }
            )
        }
    }
}